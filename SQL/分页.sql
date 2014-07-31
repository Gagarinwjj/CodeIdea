SQL Server 2005������·�����
 
SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY keyField DESC) AS rowNum, * FROM tableName) AS t WHERE rowNum > start[����:90] AND rowNum <= end[����:100]=>[����91-100]

SELECT top (PAGESIZE[����:10]) FROM (SELECT ROW_NUMBER() OVER(ORDER BY keyField DESC) AS rowNum, * FROM tableName) AS t WHERE rowNum >(PAGEINDEX-1)*(PAGESIZE)[����:90]
 =>[����91-100]
���У�
 
keyFieldΪ��tableName��һ���ֶ�(���������)��
tableNameΪ��ѯ�ı�����
DESC���԰��軻ΪASC��
startΪҪȡ�Ľ��������ʼ��¼
endΪҪȡ�Ľ�����Ľ�β��¼�����ɣ�(start + pageSize)����ó���
 
====================================================================


һ�㷽��:������������Ϊ��ʶ�У�[ID] int IDENTITY (1,1)
  
  ������

CREATE TABLE [TestTable] (
[ID] [int] IDENTITY (1, 1) NOT NULL ,
[FirstName] [nvarchar] (100) COLLATE Chinese_PRC_CI_AS NULL ,
[LastName] [nvarchar] (100) COLLATE Chinese_PRC_CI_AS NULL ,
[Country] [nvarchar] (50) COLLATE Chinese_PRC_CI_AS NULL ,
[Note] [nvarchar] (2000) COLLATE Chinese_PRC_CI_AS NULL
) ON [PRIMARY]
GO

�������ݣ�(2�������ø�������ݲ��Ի�����һЩ)
SET IDENTITY_INSERT TestTable ON

declare @i int
set @i=1
while @i<=20000
begin
insert into TestTable([id], FirstName, LastName, Country,Note) values(@i, 'FirstName_XXX','LastName_XXX','Country_XXX','Note_XXX')
set @i=@i+1
end

SET IDENTITY_INSERT TestTable OFF




  1.��ҳ����һ��(����Not In��SELECT TOP��ҳ)
�����ʽ��   
SELECT TOP 10 *
FROM TestTable
WHERE (ID NOT IN
          (SELECT TOP 20 id
         FROM TestTable
         ORDER BY id))
ORDER BY ID


SELECT TOP ҳ��С *
FROM TestTable
WHERE (ID NOT IN
          (SELECT TOP ҳ��С*ҳ�� id
         FROM ��
         ORDER BY id))
ORDER BY ID
   
   
   
   
   
   2.��ҳ��������(����ID���ڶ��ٺ�SELECT TOP��ҳ��
�����ʽ��  
SELECT TOP 10 *
FROM TestTable
WHERE (ID >
          (SELECT MAX(id)
         FROM (SELECT TOP 20 id
                 FROM TestTable
                 ORDER BY id) AS T))
ORDER BY ID


SELECT TOP ҳ��С *
FROM TestTable
WHERE (ID >
          (SELECT MAX(id)
         FROM (SELECT TOP ҳ��С*ҳ�� id
                 FROM ��
                 ORDER BY id) AS T))
ORDER BY ID
 
 
 
 
 
  3.��ҳ��������(����SQL���α�洢���̷�ҳ)

create  procedure SqlPager
@sqlstr nvarchar(4000), --��ѯ�ַ���
@currentpage int, --��Nҳ
@pagesize int --ÿҳ����
as
set nocount on
declare @P1 int, --P1���α��id
 @rowcount int
exec sp_cursoropen @P1 output,@sqlstr,@scrollopt=1,@ccopt=1, @rowcount=@rowcount output
select ceiling(1.0*@rowcount/@pagesize) as ��ҳ��--,@rowcount as ������,@currentpage as ��ǰҳ 
set @currentpage=(@currentpage-1)*@pagesize+1
exec sp_cursorfetch @P1,16,@currentpage,@pagesize 
exec sp_cursorclose @P1
set nocount off



�����ķ��������û����������������ʱ��Ҳ�����÷�������������Ч�ʻ�͡�
�����Ż���ʱ�򣬼�����������������ѯЧ�ʻ���ߡ�

ͨ��SQL ��ѯ����������ʾ�Ƚϣ��ҵĽ�����:
��ҳ��������(����ID���ڶ��ٺ�SELECT TOP��ҳ��Ч����ߣ���Ҫƴ��SQL���
��ҳ����һ��(����Not In��SELECT TOP��ҳ)   Ч�ʴ�֮����Ҫƴ��SQL���
��ҳ��������(����SQL���α�洢���̷�ҳ)    Ч����������Ϊͨ�� 
