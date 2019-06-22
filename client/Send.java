package client;

import java.io.*;
import java.net.Socket;

/**
客户端发送消息线程
 * 
 * @author 赖晨朗
 */
public class Send implements Runnable{
	
	private Boolean isRunnable;
	private Socket client;
	private BufferedReader buf;
	private DataOutputStream dos;
	
	/**
	 * 构造函数
	 * @param client
	 */
	Send(Socket client){
		this.client=client;
		isRunnable=true;
		buf=new BufferedReader(new InputStreamReader(System.in));
		try {
			dos=new DataOutputStream(client.getOutputStream());//获取输出流
			System.out.println("欢迎使用！请输入你的名字：");
			String name=buf.readLine();//输入用户名
			dos.writeUTF(name);//向服务器发送用户名
			dos.flush();//清除缓冲区
		} catch (IOException e) {
			System.out.println("客户端获取输出流失败");
			release();//释放资源
		}
	}
	
	@Override
	public void run() {
		while(isRunnable) {
			String msg=getMsg();//循环读取用户输入
			send(msg);//向服务器发送消息
			if(msg.equals("bye")) break;
		}
		release();//释放资源
	}
	
	public String getMsg() {
		String msg="";
		try {
			msg=buf.readLine();
		} catch (IOException e) {
			//System.out.println("客户端读取输入失败");
			release();
		}
		return msg;
	}
	
	/**
	 * 发送消息
	 * @param msg
	 */
	public void send(String msg) {
		try {
			dos.writeUTF(msg);//向输出流写消息
			dos.flush();//清除缓冲区
		} catch (IOException e) {
			//System.out.println("客户端发送消息失败");
			release();//出现异常释放资源
		}
	}
	
	/**
	 * 释放资源
	 */
	public void release() {
		isRunnable=false;
		CloseUtil.close(client,dos);
	}
	
}
