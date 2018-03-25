package example.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {

		 ClientServer clientServer = new ClientServer(80);
		 clientServer.start();

//		test();
	}

	private static void test() {
		// TODO Auto-generated method stub

		try {
			ServerSocket server = new ServerSocket(179);
			while (true) {
				System.out.println("Connection¡­¡­");
				Socket socket = server.accept();
				InetAddress addr = socket.getInetAddress();
				System.out.println("Connection from==>" + addr.getHostName() + "/t" + addr.getHostAddress());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
