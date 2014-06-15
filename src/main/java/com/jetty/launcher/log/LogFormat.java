package com.jetty.launcher.log;

/**
 * Created with IntelliJ IDEA.
 * User: gfr
 * Date: 12/06/14
 * Time: 12:30
 * To change this template use File | Settings | File Templates.
 */
public class LogFormat {

    private static LogFormat instance;
    private static final String PLACE_HOLDER="{}";
    private static final String SEPARATOR=" | ";

    public static LogFormat getInstance() {
        if (instance == null) {
            instance = new LogFormat();
        }

        return instance;
    }

    public String format(Object ... params) {

        StringBuilder formatParams= new StringBuilder(PLACE_HOLDER);

        if (params.length > 1) {
            for (int i=0; i< params.length-1; i++) {
                formatParams.append(SEPARATOR).append(PLACE_HOLDER);
            }
//            formatParams.append(PLACE_HOLDER);
        }
        return formatParams.toString();
    }
}
