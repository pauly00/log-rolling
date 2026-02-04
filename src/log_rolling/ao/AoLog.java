package log_rolling.ao;

public class AoLog {
    private String traceId;   
    private String method;    
    private String uri;       
    private String logLevel;   
    private String message;   
    private long executionTime;

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