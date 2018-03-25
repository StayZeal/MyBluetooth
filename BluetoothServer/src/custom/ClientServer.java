//
//package custom;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketException;
//import java.util.HashMap;
//
//public class ClientServer implements Runnable {
//
//	private static final String TAG = "ClientServer";
//
//	private final int mPort;
//	private final RequestHandler mRequestHandler;
//	private boolean mIsRunning;
//	private ServerSocket mServerSocket;
//
//	public ClientServer(int port) {
//		mRequestHandler = new RequestHandler();
//		mPort = port;
//	}
//
//	public void start() {
//		mIsRunning = true;
//		new Thread(this).start();
//	}
//
//	public void stop() {
//		try {
//			mIsRunning = false;
//			if (null != mServerSocket) {
//				mServerSocket.close();
//				mServerSocket = null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void run() {
//		try {
//			mServerSocket = new ServerSocket(mPort);
//			while (mIsRunning) {
//				Socket socket = mServerSocket.accept();
//				mRequestHandler.handle(socket);
//				socket.close();
//			}
//		} catch (SocketException e) {
//			// The server was stopped; ignore.
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception ignore) {
//			ignore.printStackTrace();
//		}
//	}
//
//	// public void setCustomDatabaseFiles(HashMap<String, Pair<File, String>>
//	// customDatabaseFiles) {
//	// mRequestHandler.setCustomDatabaseFiles(customDatabaseFiles);
//	// }
//	//
//	// public void setInMemoryRoomDatabases(HashMap<String, SupportSQLiteDatabase>
//	// databases) {
//	// mRequestHandler.setInMemoryRoomDatabases(databases);
//	// }
//
//	public boolean isRunning() {
//		return mIsRunning;
//	}
//}
