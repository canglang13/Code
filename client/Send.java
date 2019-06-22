package client;

import java.io.*;
import java.net.Socket;

/**
�ͻ��˷�����Ϣ�߳�
 * 
 * @author ������
 */
public class Send implements Runnable{
	
	private Boolean isRunnable;
	private Socket client;
	private BufferedReader buf;
	private DataOutputStream dos;
	
	/**
	 * ���캯��
	 * @param client
	 */
	Send(Socket client){
		this.client=client;
		isRunnable=true;
		buf=new BufferedReader(new InputStreamReader(System.in));
		try {
			dos=new DataOutputStream(client.getOutputStream());//��ȡ�����
			System.out.println("��ӭʹ�ã�������������֣�");
			String name=buf.readLine();//�����û���
			dos.writeUTF(name);//������������û���
			dos.flush();//���������
		} catch (IOException e) {
			System.out.println("�ͻ��˻�ȡ�����ʧ��");
			release();//�ͷ���Դ
		}
	}
	
	@Override
	public void run() {
		while(isRunnable) {
			String msg=getMsg();//ѭ����ȡ�û�����
			send(msg);//�������������Ϣ
			if(msg.equals("bye")) break;
		}
		release();//�ͷ���Դ
	}
	
	public String getMsg() {
		String msg="";
		try {
			msg=buf.readLine();
		} catch (IOException e) {
			//System.out.println("�ͻ��˶�ȡ����ʧ��");
			release();
		}
		return msg;
	}
	
	/**
	 * ������Ϣ
	 * @param msg
	 */
	public void send(String msg) {
		try {
			dos.writeUTF(msg);//�������д��Ϣ
			dos.flush();//���������
		} catch (IOException e) {
			//System.out.println("�ͻ��˷�����Ϣʧ��");
			release();//�����쳣�ͷ���Դ
		}
	}
	
	/**
	 * �ͷ���Դ
	 */
	public void release() {
		isRunnable=false;
		CloseUtil.close(client,dos);
	}
	
}
