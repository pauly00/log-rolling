package log_rolling.ao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ApplicationLogger {

    private static final Logger logger = LoggerFactory.getLogger("ApplicationLogger");

    public void log(AoLog logData) {
        MDC.put("traceId", logData.getTraceId());

        try {
            String formattedMsg = String.format("[%s] %s - %s (Exec: %dms)",
                    logData.getMethod(),
                    logData.getUri(),
                    logData.getMessage(),
                    logData.getExecutionTime());

            if ("WARN".equals(logData.getLogLevel())) {
                logger.warn(formattedMsg);
            } else if ("DEBUG".equals(logData.getLogLevel())) {
                logger.debug(formattedMsg);
            } else {
                logger.info(formattedMsg);
            }
        } finally {
            MDC.clear(); 
        }
    }
}