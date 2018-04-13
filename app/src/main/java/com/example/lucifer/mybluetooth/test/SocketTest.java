package com.example.lucifer.mybluetooth.test;


import java.io.IOException;
import java.net.Socket;

public class SocketTest {

    public void test() {
        Socket socket = new Socket();
        try {
            socket.connect(null, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
