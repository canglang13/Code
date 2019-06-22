package client;

import java.io.*;
import java.net.Socket;

/** 
客户端接收消息线程
 *
 * @author 赖晨朗 
 */
public class Receive implements Runnable{
	
	private Boolean isRunnable;
	private Socket client;
	private DataInputStream dis;
	
	/**
	 * 构造函数
	 * @param client
	 */
	Receive(Socket client){
		this.client=client;
		isRunnable=true;
		try {
			dis=new DataInputStream(client.getInputStream());//获取输入流
		} catch (IOException e) {
			//System.out.println("客户端获取输入流失败");
			release();//出现异常释放资源
		}
	}
	
	@Override
	public void run() {
		while (isRunnable) {
			String msg=receive();
			System.out.println(msg);
			if(msg.equals("系统消息：再见！")) break;//如果收到退出消息，退出
		}
		release();//释放资源
	}
	
	/**
	 * 获取消息
	 * @return
	 */
	public String receive() {
		String msg="";
		try {
			msg=dis.readUTF();//从输入流读取消息
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.println("客户端接收失败");
			release();//出现异常释放资源
		}
		return msg;
	}
	
	/**
	 * 释放资源
	 */
	public void release() {
		isRunnable=false;
		CloseUtil.close(client,dis);
	}
}
