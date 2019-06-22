package server;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * 处理线程，代表每个客户
 * 
 * @author 赖晨朗
 */

public class Channel implements Runnable {
	private String name;
	private boolean isRunning;
	private Socket client;
	private InetAddress ip;
	private DataInputStream dis;//输出流
	private DataOutputStream dos;//输入流
	
	/**
	 * 构造函数
	 * @param client
	 */
	Channel(Socket client){
		this.client=client;
		ip=client.getInetAddress();
		try {
			dis=new DataInputStream(client.getInputStream());//获取输入流
			name=dis.readUTF();//获取客户端名字
		} catch (IOException e) {
			System.out.println("服务器获取输入流异常");
			release();//出现异常关闭链接
			
		}
		try {
			dos=new DataOutputStream(client.getOutputStream());//获取输出流
		} catch (IOException e) {
			System.out.println("服务器获取输出流异常");
			release();//出现异常关闭链接
		}
		isRunning=true;
	}
	
	/**
	 * 线程运行，循环接收客户端数据
	 */
	@Override
	public void run() {
			if(dos!=null) {
				send("系统消息：欢迎你！"+name);
				sendAll(name+" 加入了群聊！ ip地址: "+ip);
			}
			while (isRunning) {
				String msg=read();
				//System.out.println(msg);
				
				//如果消息为空，不发送
				if(msg.equals("")||msg.trim().length()==0||msg==null) continue;
				
				//客户端退出指令
				if(msg.equals("bye")) {
					send("系统消息：再见！");
					sendAll(name+"退出了群聊！");
					break;
				}
				
				//私聊指令，从数据流获取对方名字和消息体，然后转发
				if(Pattern.matches("@.+", msg)) {
					String msgs[]=msg.split(" ");
					String str="";
					for(int i=1;i<msgs.length;i++) {
						if(i==1) str+=msgs[i];
						else {
							str=str+" "+msgs[i];
						}
					}
					String name=msgs[0].substring(1);
					//System.out.println("传入name:"+name);
					//System.out.println("传入消息："+str);
					
					sendTosb(name,str);//转发消息
					continue;
				}
				//转发消息
				sendAll(name+"说： "+msg);
			}
			release();//释放资源
	}
	
	/**
	 * 群聊功能
	 * 向所有用户转发消息
	 * @param msg
	 */
	public static void sendAll(String msg) {
		Iterator<Channel> it=Server.all.iterator(); 
		while(it.hasNext()) {
			Channel next= (Channel) it.next();
			next.send(msg);
		}
	}

	/**
	 * 私聊功能
	 */
	public void sendTosb(String name,String msg) {
		Iterator<Channel> it=Server.all.iterator(); 
		Boolean flag=false;
		Channel c=null;
		while(it.hasNext()) {
			c=it.next();
			//System.out.println(c.getName());
			if(c.getName().equals(name)) {
				flag=true;
				break;
			}
		}
		if(flag) {
			c.send(this.name+" 对你说："+msg);
		}
		else {
			this.send( "系统消息：该用户不存在！");
		}
	}
	
	
	/**
	 * 发送消息
	 * @param msg 需要发送的消息
	 */
	public synchronized void send(String msg) {
		try {
			dos.writeUTF(msg);//向输出流写入消息
			dos.flush();//清除缓冲区
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.println("断开连接");
			release();//出现异常释放资源
		}
		
	}
	
	/**
	 * 接收消息
	 * @return 返回接收的消息
	 */
	public String read() {
		String msg="";
		try {
			msg=dis.readUTF();//从输入流读取消息
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.println("断开连接");
			release();//出现异常释放资源
		}
		return msg;
	}
	
	
	
	/**
	 * 释放资源
	 */
	private void release() {
		CloseUtil.close(dis,dos,client);//关闭连接
		isRunning=false;
		Server.all.remove(this);//在列表里移除自己
	}
	
	
	/**
	 * Getter、Setter函数
	 * 
	 */
	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
