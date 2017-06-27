package com.paradis.dtserver;


public class Server {

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }

        new Thread(new DateTimeTCPEngine(port)).start();
        new Thread(new DateTimeUDPEngine(port)).start();

    }

}
