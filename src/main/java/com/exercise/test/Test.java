package com.exercise.test;

public class Test {

	public static void fun(Integer i){
		i = new Integer(2);
	}
	
	public static void fun1(String str){
		str = "str";
	}
	
	public static void fun2(StringBuffer sb){
		sb.append("4");
	}
	
	public static void main(String[] args) {
		Integer i = new Integer(0);
		fun(i);
		System.out.println("1."+i);
		
		String str = "";
		fun1(str);
		System.out.println("2."+str);

		StringBuffer sb = new StringBuffer("");
		fun2(sb);
		System.out.println("3."+sb);
	}
	
	
}
