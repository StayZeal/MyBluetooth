package example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;


import example.util.Log;
import example.util.TextUtils;

public class Test {

	private ServerSocket mServerSocket;
	private int mPort = 80;

	private void tet() {


		try {
			mServerSocket = new ServerSocket(mPort);
			Socket socket = mServerSocket.accept();
			InetAddress addr = socket.getInetAddress();
			Log.i("�ͻ���ip: " + addr.getHostAddress() + ":" + mPort + "  " + System.currentTimeMillis() / 1000);

			handle(socket);
			socket.close();
		} catch (SocketException e) {
			// The server was stopped; ignore.
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ignore) {

			ignore.printStackTrace();
		}
	}

	public void handle(Socket socket) {

		BufferedReader reader = null;
		PrintStream output = null;
		try {
			String route = null;
			StringBuffer request = new StringBuffer();

			// Read HTTP headers and parse out the route.
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			int postDataI = -1;
			Log.i("Header:");
			while (!TextUtils.isEmpty(line = reader.readLine())) {
				if (line.startsWith("GET /")) {
					int start = line.indexOf('/') + 1;
					int end = line.indexOf(' ', start);
					route = line.substring(start, end);

				    Log.i(route);
					return;
				}

				if (line.indexOf("Content-Length:") > -1) {
					postDataI = new Integer(line.substring(line.indexOf("Content-Length:") + 16, line.length()))
							.intValue();
				}
				Log.i(line);

			}

			String postData = "";
			// ��ȡpost����
			if (postDataI > 0) {
				char[] charArray = new char[postDataI];
				reader.read(charArray, 0, postDataI);
				postData = new String(charArray);
			}

			// �滻�����ַ�
			postData = postData.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
			// utf-8����
			postData = URLDecoder.decode(postData, "utf-8");

			Log.i("route:" + route);
			output = new PrintStream(socket.getOutputStream());

			// Send out the content.
			output.println("HTTP/1.1 200 OK");
			output.println("Content-Type: " + "application/octet-stream");

			String responseStr = "";
			byte[] bytes = responseStr.getBytes();
			output.println("Content-Length: " + bytes.length);
			output.println();
			output.write(bytes);
			output.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != output) {
					output.close();
				}
				if (null != reader) {
					reader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
