package server;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ��������������
 * 
 * @author ������
 */
public class Server {
	public static CopyOnWriteArrayList<Channel> all=new CopyOnWriteArrayList<Channel>();
	
	public static void main(String[] args) throws IOException {
		System.out.println("--------������--------");
		@SuppressWarnings("resource")
		ServerSocket server=new ServerSocket(8888);
		
		while (true) {
			
			Socket client=server.accept();
			Channel c=new Channel(client);//�����ͻ��߳�
			all.add(c);//���̼߳�������
			new Thread(c).start();
			
		}
		
	}
}
