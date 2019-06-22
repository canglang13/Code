package server;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * �����̣߳�����ÿ���ͻ�
 * 
 * @author ������
 */

public class Channel implements Runnable {
	private String name;
	private boolean isRunning;
	private Socket client;
	private InetAddress ip;
	private DataInputStream dis;//�����
	private DataOutputStream dos;//������
	
	/**
	 * ���캯��
	 * @param client
	 */
	Channel(Socket client){
		this.client=client;
		ip=client.getInetAddress();
		try {
			dis=new DataInputStream(client.getInputStream());//��ȡ������
			name=dis.readUTF();//��ȡ�ͻ�������
		} catch (IOException e) {
			System.out.println("��������ȡ�������쳣");
			release();//�����쳣�ر�����
			
		}
		try {
			dos=new DataOutputStream(client.getOutputStream());//��ȡ�����
		} catch (IOException e) {
			System.out.println("��������ȡ������쳣");
			release();//�����쳣�ر�����
		}
		isRunning=true;
	}
	
	/**
	 * �߳����У�ѭ�����տͻ�������
	 */
	@Override
	public void run() {
			if(dos!=null) {
				send("ϵͳ��Ϣ����ӭ�㣡"+name);
				sendAll(name+" ������Ⱥ�ģ� ip��ַ: "+ip);
			}
			while (isRunning) {
				String msg=read();
				//System.out.println(msg);
				
				//�����ϢΪ�գ�������
				if(msg.equals("")||msg.trim().length()==0||msg==null) continue;
				
				//�ͻ����˳�ָ��
				if(msg.equals("bye")) {
					send("ϵͳ��Ϣ���ټ���");
					sendAll(name+"�˳���Ⱥ�ģ�");
					break;
				}
				
				//˽��ָ�����������ȡ�Է����ֺ���Ϣ�壬Ȼ��ת��
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
					//System.out.println("����name:"+name);
					//System.out.println("������Ϣ��"+str);
					
					sendTosb(name,str);//ת����Ϣ
					continue;
				}
				//ת����Ϣ
				sendAll(name+"˵�� "+msg);
			}
			release();//�ͷ���Դ
	}
	
	/**
	 * Ⱥ�Ĺ���
	 * �������û�ת����Ϣ
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
	 * ˽�Ĺ���
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
			c.send(this.name+" ����˵��"+msg);
		}
		else {
			this.send( "ϵͳ��Ϣ�����û������ڣ�");
		}
	}
	
	
	/**
	 * ������Ϣ
	 * @param msg ��Ҫ���͵���Ϣ
	 */
	public synchronized void send(String msg) {
		try {
			dos.writeUTF(msg);//�������д����Ϣ
			dos.flush();//���������
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.println("�Ͽ�����");
			release();//�����쳣�ͷ���Դ
		}
		
	}
	
	/**
	 * ������Ϣ
	 * @return ���ؽ��յ���Ϣ
	 */
	public String read() {
		String msg="";
		try {
			msg=dis.readUTF();//����������ȡ��Ϣ
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.println("�Ͽ�����");
			release();//�����쳣�ͷ���Դ
		}
		return msg;
	}
	
	
	
	/**
	 * �ͷ���Դ
	 */
	private void release() {
		CloseUtil.close(dis,dos,client);//�ر�����
		isRunning=false;
		Server.all.remove(this);//���б����Ƴ��Լ�
	}
	
	
	/**
	 * Getter��Setter����
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
