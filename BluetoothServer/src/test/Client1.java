package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Client1 {
	public static Socket sk;

	public static void main(String[] args) throws IOException {
		Client1 c = new Client1();
		sk = new Socket("127.0.0.1", 9000);
		OutputStream os = sk.getOutputStream();
		byte[] by = "����\n".getBytes(); // ����ַ�������\n readline������һֱ���� �� //read�����򲻻�������������
		os.write(by, 0, by.length);// ��������д��ȥ֮�����������ܵ��߳�
		// ����һ�����յ��߳�
		TThread td = c.new TThread();
		td.start();
	}

	class TThread extends Thread {
		// ���ܵ����Ǹ������
		@Override
		public void run() {
			try {
				while (true) {
					InputStream is = sk.getInputStream();
					BufferedReader buff = new BufferedReader(new InputStreamReader(is));
					String str = null;
					while ((str = buff.readLine()) != null) {
						System.out.println(str);
					}
					// is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
