package client;
import java.io.IOException;
import java.net.*;





/**
 客户端主程序
 *    
 * @author 赖晨朗
 */
public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client=new Socket("127.0.0.1",8888);
		
		new Thread(new Send(client)).start();//启动发送线程
		new Thread(new Receive(client)).start();//启动接收线程
		
	}
}
