package com.jetty.launcher;

/**
 * Created with IntelliJ IDEA.
 * User: gfr
 * Date: 10/06/14
 * Time: 13:16
 * To change this template use File | Settings | File Templates.
 */

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyAdmin implements Runnable{

    private static Logger log = LoggerFactory.getLogger(AdminHandler.class);

    private Thread instance = null;
    private Server server = null;

    public JettyAdmin(Server server) {

        this.instance = new Thread(this);
        this.server = server;
    }
    @Override
    public void run() {
        try {
            server.setStopTimeout(10000L);
            server.stop();

        } catch (Exception ex) {
            log.error("Cannot Stop Jetty Server: " + ex.getMessage());
        }
    }

    public void stop() {
        this.instance.start();
        try {
            this.instance.join();
        } catch (InterruptedException intEx) {
            log.error("Cannot Stop Jetty Server: " + intEx.getMessage());        }
    }
}
