package log_rolling.ao;

public class AoLog {
    private String traceId;    // 트랜잭션 추적 ID (장애 분석 필수)
    private String method;     // GET, POST 등
    private String uri;        // API 경로
    private String logLevel;   // INFO, WARN, DEBUG
    private String message;    // 비즈니스 로직 내용
    private long executionTime;// 실행 소요 시간 (ms)

    public AoLog(String traceId, String method, String uri, String logLevel, String message, long executionTime) {
        this.traceId = traceId;
        this.method = method;
        this.uri = uri;
        this.logLevel = logLevel;
        this.message = message;
        this.executionTime = executionTime;
    }

    // Getter methods
    public String getTraceId() { return traceId; }
    public String getMethod() { return method; }
    public String getUri() { return uri; }
    public String getLogLevel() { return logLevel; }
    public String getMessage() { return message; }
    public long getExecutionTime() { return executionTime; }
}