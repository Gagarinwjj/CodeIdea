--http://www.cnblogs.com/TLLi/archive/2012/07/15/2592042.html
--http://q.cnblogs.com/q/40346/
--http://www.cnblogs.com/nevernet/archive/2012/08/23/2652426.html

--2008的日志清理只能在简单模式下进行

--step1:设置为简单模式
USE [master]

   GO
ALTER DATABASE InfoWebDB SET RECOVERY SIMPLE WITH NO_WAIT

   GO
ALTER DATABASE InfoWebDB SET RECOVERY SIMPLE
   --简单模式

   GO

--step2:收缩数据
USE InfoWebDB 

   GO

DBCC SHRINKFILE (N'InfoWebDB_Log' , 11, TRUNCATEONLY)

   GO


--step3:还原为完整模式
USE [master]

   GO

ALTER DATABASE InfoWebDB SET RECOVERY FULL WITH NO_WAIT

   GO

ALTER DATABASE InfoWebDB SET RECOVERY FULL --还原为完全模式

   GO

 

--优点：此清除日志所运行消耗的时间短，90GB的日志在分钟左右即可清除完毕，做完之后做个完全备份在分钟内

--即可完成。

--缺点：不过此动作最好不要经常使用，因为它的运行会带来系统碎片。普通状态下LOG和DIFF的备份即可截断日志。

--此语句使用的恰当环境：当系统的日志文件异常增大或者备份LOG时间太长可能影响生产的情况下使用。
