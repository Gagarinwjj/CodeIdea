package wjj;

/**
 * Project Name:  Test
 * File Name:     Child.java
 * Package Name:  
 * @Date:         2014年10月24日
 * Copyright (c)  2014, wulian All Rights Reserved.
 */

/**
 * @ClassName: Child
 * @Function:
 * @Date: 2014年10月24日
 * @author Wangjj
 * @email wangjj@wuliangroup.cn
 */
public class Child extends Base {
	// 标记不打也行
	public void test1() {
		super.test1();
		System.out.println("子类this " + this);
		System.out.println("子类Test1");
	}

	public void test2() {
		// super.test2();
		System.out.println("子类Test2");
	}

	public static void main(String[] args) {
		Child child = new Child();
		child.test3();

	}
}
