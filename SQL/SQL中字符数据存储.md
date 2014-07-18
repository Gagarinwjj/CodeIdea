SQL中的数据存储
===
- ###`char` 定长，非unicode字符，小于补空，大于截取
		DECLARE @name CHAR(10)
		SELECT @name='我a'
		SELECT LEN(@name)--2
		SELECT DATALENGTH(@name)--10
		
- ###`nchar` 
		```sql
		DECLARE @name NCHAR(10)
		SELECT @name='我a'
		SELECT LEN(@name)--2
		SELECT DATALENGTH(@name)--20
		```
- varchar
- nvarchar
- text
- ntext

