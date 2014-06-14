package com.jetty.launcher;



import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyLauncher {

    private static Logger log = LoggerFactory.getLogger(JettyLauncher.class);

    private static ConfigReader config = ConfigReader.getInstance("config.properties");

    private static AdminHandler adminHandler = null;

    public static void main(String[] args) throws Exception {

        config.loadProperties();
        // Handler to manage the webapp
        WebAppContext webappHandler = new WebAppContext();
        webappHandler.setContextPath(config.get("webapp.contextPath"));
        webappHandler.setWar(config.get("webapp.war"));

        // I added this to show how to add access logs to an embedded server.
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        NCSARequestLog requestLog = new NCSARequestLog(config.get("server.log"));
        requestLog.setAppend(true);
        requestLog.setExtended(true);
        requestLog.setLogTimeZone(config.get("server.log.timezone"));
        requestLogHandler.setRequestLog(requestLog);
        //requestLogHandler.setHandler(webappHandler);

        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[] {webappHandler, requestLogHandler});

        int serverPort = Integer.parseInt(config.get("server.port"));
        log.info(String.format("Starting Jetty on port %d", serverPort));

        adminHandler = new AdminHandler(serverPort);
        adminHandler.setHandlerList(handlerList);
        do {
             log.info("Arrancamos Jetty");
             adminHandler.startServer();
             adminHandler.joinServer();
        } while (adminHandler.isMustBeRunning());

    }
}