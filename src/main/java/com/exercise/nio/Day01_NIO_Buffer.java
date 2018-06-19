package com.exercise.nio;

import java.nio.ByteBuffer;

import org.junit.Test;

/**
 * 缓冲区
 * @author root
 *
 */
public class Day01_NIO_Buffer {

	@Test
	public void test(){
		//创建缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		buf.put("abcdefg".getBytes());
		
		System.out.println("当前位置:---->"+buf.position());
		System.out.println("界限:---->"+buf.limit());
		System.out.println("总容量:---->"+buf.capacity());
		
		//切换读模式
		buf.flip();
		
		byte[] dst = new byte[buf.limit()];
		buf.get(dst);
		System.out.println(new String(dst, 0, dst.length));
		buf.flip();//与rewind()效果一致
		buf.get(dst);
		System.out.println(new String(dst, 0, dst.length));
		System.out.println("当前位置:---->"+buf.position());
		buf.rewind();
		System.out.println("当前位置:---->"+buf.position());
		
	}
}
