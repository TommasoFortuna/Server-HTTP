package com.fortuna;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MioThread extends Thread {

    Socket s;

    public MioThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            String[] firstline = in.readLine().split(" ");

            String method = firstline.length > 0 ? firstline[0] : null;
            String path = firstline.length > 1 ? firstline[1] : null;
            String version = firstline.length > 2 ? firstline[2] : null;

            System.out.println(method + " " + path + " " + version);

            String header;

            do {

                header = in.readLine();
                System.out.println(header);

            } while (!header.isEmpty());

            String body;

            switch (method) {
                case "GET":
                    if (path.equals("/nome")) {
                        body = "<b>Ciao</b> a tutti";

                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: Text/html");
                        out.println("Content-Length: " + body.length());
                        out.println("");

                    } else {
                        body = "Pagina non trovata";

                        out.println("HTTP/1.1 404 NOT FOUND");
                        out.println("Content-Type: Text/html");
                        out.println("Content-Length: " + body.length());
                        out.println("");

                    }
                    out.println(body);

                    break;

                default:

                    break;
            }

            s.close();
            in.close();
            out.close();
        } catch (Exception e) {

        }
    }
}
