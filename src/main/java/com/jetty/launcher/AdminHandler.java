package com.jetty.launcher;


import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminHandler extends AbstractHandler {

    private static Logger log = LoggerFactory.getLogger(AdminHandler.class);

    private Server jettyServer = null;
    private enum Operations {stop, restart};
    private JettyAdmin serverAdmin = null;
    private HandlerList handlerList = null;
    private int port;


    private boolean mustBeRunning = false;


    public AdminHandler(int port) {
        this.port = port;
        this.jettyServer = new Server(this.port);
        jettyServer.setStopAtShutdown(true);
        this.serverAdmin = new JettyAdmin(jettyServer);
    }

    public void setHandlerList(HandlerList list) {
        this.handlerList = list;
        this.handlerList.addHandler(this);
        this.jettyServer.setHandler(list);
    }


    public void startServer() throws Exception {

        if (!jettyServer.isRunning()) {
            jettyServer.start();
            this.mustBeRunning = true;
        }
    }

    public void joinServer() throws InterruptedException {

        jettyServer.join();
    }

//    private void restartServer(HttpServletResponse response, Operations operation) throws IOException {
//        this.isStarted = true;
//        serverAdmin.stop();
//    }

    private void stopServer(HttpServletResponse response, Operations operation) throws IOException {
        this.mustBeRunning = false;
        writingResponse(response, operation);
        serverAdmin.stop();
        jettyServer.destroy();
    }

    private void writingResponse(HttpServletResponse response, Operations operation) throws IOException {

        String message = null;

        switch (operation) {
            case stop:
                message = "Stopping Jetty Server";
                break;
            case restart:
                message = "Rearrancando Jetty Server";
                break;
        }

        log.info(message);
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        response.setContentType("text/plain");
        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.println(message);
        servletOutputStream.close();
        response.flushBuffer();

    }

    @Override
    public void handle(String string, Request baseRequest, HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {

        String pathInfo = request.getPathInfo();
        // THIS SHOULD OBVIOUSLY BE SECURED!!!

        if ("/admin/stop".equals(pathInfo)) {
            stopServer(response, Operations.stop);
        }
        else if ("/admin/restart".equals(pathInfo)) {
            //restartServer(response, Operations.restart);
        }
    }

    public boolean isMustBeRunning() {
        return mustBeRunning;
    }

    public void setMustBeRunning(boolean mustBeRunning) {
        this.mustBeRunning = mustBeRunning;
    }


}