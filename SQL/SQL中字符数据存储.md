SQL中字符数据存储
===
- ###`char(n)` 定长，`非Unicode字符`，小于补空，大于截取
	```sql
	DECLARE @name CHAR(10)
	SELECT @name='我a'
	SELECT LEN(@name)--2
	SELECT DATALENGTH(@name)--10
	```
	
- ###`nchar(n)` 定长，`Unicode字符` n范围：[1,4000]，小于补空，大于截取
	```sql
	DECLARE @name NCHAR(10)
	SELECT @name='我a'
	SELECT LEN(@name)--2
	SELECT DATALENGTH(@name)--20
	```

- ###`varchar(n)` 不定长，`非Unicode字符` n范围：[1,8000],中文2个字节，英文1个字节

	```sql
	DECLARE @name VARCHAR(10)
	SELECT @name='我a'
	SELECT LEN(@name)--2
	SELECT DATALENGTH(@name)--3
	```

- ###`nvarchar` 不定长，`Unicode字符` n范围：[1,4000],中英文均为2字节，所以存储大小是所输入字符个数的两倍

	```sql
	DECLARE @name NCHAR(10)
	SELECT @name='我a'
	SELECT LEN(@name)--2
	SELECT DATALENGTH(@name)--4
		```

- ###`text` 不定长，`非Unicode字符`,最大长度为2^31-1(2,147,483,647)个字符

	```sql
	--对于局部变量，text、ntext 和 image 数据类型无效。
	```

- ###`ntext` 不定长，`Unicode字符`

	```sql
	--对于局部变量，text、ntext 和 image 数据类型无效。
	```

