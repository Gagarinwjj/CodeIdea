#SQL Server 2005引入的新方法。

```sql
SELECT  *
FROM    ( SELECT    ROW_NUMBER() OVER ( ORDER BY keyField DESC ) AS rowNum ,
                    *
          FROM      tableName
        ) AS t
WHERE   rowNum > @START--[比如:90]
        AND rowNum <= @END--[比如:100]=>[返回91-100]
```
```sql
SELECT TOP ( @PAGESIZE )
        *
FROM    ( SELECT    ROW_NUMBER() OVER ( ORDER BY keyField DESC ) AS rowNum ,
                    *
          FROM      tableName
        ) AS t
WHERE   rowNum > ( @PAGEINDEX - 1 ) * ( @PAGESIZE )
```
	
##`其中:`
	keyField为表tableName的一个字段(最好是主键)；
	tableName为查询的表名；
	DESC可以按需换为ASC；
	start为要取的结果集的起始记录
	end为要取的结果集的结尾记录，可由：(start + pageSize)计算得出。
 
---

#一般方法
>表中主键必须为标识列，[ID] int IDENTITY (1,1)
  
##1.建立表

```sql
CREATE TABLE [TestTable]
    (
      [ID] [int] IDENTITY(1, 1)
                 NOT NULL ,
      [FirstName] [nvarchar](100) COLLATE Chinese_PRC_CI_AS
                                  NULL ,
      [LastName] [nvarchar](100) COLLATE Chinese_PRC_CI_AS
                                 NULL ,
      [Country] [nvarchar](50) COLLATE Chinese_PRC_CI_AS
                               NULL ,
      [Note] [nvarchar](2000) COLLATE Chinese_PRC_CI_AS
                              NULL
    )
ON  [PRIMARY]
GO
```

##2.插入数据:

>插入2万条，用更多的数据测试会明显一些

```sql
SET IDENTITY_INSERT TestTable ON

DECLARE @i INT
SET @i = 1
WHILE @i <= 20000 
    BEGIN
        INSERT  INTO TestTable
                ( [id] ,
                  FirstName ,
                  LastName ,
                  Country ,
                  Note
                )
        VALUES  ( @i ,
                  'FirstName_XXX' ,
                  'LastName_XXX' ,
                  'Country_XXX' ,
                  'Note_XXX'
                )
        SET @i = @i + 1
    END

SET IDENTITY_INSERT TestTable OFF
```
---

## 1.分页方案一：(利用Not In和SELECT TOP分页)

>语句形式：
```sql
--示例
SELECT TOP 10
        *
FROM    TestTable
WHERE   ( ID NOT IN ( SELECT TOP 20
                                id
                      FROM      TestTable
                      ORDER BY  id ) )
ORDER BY ID
```
```sql
--通用
 SELECT TOP ( @pageSize )
        *
 FROM   dbo.PayPoints
 WHERE  ( ID NOT IN ( SELECT TOP ( @pageSize * ( @pageFor - 1 ) )
                                id
                      FROM      PayPoints
                      ORDER BY  id DESC ) )
 ORDER BY ID DESC
```
 
##2.分页方案二：(利用ID大于多少和SELECT TOP分页）
>语句形式：
```sql
--示例
SELECT TOP 10
        *
FROM    TestTable
WHERE   ( id > ( SELECT MAX(id)
                 FROM   ( SELECT TOP 20
                                    id
                          FROM      TestTable
                          ORDER BY  id
                        ) AS T
               ) )
ORDER BY id
```  
```sql
--通用
SELECT TOP ( @pageSize )
        *
FROM    TestTable
WHERE   ( ID > ( SELECT MAX(ID)
                 FROM   ( SELECT TOP ( @pageSize * ( @pageFor - 1 ) )
                                    ID
                          FROM      TestTable
                          ORDER BY  ID
                        ) AS T
               ) )
ORDER BY ID
```

##3.分页方案三：(利用SQL的游标存储过程分页)

```sql
ALTER  PROCEDURE SqlPager
    @sqlstr NVARCHAR(4000) , --查询字符串
    @pageFor INT , --第N页
    @pageSize INT --每页个数
AS 
    SET nocount ON
    DECLARE @P1 INT , --P1是游标的id
        @rowcount INT ,
        @startIndex INT
    EXEC sp_cursoropen @P1 OUTPUT, @sqlstr, @scrollopt = 1, @ccopt = 1,
        @rowcount = @rowcount OUTPUT
    SELECT  CEILING(1.0 * @rowcount / @pageSize) AS 总页数
    --@rowcount as 总行数,@pageFor as 当前页 
    SET @startIndex = ( @pageFor - 1 ) * @pageSize + 1
    EXEC sp_cursorfetch @cursor = @P1, @fetchtype = 16, @rownum = @startIndex,
        @nrows = @pageSize 
    EXEC sp_cursorclose @P1
    SET nocount OFF
  go
    
EXEC SqlPager 'select * from paypoints order by id desc', 1, 3
```
>参考：[sp_cursorfetch (Transact-SQL)](http://technet.microsoft.com/zh-cn/ff848736)
>其它的方案：如果没有主键，可以用临时表，也可以用方案三做，但是效率会低。
>建议优化的时候，加上主键和索引，查询效率会提高。

##4.通过SQL 查询分析器，显示比较：我的结论是:
- 分页方案二：(利用ID大于多少和SELECT TOP分页）效率最高，需要拼接SQL语句
- 分页方案一：(利用Not In和SELECT TOP分页)   效率次之，需要拼接SQL语句
- 分页方案三：(利用SQL的游标存储过程分页)    效率最差，但是最为通用 
