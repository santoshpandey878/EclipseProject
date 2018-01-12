package com.javabrains.messenger.resources;

public class test {

	public static void main(String[] args) {
	Employee emp = new Employee();
	String res = JsonUtil.convertJavaToJson(emp);
	System.out.println(res);
	}

}
