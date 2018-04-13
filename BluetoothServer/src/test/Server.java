package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket ssk = new ServerSocket(9000);
		// Socket sk;
		// 等待连接
		// 实现多个连接
		while (true) {
			Socket sk = ssk.accept();// 包含数据
			System.out.println("连接成功");
			InputStream is = sk.getInputStream();

			// 这是我使用read方法读取 是没问题的
			// byte [] b = new byte[1024];
			// int x = is.read(b, 0, b.length);
			// String str3 = new String(b,0,x);
			// System.out.println(str3);

			BufferedReader buff = new BufferedReader(new InputStreamReader(is));
			String str = null;
			while ((str = buff.readLine()) != null) {
				System.out.println("---");
				System.out.println(str);
			}
			System.out.println("是否发送数据回去");
			Scanner sc = new Scanner(System.in);
			String str1 = sc.next();
			if ("y".equals(str1)) {
				OutputStream os = sk.getOutputStream();
				byte[] byt = "javahello".getBytes();
				os.write(byt);
				os.close();
			} else {
				break;
			}
			is.close();
			sk.close();
		}
		System.out.println("停止服务器");
	}
}
