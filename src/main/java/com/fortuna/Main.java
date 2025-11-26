package com.fortuna;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(8080);

        do {
            Socket s = ss.accept();
            MioThread thread = new MioThread(s);
            thread.start();
        } while (true);

    }
}