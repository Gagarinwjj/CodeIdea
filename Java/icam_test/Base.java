package wjj;

/**
 * Project Name:  Test
 * File Name:     Base.java
 * Package Name:  
 * @Date:         2014年10月24日
 * Copyright (c)  2014, wulian All Rights Reserved.
 */

/**
 * @ClassName: Base
 * @Function:
 * @Date: 2014年10月24日
 * @author Wangjj
 * @email wangjj@wuliangroup.cn
 */
public class Base {
	// public default protected 调用子类的东西
	// private 调用父类自己的东西
	public void test1() {
		System.out.println("父类this " + this);
		System.out.println("父类Test1");
	}

	public void test2() {
		System.out.println("父类Test2");
	}

	public void test3() {
		test1();// public child调用的是子类重写的test1
		test2();// public child调用的是子类重写的test2
	}
}
