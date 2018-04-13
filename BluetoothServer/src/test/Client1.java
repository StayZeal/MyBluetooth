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
		byte[] by = "下雨\n".getBytes(); // 如果字符串不加\n readline方法将一直阻塞 而 //read方法则不会出现阻塞的情况
		os.write(by, 0, by.length);// 当把数据写过去之后，立马开启接受的线程
		// 开启一个接收的线程
		TThread td = c.new TThread();
		td.start();
	}

	class TThread extends Thread {
		// 接受的是那个对象的
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
