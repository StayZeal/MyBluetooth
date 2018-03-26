package example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import example.util.TextUtils;
import example.util.Utils;

/**
 * 处理get请求
 * 
 */
public class GetHandler {

	/**
	 * 处理get请求
	 * 
	 * @param socket
	 */
	public void getProcess(Socket socket) {

		BufferedReader reader = null;
		PrintStream output = null;
		try {
			String route = null;

			// Read HTTP headers and parse out the route.
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			while (!TextUtils.isEmpty(line = reader.readLine())) {
				if (line.startsWith("GET /")) {
					int start = line.indexOf('/') + 1;
					int end = line.indexOf(' ', start);
					route = line.substring(start, end);
					break;
				}
			}

			// Output stream that we send the response to
			output = new PrintStream(socket.getOutputStream());

			if (route == null || route.isEmpty()) {
				route = "index.html";
			}

			byte[] bytes = null;

			if (route.startsWith("getDbList")) {
				final String response = getDBListResponse();
				bytes = response.getBytes();
			} else if (route.startsWith("getAllDataFromTheTable")) {
				final String response = getAllDataFromTheTableResponse(route);
				bytes = response.getBytes();
			} else if (route.startsWith("getTableList")) {
				final String response = getTableListResponse(route);
				bytes = response.getBytes();
			} else if (route.startsWith("addTableData")) {
				final String response = addTableDataAndGetResponse(route);
				bytes = response.getBytes();
			} else if (route.startsWith("updateTableData")) {
				final String response = updateTableDataAndGetResponse(route);
				bytes = response.getBytes();
			} else if (route.startsWith("deleteTableData")) {
				final String response = deleteTableDataAndGetResponse(route);
				bytes = response.getBytes();
			} else if (route.startsWith("query")) {
				final String response = executeQueryAndGetResponse(route);
				bytes = response.getBytes();
			} else if (route.startsWith("downloadDb")) {
				// bytes = Utils.getDatabase(mSelectedDatabase, mDatabaseFiles);
			} else {
				// bytes = Utils.loadContent(route, mAssets);
			}

			if (null == bytes) {
				writeServerError(output);
				return;
			}

			// Send out the content.
			output.println("HTTP/1.0 200 OK");
			output.println("Content-Type: " + Utils.detectMimeType(route));

			if (route.startsWith("downloadDb")) {
				// output.println("Content-Disposition: attachment; filename=" +
				// mSelectedDatabase);
			} else {
				output.println("Content-Length: " + bytes.length);
			}
			output.println();
			output.write(bytes);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	private void writeServerError(PrintStream output) {
		// TODO Auto-generated method stub

	}

	private String executeQueryAndGetResponse(String route) {
		// TODO Auto-generated method stub
		return null;
	}

	private String deleteTableDataAndGetResponse(String route) {
		// TODO Auto-generated method stub
		return null;
	}

	private String updateTableDataAndGetResponse(String route) {
		// TODO Auto-generated method stub
		return null;
	}

	private String addTableDataAndGetResponse(String route) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getTableListResponse(String route) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getAllDataFromTheTableResponse(String route) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getDBListResponse() {
		// TODO Auto-generated method stub
		return null;
	}

}
