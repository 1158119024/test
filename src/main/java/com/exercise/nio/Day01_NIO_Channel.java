package com.exercise.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/**
 * 通道
 * @author root
 *
 */
public class Day01_NIO_Channel {

	
	/**
	 * 方式一：利用FileInputStream/FileOutputStream里的getChannel()获取通道
	 * @throws IOException
	 */
	@Test
	public void test() throws IOException{
		
		FileInputStream fis = new FileInputStream("1.txt");
		FileOutputStream fos = new FileOutputStream("2.txt");
		
		//获取通道
		FileChannel inChannel = fis.getChannel();
		FileChannel outChannel = fos.getChannel();
		
		//创建缓冲区
		ByteBuffer dst = ByteBuffer.allocate(1024);
		
		int len;
		//读取数据
		while((len = inChannel.read(dst)) != -1){
			//切换读模式
			dst.flip();
			System.out.println(new String(dst.array(), 0, len));
			outChannel.write(dst);
			//清空缓冲区
			dst.clear();
		}
		
		inChannel.close();
		outChannel.close();
		fis.close();
		fos.close();
	}
	
	/**
	 * 方式二: FileChannel.open()获取通道
	 * @throws IOException 
	 */
	@Test
	public void test2() throws IOException{
		long startTime = System.currentTimeMillis();
		FileChannel inChannel = FileChannel.open(Paths.get("F:/nio.zip"),StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("F:/nio2.zip"), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ);
		
//		ByteBuffer buf = ByteBuffer.allocate(1024);//直接缓冲区
		ByteBuffer buf = ByteBuffer.allocateDirect(1024);//非直接缓冲区
		
		while(inChannel.read(buf) != -1){
			buf.flip();
			outChannel.write(buf);
			buf.clear();
		}
		
		inChannel.close();
		outChannel.close();
		
		System.out.println(System.currentTimeMillis() - startTime);
	}
	
	/**
	 * 方式二: FileChannel.open()
	 * MappedByteBuffer: 内存映射文件(直接缓冲区)
	 * @throws IOException 
	 * 
	 */
	@Test
	public void test3() throws IOException{
		long startTime = System.currentTimeMillis();
		FileChannel inChannel = FileChannel.open(Paths.get("F:/nio.zip"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("F:/nio3.zip"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
		
		//内存映射文件
		MappedByteBuffer inMappedBuf = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outMappedBuf = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
		
		outMappedBuf.put(inMappedBuf);
		
		inChannel.close();
		outChannel.close();
		System.out.println(System.currentTimeMillis() - startTime);
	}
	
	
}
