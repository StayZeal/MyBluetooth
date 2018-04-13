package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import example.util.Log;

public class BufferTest {

	public static void main(String[] args) {

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("test.txt"));

			String line;

			while ((line = bufferedReader.readLine()) != null) {

				Log.i(line);
			}

			int postDataI = 1000;

			String postData = "";
			// read the post data
			if (postDataI > 0) {
				char[] charArray = new char[postDataI];
				bufferedReader.read(charArray, 0, postDataI);
				postData = new String(charArray);
			}

			Log.i("------------");
			Log.i("postdata: " + postData);

			bufferedReader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
