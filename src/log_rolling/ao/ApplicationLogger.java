package log_rolling.ao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ApplicationLogger {

    // [중요] 여기가 핵심입니다! 
    // "ApplicationLogger"라는 이름이 XML의 <logger name="ApplicationLogger">와 똑같아야 합니다.
    // 만약 txn처럼 패키지명으로 하고 싶다면 "log_rolling.ao.ApplicationLogger"로 XML과 Java를 둘 다 맞춰야 합니다.
    private static final Logger logger = LoggerFactory.getLogger("ApplicationLogger");

    public void log(AoLog logData) {
        // Trace ID 설정 (로그 추적용)
        MDC.put("traceId", logData.getTraceId());

        try {
            // 출력 포맷 만들기: [POST] /api/v1/users - 메시지 (Exec: 100ms)
            String formattedMsg = String.format("[%s] %s - %s (Exec: %dms)",
                    logData.getMethod(),
                    logData.getUri(),
                    logData.getMessage(),
                    logData.getExecutionTime());

            // 레벨별로 찍기
            if ("WARN".equals(logData.getLogLevel())) {
                logger.warn(formattedMsg);
            } else if ("DEBUG".equals(logData.getLogLevel())) {
                logger.debug(formattedMsg);
            } else {
                logger.info(formattedMsg);
            }
        } finally {
            MDC.clear(); // ID 초기화 (필수)
        }
    }
}