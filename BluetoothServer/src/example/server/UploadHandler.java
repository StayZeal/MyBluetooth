package example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import example.model.TableDataResponse;
import example.sql.DB;
import example.sql.SqliteHelper;
import example.util.Log;
import example.util.TextUtils;
import example.util.Utils;

public class UploadHandler {

	public static final String INSERT_ACCELERATION = "inser_acceleration";
	public static final String INSERT_ANGLE = "inser_angle";
	public static final String INSERT_LIDU = "inser_lidu";
	private Gson mGson;

	public UploadHandler() {
		mGson = new GsonBuilder().serializeNulls().create();
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
					break;
				}

				if (line.indexOf("Content-Length:") > -1) {
					postDataI = new Integer(line.substring(line.indexOf("Content-Length:") + 16, line.length()))
							.intValue();
				}
				Log.i(line);

			}

			String postData = "";
			// read the post data
			if (postDataI > 0) {
				char[] charArray = new char[postDataI];
				reader.read(charArray, 0, postDataI);
				postData = new String(charArray);
			}

			Log.i(postData);
			uploadData(postData);

			Log.i("route:" + route);
			output = new PrintStream(socket.getOutputStream());

			// Send out the content.
			output.println("HTTP/1.1 200 OK");
//			output.println("Content-Type: " + Utils.detectMimeType(route));
			output.println("Content-Type: " + "application/octet-stream");
			String response = "{\"success\":true}";
			byte[] bytes = response.getBytes();
			output.println("Content-Length: " + bytes.length);
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

	private void uploadData(String data) {

		String[] params = data.split("&");
		int count = params.length;
		String tableName = null;
		String tableData = null;
		for (int i = 0; i < count; i++) {

			String[] values = params[i].split("=");
			if (values[0].equals("table_name")) {
				tableName = values[1];
			} else if (values[0].equals("db_data")) {
				tableData = values[1];
			}

		}

		try {
			tableData = tableData.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
			tableData = URLDecoder.decode(tableData, "utf-8");
			Log.i("table data: " + tableData);
			TableDataResponse tableDataResponse = new Gson().fromJson(tableData, TableDataResponse.class);
			insertData(tableName, tableDataResponse);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void insertData(String tableName, TableDataResponse tableDataResponse) {

		DB db = DB.getInstance();
		db.insert(tableName, tableDataResponse);
	}

}
