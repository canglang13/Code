package client;

import java.io.Closeable;
import java.io.IOException;
/**
 * �����࣬�ر�����
 * @author Administrator
 *
 */

public class CloseUtil {
	public static void  close(Closeable... targets) {
		for(Closeable target:targets) {
			if(target!=null) {
				try {
					target.close();
				} catch (IOException e) {
					System.out.println("���ӹرմ��󡣡���");
				}
			}
		}
	}
}
