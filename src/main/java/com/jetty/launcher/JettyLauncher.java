package com.jetty.launcher;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created with IntelliJ IDEA.
 * User: gfr
 * Date: 09/06/14
 * Time: 21:54
 * To change this template use File | Settings | File Templates.
 */
public class JettyLauncher {
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(4444);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("site.war");

        server.setHandler(webapp);

        server.start();
        server.join();
    }

}