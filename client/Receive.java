package client;

import java.io.*;
import java.net.Socket;

/** 
�ͻ��˽�����Ϣ�߳�
 *
 * @author ������ 
 */
public class Receive implements Runnable{
	
	private Boolean isRunnable;
	private Socket client;
	private DataInputStream dis;
	
	/**
	 * ���캯��
	 * @param client
	 */
	Receive(Socket client){
		this.client=client;
		isRunnable=true;
		try {
			dis=new DataInputStream(client.getInputStream());//��ȡ������
		} catch (IOException e) {
			//System.out.println("�ͻ��˻�ȡ������ʧ��");
			release();//�����쳣�ͷ���Դ
		}
	}
	
	@Override
	public void run() {
		while (isRunnable) {
			String msg=receive();
			System.out.println(msg);
			if(msg.equals("ϵͳ��Ϣ���ټ���")) break;//����յ��˳���Ϣ���˳�
		}
		release();//�ͷ���Դ
	}
	
	/**
	 * ��ȡ��Ϣ
	 * @return
	 */
	public String receive() {
		String msg="";
		try {
			msg=dis.readUTF();//����������ȡ��Ϣ
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.println("�ͻ��˽���ʧ��");
			release();//�����쳣�ͷ���Դ
		}
		return msg;
	}
	
	/**
	 * �ͷ���Դ
	 */
	public void release() {
		isRunnable=false;
		CloseUtil.close(client,dis);
	}
}
