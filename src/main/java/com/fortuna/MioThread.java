package com.fortuna;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class MioThread extends Thread {

    private Socket s;

    public MioThread(Socket s) {
        this.s = s;
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream outBinary = null;

        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
            outBinary = new BufferedOutputStream(s.getOutputStream());

            String[] firstline = in.readLine().split(" ");

            if (firstline.length < 3) {
                out.println("HTTP/1.1 400 Bad Request");
                out.println("Content-Type: Text/html");
                out.println("Content-Length: 15");
                out.println("");
                out.println("Bad Request");
                return;
            }

            String method = firstline[0];
            String path = firstline[1];
            String version = firstline[2];

            System.out.println(method + " " + path + " " + version);

            String header;
            while ((header = in.readLine()) != null && !header.isEmpty()) {
                System.out.println(header);
            }

            switch (method) {
                case "GET":
                    if (path.endsWith("/")) {
                        path += "index.html";
                    }

                    File file = new File("htdocs/" + path);

                    if (file.exists()) {
                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Length: " + file.length());

                        String mimeType = Files.probeContentType(file.toPath());
                        if (mimeType == null)
                            mimeType = "application/octet-stream";

                        out.println("Content-Type: " + mimeType);
                        out.println("");

                        try (InputStream input = new FileInputStream(file)) {
                            byte[] buf = new byte[8192];
                            int n;
                            while ((n = input.read(buf)) != -1) {
                                outBinary.write(buf, 0, n);
                            }
                            outBinary.flush();
                        }

                    } else {
                        String body = "404 Pagina non trovata";

                        out.println("HTTP/1.1 404 NOT FOUND");
                        out.println("Content-Type: Text/html");
                        out.println("Content-Length: " + body.length());
                        out.println("");
                        out.println(body);
                    }
                    break;

                default:
                    break;
            }

            s.close();
            in.close();
            out.close();
            outBinary.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
