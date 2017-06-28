package com.paradis.dtserver;


import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Server {

    public static void main(String[] args) throws Exception {

        OptionParser parser = new OptionParser();
        parser.accepts( "port" ).withRequiredArg().ofType( Integer.class ).defaultsTo(40000);
        parser.accepts( "idle-timeout" ).withRequiredArg().ofType( Integer.class ).defaultsTo(5);
        OptionSet options = parser.parse(args);

        int port = ((Integer)options.valueOf("port")).intValue();
        int idleTimeout = ((Integer)options.valueOf("idle-timeout")).intValue();

        new Thread(new DateTimeTCPEngine(port, idleTimeout)).start();
        new Thread(new DateTimeUDPEngine(port)).start();
    }

}
