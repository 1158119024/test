package com.exercise.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

/**
 * 非阻塞试NIO Selector
 * @author root
 *
 */
public class Day02_NIO_NotBlockingNIO {

	@Test
	public void client() throws IOException{
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
		//配置非阻塞模式
		sChannel.configureBlocking(false);
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put(new Date().toString().getBytes());
		
		buf.flip();
		sChannel.write(buf);
		//sChannel.read(buf);
		buf.clear();
		
		sChannel.shutdownOutput();
		sChannel.close();
	}
	
	@Test
	public void server() throws IOException{
		//1. 获取服务端网络通道
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		
		//2. 配置非阻塞模式
		ssChannel.configureBlocking(false);
		
		//3. 绑定端口
		ssChannel.bind(new InetSocketAddress(8080));
		
		//4. 开启选择器
		Selector selector = Selector.open();
		
		//5. 将通道注册选择器,OP_ACCEPT: 监听接收事件
		ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		//6. 轮训选择器上的"准备就绪"的事件
		while(true){
			
			if(selector.select() > 0){
				//7. 获取选择器上的"准备就绪"的事件
				Set<SelectionKey> kSet = selector.selectedKeys();
				
				for(SelectionKey set : kSet){
					//9. 判断事件类型
					if( set.isReadable() ){//读准备就绪
						System.out.println("读准备就绪...");
						//13. 获取当前选择器上“读就绪”状态的通道
						SocketChannel sChannel = (SocketChannel) set.channel();
						
						//14. 读取数据
						ByteBuffer buf = ByteBuffer.allocate(1024);
						
						int len = 0;
						while((len = sChannel.read(buf)) > 0 ){
							buf.flip();
							System.out.println(new String(buf.array(), 0, len));
							buf.clear();
						}
						//
						set.interestOps(SelectionKey.OP_ACCEPT);
						
					}else if( set.isWritable() ){//写准备就绪
						SocketChannel sChannel = ssChannel.accept();
						System.out.println("写准备就绪...");
					}else if( set.isAcceptable() ){//接收准备就绪
						System.out.println("接收准备就绪...");
						//10. 若“接收就绪”，获取客户端连接
						SocketChannel sChannel = ssChannel.accept();
						
						//11. 切换非阻塞模式
						sChannel.configureBlocking(false);
						
						//12. 将该通道注册到选择器上(为对应的通道配置后续操作)
						sChannel.register(selector, SelectionKey.OP_READ);
					}else if( set.isConnectable() ){//连接准备就绪
						SocketChannel sChannel = ssChannel.accept();
						System.out.println("连接准备就绪...");
					}
				}
				kSet.clear();
			}
			
		}
		
	}
	
	
	
}
