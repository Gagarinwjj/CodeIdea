--http://www.cnblogs.com/TLLi/archive/2012/07/15/2592042.html
--http://q.cnblogs.com/q/40346/
--http://www.cnblogs.com/nevernet/archive/2012/08/23/2652426.html

--2008����־����ֻ���ڼ�ģʽ�½���

--step1:����Ϊ��ģʽ
USE [master]

   GO
ALTER DATABASE InfoWebDB SET RECOVERY SIMPLE WITH NO_WAIT

   GO
ALTER DATABASE InfoWebDB SET RECOVERY SIMPLE
   --��ģʽ

   GO

--step2:��������
USE InfoWebDB 

   GO

DBCC SHRINKFILE (N'InfoWebDB_Log' , 11, TRUNCATEONLY)

   GO


--step3:��ԭΪ����ģʽ
USE [master]

   GO

ALTER DATABASE InfoWebDB SET RECOVERY FULL WITH NO_WAIT

   GO

ALTER DATABASE InfoWebDB SET RECOVERY FULL --��ԭΪ��ȫģʽ

   GO

 

--�ŵ㣺�������־���������ĵ�ʱ��̣�90GB����־�ڷ������Ҽ��������ϣ�����֮��������ȫ�����ڷ�����

--������ɡ�

--ȱ�㣺�����˶�����ò�Ҫ����ʹ�ã���Ϊ�������л����ϵͳ��Ƭ����ͨ״̬��LOG��DIFF�ı��ݼ��ɽض���־��

--�����ʹ�õ�ǡ����������ϵͳ����־�ļ��쳣������߱���LOGʱ��̫������Ӱ�������������ʹ�á�
