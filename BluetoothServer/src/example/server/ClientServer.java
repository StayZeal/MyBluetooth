package example.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import example.util.Log;

public class ClientServer implements Runnable {

	private static final String TAG = "ClientServer";

	private final int mPort;
	// private final RequestHandler mRequestHandler;
	private final UploadHandler mUploadHandler;
	private boolean mIsRunning;
	private ServerSocket mServerSocket;

	public ClientServer(int port) {
		// mRequestHandler = new RequestHandler();
		mUploadHandler = new UploadHandler();
		mPort = port;
	}

	public void start() {
		mIsRunning = true;
		new Thread(this).start();
	}

	public void stop() {
		try {
			mIsRunning = false;
			if (null != mServerSocket) {
				mServerSocket.close();
				mServerSocket = null;
			}
		} catch (Exception e) {
			Log.e(TAG, "Error closing the server socket.", e);
		}
	}

	@Override
	public void run() {
		Log.i("线程启动");
		try {
			mServerSocket = new ServerSocket(mPort);
			Log.i("服务器已经启动在: ");
			while (mIsRunning) {
				Socket socket = mServerSocket.accept();
				InetAddress addr = socket.getInetAddress();
				Log.i("客户端ip: " + addr.getHostAddress() + ":" + mPort+"  "+System.currentTimeMillis()/1000);
//				Log.i("服务器已经启动在: " + addr.getHostName() + ":" + mPort);
				

				// mRequestHandler.handle(socket);
				mUploadHandler.handle(socket);
				socket.close();
			}
		} catch (SocketException e) {
			// The server was stopped; ignore.
		} catch (IOException e) {
			Log.e(TAG, "Web server error.", e);
		} catch (Exception ignore) {
			Log.e(TAG, "Exception.", ignore);
		}
	}

	public boolean isRunning() {
		return mIsRunning;
	}
}
