package client;
import java.io.IOException;
import java.net.*;





/**
 �ͻ���������
 *    
 * @author ������
 */
public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client=new Socket("127.0.0.1",8888);
		
		new Thread(new Send(client)).start();//���������߳�
		new Thread(new Receive(client)).start();//���������߳�
		
	}
}
