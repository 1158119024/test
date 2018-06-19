package com.exercise.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test2 {

	//43112119900101875X
	//431103199706173314
	//431121********875X
	
	public static void main(String[] args) throws Exception {
		
		//int y = 1990;//年
//		int m = 1;//月
//		int d = 1;//日
		String card = "";
		String month = "";
		String day = "";
		for( int y = 1972; y<1973; y++ ){
			System.out.println(y+"年开始");
			for( int m = 9; m<12; m++ ){
				month = "";
				if( m < 10 ){
					month = "0" + m;
				}else{
					month = m + "";
				}
				for( int d = 1; d<31; d++ ){
					if( d < 10 ){
						day = "0" + d;
					}else{
						day = d + "";
					}
					card = "431121" + y + month + day +"875X";
					day = "";
					check(card);
					card = "";
				}
				
			}
			System.out.println(y+"年结束");
		}
		
		//check("43112119900101875X");
	}
	
	
	public static void check(String card) throws Exception {
		
		String strURL = "http://api.k780.com:88/?app=idcard.get&idcard="+card+"&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";  
		URL url = new URL(strURL);  
		HttpURLConnection httpConn = (HttpURLConnection)  
		        url.openConnection();  
		httpConn.setRequestMethod("GET");  
		httpConn.connect(); 
		  
		BufferedReader reader = new BufferedReader(new InputStreamReader(  
		        httpConn.getInputStream(),"utf-8"));  
		String line;  
		StringBuffer buffer = new StringBuffer();  
		while ((line = reader.readLine()) != null) {  
		    buffer.append(line);  
		}  
		reader.close();  
		httpConn.disconnect();  
		
		if(!buffer.toString().contains("身份证号码输入不正确")){
			System.out.println(card);
		}
//		System.out.println(buffer.toString());
//		int indexOf = buffer.toString().indexOf("发&nbsp;证&nbsp;地：</td><td class=\"tdc2\">");
//		String str = buffer.substring(indexOf, indexOf+7);
	}
	
	
	
}
