package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TimeServer {

    public static void main(String[] args) throws IOException {
        int port  = 8085;
        ServerSocket server = null;
        try {
            // ��������
            server = new ServerSocket(port);
            System.out.println("The time server is start  in port :"+port);
            Socket socket = null;
            while(true){
                // ����
                socket = server.accept(); 
                new Thread(new TimeServerHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(server != null){
                server.close();
                System.out.println("The time server is close!");
                server = null;
            }
        }
    }


}

class TimeServerHandler implements Runnable{


    private Socket socket = null;

    public  TimeServerHandler(Socket socket ) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // �ַ���ȡ������
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()),true);
            String currentTime = null;
            String body = null;
            while(true){
                body = in.readLine();
                if(body == null){
                    break;
                }
                System.out.println("The time server receive order:"+body);
                // ����ͻ��˷�����ָ��Ϊ��QUERY TIME ORDER �򷵻ص�ǰʱ�䣬 ���򷵻� BAD ORDER ָ����Ч
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date().toString():"BAD ORDER";
            }
            // ��Ӧ�ͻ���
            out.print(currentTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if(out != null){
                out.close();
                out = null;
            }
            if(socket != null){
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.socket = null;
            }

        }
    }

}
