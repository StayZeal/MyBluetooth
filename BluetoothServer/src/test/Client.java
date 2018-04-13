package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

    private static final String IP = "192.168.3.26";
    private static final int PORT = 80;


    public static void main(String[] args) {

        Client client = new Client();
        client.start(IP, PORT);
    }

    public void start(String ip, int port) {
        Socket socket;
        OutputStream outputStream;
        OutputStreamWriter outputStreamWriter;
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        try {
            socket = new Socket(ip, port);
            outputStream = socket.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);

            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write("hahaha"+"0\r");
            bufferedWriter.write("GET "  + " HTTP/1.1\r\n");  
            bufferedWriter.write("Host: "+ "\r\n");  
            bufferedWriter.write("\r\n");  

            bufferedWriter.flush();

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String response = bufferedReader.readLine();

            System.out.println("response: " + response);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
