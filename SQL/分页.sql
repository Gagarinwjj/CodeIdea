SQL Server 2005引入的新方法。
 
SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY keyField DESC) AS rowNum, * FROM tableName) AS t WHERE rowNum > start[比如:90] AND rowNum <= end[比如:100]=>[返回91-100]

SELECT top (PAGESIZE[比如:10]) FROM (SELECT ROW_NUMBER() OVER(ORDER BY keyField DESC) AS rowNum, * FROM tableName) AS t WHERE rowNum >(PAGEINDEX-1)*(PAGESIZE)[比如:90]
 =>[返回91-100]
其中：
 
keyField为表tableName的一个字段(最好是主键)；
tableName为查询的表名；
DESC可以按需换为ASC；
start为要取的结果集的起始记录
end为要取的结果集的结尾记录，可由：(start + pageSize)计算得出。
 
====================================================================


一般方法:表中主键必须为标识列，[ID] int IDENTITY (1,1)
  
  建立表

CREATE TABLE [TestTable] (
[ID] [int] IDENTITY (1, 1) NOT NULL ,
[FirstName] [nvarchar] (100) COLLATE Chinese_PRC_CI_AS NULL ,
[LastName] [nvarchar] (100) COLLATE Chinese_PRC_CI_AS NULL ,
[Country] [nvarchar] (50) COLLATE Chinese_PRC_CI_AS NULL ,
[Note] [nvarchar] (2000) COLLATE Chinese_PRC_CI_AS NULL
) ON [PRIMARY]
GO

插入数据：(2万条，用更多的数据测试会明显一些)
SET IDENTITY_INSERT TestTable ON

declare @i int
set @i=1
while @i<=20000
begin
insert into TestTable([id], FirstName, LastName, Country,Note) values(@i, 'FirstName_XXX','LastName_XXX','Country_XXX','Note_XXX')
set @i=@i+1
end

SET IDENTITY_INSERT TestTable OFF




  1.分页方案一：(利用Not In和SELECT TOP分页)
语句形式：   
SELECT TOP 10 *
FROM TestTable
WHERE (ID NOT IN
          (SELECT TOP 20 id
         FROM TestTable
         ORDER BY id))
ORDER BY ID


SELECT TOP 页大小 *
FROM TestTable
WHERE (ID NOT IN
          (SELECT TOP 页大小*页数 id
         FROM 表
         ORDER BY id))
ORDER BY ID
   
   
   
   
   
   2.分页方案二：(利用ID大于多少和SELECT TOP分页）
语句形式：  
SELECT TOP 10 *
FROM TestTable
WHERE (ID >
          (SELECT MAX(id)
         FROM (SELECT TOP 20 id
                 FROM TestTable
                 ORDER BY id) AS T))
ORDER BY ID


SELECT TOP 页大小 *
FROM TestTable
WHERE (ID >
          (SELECT MAX(id)
         FROM (SELECT TOP 页大小*页数 id
                 FROM 表
                 ORDER BY id) AS T))
ORDER BY ID
 
 
 
 
 
  3.分页方案三：(利用SQL的游标存储过程分页)

create  procedure SqlPager
@sqlstr nvarchar(4000), --查询字符串
@currentpage int, --第N页
@pagesize int --每页行数
as
set nocount on
declare @P1 int, --P1是游标的id
 @rowcount int
exec sp_cursoropen @P1 output,@sqlstr,@scrollopt=1,@ccopt=1, @rowcount=@rowcount output
select ceiling(1.0*@rowcount/@pagesize) as 总页数--,@rowcount as 总行数,@currentpage as 当前页 
set @currentpage=(@currentpage-1)*@pagesize+1
exec sp_cursorfetch @P1,16,@currentpage,@pagesize 
exec sp_cursorclose @P1
set nocount off



其它的方案：如果没有主键，可以用临时表，也可以用方案三做，但是效率会低。
建议优化的时候，加上主键和索引，查询效率会提高。

通过SQL 查询分析器，显示比较：我的结论是:
分页方案二：(利用ID大于多少和SELECT TOP分页）效率最高，需要拼接SQL语句
分页方案一：(利用Not In和SELECT TOP分页)   效率次之，需要拼接SQL语句
分页方案三：(利用SQL的游标存储过程分页)    效率最差，但是最为通用 
