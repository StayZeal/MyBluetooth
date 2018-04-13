package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient {

    public static void main(String[] args) {

        Socket socket  = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("192.168.3.26", 8085);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            out.println("QUERY TIME ORDER\n"+ System.lineSeparator());
            System.out.println("send order to server succeed!");
            
            String resp = in.readLine();
//            out.close();
            System.out.println("Now time is : "+resp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(out != null){
                out.close();
                out = null;
            }
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                in = null;
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}