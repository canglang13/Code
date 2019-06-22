package server;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 服务器端主程序
 * 
 * @author 赖晨朗
 */
public class Server {
	public static CopyOnWriteArrayList<Channel> all=new CopyOnWriteArrayList<Channel>();
	
	public static void main(String[] args) throws IOException {
		System.out.println("--------服务器--------");
		@SuppressWarnings("resource")
		ServerSocket server=new ServerSocket(8888);
		
		while (true) {
			
			Socket client=server.accept();
			Channel c=new Channel(client);//创建客户线程
			all.add(c);//将线程加入容器
			new Thread(c).start();
			
		}
		
	}
}
