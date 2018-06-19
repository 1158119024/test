package com.exercise.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import org.junit.Test;

/**
 * 阻塞io: 客服端-服务端
 * @author root
 *
 */
public class Day02_NIO_BlockingNIO {

	@Test
	public void client() throws IOException{
		//1. 获取客户端网络通道
		SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
		
		//2. 指定文件
		FileChannel inChannel = FileChannel.open(Paths.get("F:/1.txt"), StandardOpenOption.READ);
		
		//3. 建立缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		//4. 读出本地文件，写入网络通道
		while(inChannel.read(buf) != -1){
			buf.flip();
			channel.write(buf);
			buf.clear();
		}
		
		//5.通知服务端(客户端已经写入就绪)
		channel.shutdownOutput();
		
		//6.接收服务端返回信息
		ByteBuffer dst = ByteBuffer.allocate(1024);
		int len = -1;
		while( (len = channel.read(dst)) != -1 ){
			dst.flip();
			System.out.println(new String(dst.array(), 0, len));
			dst.clear();
		}
		
		//7. 关闭
		inChannel.close();
		channel.close();
		
	}
	
	@Test
	public void server() throws IOException{
		//1. 开启服务端网络通道
		ServerSocketChannel channel = ServerSocketChannel.open();
		
		//2. 获取本地通道
		FileChannel outChannel = FileChannel.open(Paths.get("F:/2.txt"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
		
		//3. 绑定端口连接，监听从这个端口发来的信息
		channel.bind(new InetSocketAddress(8080));
		
		//4. 获取客户端通道
		SocketChannel clientChannel = channel.accept();
		
		//5. 建立缓冲区
		ByteBuffer dst = ByteBuffer.allocate(1024);
		
		//6. 读出数据，保存
		while(clientChannel.read(dst) != -1){
			dst.flip();
			outChannel.write(dst);
			dst.clear();
		}
		
		//7.通知客户端
		dst.clear();
		dst.put("服务端已成功接受信息".getBytes());
		dst.flip();//发送前切换下模式
		clientChannel.write(dst);
		
		//7. 关闭
		channel.close();
		outChannel.close();
		clientChannel.close();
		
	}
	
	
	@Test
	public void server2() throws IOException{
		ServerSocketChannel channel = ServerSocketChannel.open();
		
		channel.bind(new InetSocketAddress(8080));
		
		int i = 10;
		while(true){
			SocketChannel clientChannel = channel.accept();
			
			if(clientChannel != null){
				i++;
				FileChannel outChannel = FileChannel.open(Paths.get("F:/"+i+".txt"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
				ByteBuffer dst = ByteBuffer.allocate(1024);
				
				while(clientChannel.read(dst) != -1){
					dst.flip();
					outChannel.write(dst);
					dst.clear();
				}
				
				dst.clear();
				dst.put("服务端已成功接受信息".getBytes());
				dst.flip();//发送前切换下模式
				clientChannel.write(dst);
				outChannel.close();
				clientChannel.close();
			}
		}
		
		
	}
}
