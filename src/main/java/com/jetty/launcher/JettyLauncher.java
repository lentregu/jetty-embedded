package com.jetty.launcher;



import com.jetty.launcher.config.ConfigReader;
import com.jetty.launcher.log.LogFormat;
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

    private static LogFormat logFormat = LogFormat.getInstance();

    private static AdminHandler adminHandler = null;

    public static void main(String[] args)  throws Exception {

        config.loadProperties();
        // Handler to manage the webapp
        WebAppContext webappHandler = new WebAppContext();

        if (config.webAppWar() != null) {
            webappHandler.setWar(config.webAppWar());
            webappHandler.setContextPath(config.webAppContextPath());
        } else {
            log.error("", new JettyLauncherException("Expected a webapp to start it"));
            System.exit(-1);
        }

        // I added this to show how to add access logs to an embedded server.
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        NCSARequestLog requestLog = new NCSARequestLog(config.serverLog());
        requestLog.setAppend(true);
        requestLog.setExtended(true);
        requestLog.setLogTimeZone(config.serverLogTimeZone());
        requestLogHandler.setRequestLog(requestLog);
        //requestLogHandler.setHandler(webappHandler);

        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[] {webappHandler, requestLogHandler});

        int serverPort = config.serverPort();
        log.info(String.format("Starting Jetty on port %d", serverPort));

        adminHandler = new AdminHandler(serverPort);
        adminHandler.setHandlerList(handlerList);
        do {

            try {
                adminHandler.startServer();
                if (!webappHandler.isAvailable()) {
                    //log.error(String.format("Can't load the webapp: %s", webappHandler.getWar()));
                    log.error("Can't load the webapp: {}, {}", webappHandler.getWar());
                    log.error(logFormat.format("esto", "es", "una", "prueba"), "esto", "es", "una", "prueba");
                    log.error(logFormat.format("est"), "OTRA PRUEBA");
                    log.error(logFormat.format("est","MAS"), "OTRA PRUEBA", "MAS");
                    log.error(String.format("Prueba formateando objeto a string %s: ", new Integer(5234)));
                    System.exit(-1);
                }
            } catch (JettyLauncherException e) {
                log.error("Can't start the Jetty Server");
                System.exit(-1);
            }
            adminHandler.joinServer();
        } while (adminHandler.isMustBeRunning());

    }
}