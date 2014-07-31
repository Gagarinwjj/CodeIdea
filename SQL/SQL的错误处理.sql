--参考:http://www.cnblogs.com/xugang/archive/2011/04/09/2010216.html

--看看下面这个测试,也许能让你对sql的错误处理有所了解(要知道,sql 的错误处理功能是很弱的)

--下面演示了SQL错误处理的脆弱性
--邹建

--演示1
--测试的存储过程1
CREATE PROC p1
AS 
    PRINT 12 / 0
    IF @@error <> 0 
    begin
        --PRINT '发生错误1'
        RAISERROR('发生错误1',12,12)
        --RETURN raiserror 后面的代码会继续执行，需要return来终止
     END 
    SELECT  *
    FROM    NEWID()
    IF @@error <> 0 
        PRINT '发生错误2'
go

--调用
EXEC p1
go

--删除测试
DROP PROC p1

/*--测试结果

服务器: 消息 8134，级别 16，状态 1，过程 p1，行 6
遇到被零除错误。
发生错误1
服务器: 消息 208，级别 16，状态 1，过程 p1，行 10
对象名 'newid' 无效。

--*/


/*--结论1:

错误1,不是严重的错误,所以SQL会执行下去
错误2,属于严重的错误,所以SQL没有执行下去,因为没有第二个print的结果

--*/



--演示2,存储过程嵌套调用中的错误

--测试的存储过程1
CREATE PROC p1
AS 
    PRINT 12 / 0
    IF @@error <> 0 
        PRINT '发生错误1'

    SELECT  *
    FROM    NEWID()
    IF @@error <> 0 
        PRINT '发生错误2'
go

--测试的存储过程2
CREATE PROC p2
AS 
    EXEC p1

    IF @@error <> 0 
        PRINT '调用 存储过程1 异常结束'
    ELSE 
        PRINT '调用 存储过程1 正常结束'
go

--调用
EXEC p2
go

--删除测试
DROP PROC p1,p2

/*--测试结果

服务器: 消息 8134，级别 16，状态 1，过程 p1，行 8
遇到被零除错误。
发生错误1
服务器: 消息 208，级别 16，状态 1，过程 p1，行 12
对象名 'newid' 无效。
调用 存储过程1 异常结束
--*/


/*--结论2:

被调用的存储过程发生严重错误时,调用它的存储过程可以捕获错误,并可以继续执行下去
--*/



--演示3,更严重的错误,无法用 set xact_abort on 来自动回滚事务
SET xact_abort ON
	--我们希望能自动回滚事务
BEGIN TRAN
CREATE TABLE #t ( id INT )
INSERT  #t
        SELECT  1
SELECT  *
FROM    NEWID()
COMMIT TRAN
go
SELECT  *
FROM    #t
ROLLBACK TRAN
/*--测试结果


（所影响的行数为 1 行）

服务器: 消息 208，级别 16，状态 1，行 5
对象名 'newid' 无效。

id          
----------- 
1

（所影响的行数为 1 行）
--*/

/*--结论3:

我们希望 set xact_abort on 可以实现出错时自动回滚事务
但结果令我们希望,出错时,事务并没有被回滚
因为我们查询到了#t的结果,而且最后的回滚语句也并没有报错
--*/