package example.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by amitshekhar on 06/02/17.
 */

public class Utils {

    private static final String TAG = "Utils";

    private Utils() {
        // This class in not publicly instantiable
    }

    public static String detectMimeType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        } else if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else {
            return "application/octet-stream";
        }
    }

//    public static byte[] loadContent(String fileName, AssetManager assetManager) throws IOException {
//        InputStream input = null;
//        try {
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            input = assetManager.open(fileName);
//            byte[] buffer = new byte[1024];
//            int size;
//            while (-1 != (size = input.read(buffer))) {
//                output.write(buffer, 0, size);
//            }
//            output.flush();
//            return output.toByteArray();
//        } catch (FileNotFoundException e) {
//            return null;
//        } finally {
//            try {
//                if (null != input) {
//                    input.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static byte[] getDatabase(String selectedDatabase, HashMap<String, Pair<File, String>> databaseFiles) {
//        if (TextUtils.isEmpty(selectedDatabase) || !databaseFiles.containsKey(selectedDatabase)) {
//            return null;
//        }
//
//        byte[] byteArray = new byte[0];
//        try {
//            File file = databaseFiles.get(selectedDatabase).first;
//
//            byteArray = null;
//            try {
//                InputStream inputStream = new FileInputStream(file);
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                byte[] b = new byte[(int) file.length()];
//                int bytesRead;
//
//                while ((bytesRead = inputStream.read(b)) != -1) {
//                    bos.write(b, 0, bytesRead);
//                }
//
//                byteArray = bos.toByteArray();
//            } catch (IOException e) {
//                Log.e(TAG, "getDatabase: ", e);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return byteArray;
//    }

}
