package com.fortuna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        Socket s = ss.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        String[] request = in.readLine().split(" ");

        String method = request.length > 0 ? request[0] : null;
        String path = request.length > 1 ? request[1] : null;
        String version = request.length > 2 ? request[2] : null;
        
        System.out.println(method + " " + path + " " + version);

        String header;

        do {

            header = in.readLine();
            System.out.println(header);

        } while (!header.isEmpty());

        String body;

        switch (method) {
            case "GET":
                body = "<b>Ciao</b> a tutti";
        
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: Text/html");
                out.println("Content-Length: "  + body.length());
                out.println("");
                out.println(body);
                
                break;
        
            default:
                break;
        }

        s.close();
        ss.close();
        in.close();
        out.close();
    }
}