# 🏦 금융권 로그 관리 시스템 (Log Rolling System)

이 프로젝트는 금융 시스템에서 필수적으로 요구되는 **로그 관리 정책**을 Java와 Logback을 사용하여 구현한 예제입니다.
**거래 로그(Transaction Log)** 와 **애플리케이션 운영 로그(Operation Log)** 를 분리하여 관리하며, 각 목적에 맞는 보관 기간과 아카이빙(Archiving) 정책을 적용했습니다.

## 📂 프로젝트 구조 (Directory Structure)

```bash
log-rolling
├── src
│   └── log_rolling
│       ├── ao  # Application Operation (운영 로그)
│       │   ├── AoLog.java
│       │   ├── ApplicationLogger.java
│       │   └── Test.java
│       └── txn # Transaction (거래 로그)
│           ├── TxnLog.java
│           ├── TransactionAuditLogger.java
│           └── Test.java
├── logs        # 로그 적재 폴더 (Git 제외)
│   ├── ao      # 운영 로그 저장소
│   └── txn     # 거래 로그 저장소
└── src/resources
    └── logback.xml # 핵심 설정 파일

```

---

## 🚀 주요 기능 (Key Features)

### 1. 로그 정책 분리 (Logging Policy)

금융권 규정 및 운영 효율성을 위해 로그를 두 가지 형태로 분리하여 저장합니다.

| 구분 | 거래 로그 (TXN) | 운영 로그 (AO) |
| --- | --- | --- |
| **목적** | 금융 거래 증적, 데이터 무결성 확인 | 장애 분석, 트러블슈팅, 흐름 추적 |
| **롤링 정책** | 10MB 도달 시 or 일 단위 | 10MB 도달 시 or 일 단위 |
| **주요 특징** | 민감정보(계좌 등) **마스킹 처리** | **Trace ID**를 통한 요청 흐름 추적 |

---

## 🛠 상세 구현 내용

### 1️⃣ 거래 로그 (Transaction Log)

계좌 이체, 조회 등 금융 거래 데이터를 기록합니다. 민감한 개인정보는 반드시 마스킹 처리되어야 합니다.

주요 코드: `TransactionAuditLogger.java`

```java
public class TransactionAuditLogger {
    // ...
    public void log(TxnLog txnLog) {
        // 계좌번호 마스킹 처리 (보안 필수 요건)
        String maskedAccount = maskAccount(txnLog.getAccountNumber());
        
        // 데이터 위주의 간결한 로그 기록
        if (txnLog.getResult().equals("SUCCESS")) {
            logger.info("txnId={} username={} account={} amount={} result={}", 
                        txnLog.getTxnId(),
                        txnLog.getUsername(), 
                        maskedAccount,  // 마스킹 된 데이터 저장
                        txnLog.getAmount(), 
                        txnLog.getResult()
                    );
        } else {
            // ... (ERROR/WARN 처리)
        }
    }

    private String maskAccount(String accountNumber) {
        // 1234-123456-1234 -> 1234-******-1234 로 변환
        // ...
    }
}

```

### 2️⃣ 운영 로그 (Application Operation Log)

시스템의 장애를 추적하기 위한 로그입니다. 어떤 사용자의 요청인지 식별하기 위해 **MDC(Mapped Diagnostic Context)** 를 사용하여 Trace ID를 부여합니다.

주요 코드: `ApplicationLogger.java`

```java
public class ApplicationLogger {
    // ...
    public void log(AoLog logData) {
        // MDC에 Trace ID를 저장하여 로그의 문맥(Context)을 유지
        MDC.put("traceId", logData.getTraceId());

        try {
            // 상세한 실행 정보 기록 (URI, 실행시간 등)
            String formattedMsg = String.format("[%s] %s - %s (Exec: %dms)",
                    logData.getMethod(),
                    logData.getUri(),
                    logData.getMessage(),
                    logData.getExecutionTime());
            
            // ... (Level별 로깅)
        } finally {
            // 메모리 누수 방지를 위한 컨텍스트 초기화
            MDC.clear(); 
        }
    }
}

```

---

## ⚙️ 설정 (Logback Configuration)

`logback.xml`을 통해 **Appender** 를 분리하고, 서로 다른 **Rolling Policy** 를 적용했습니다.

핵심 설정: `logback.xml`

```xml
<configuration>
    <appender name="TXN_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_DIR}/txn/txn.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_DIR}/txn/archive/txn.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <maxHistory>14</maxHistory> </rollingPolicy>
        </appender>

    <appender name="AO_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_DIR}/ao/app-ops.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <maxHistory>180</maxHistory> </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] [%X{traceId}] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    </configuration>

```

---

## 🏃‍♂️ 실행 및 결과 확인 (How to Run)

각 패키지의 `Test.java`를 실행하여 대량의 로그를 발생시키고 롤링 테스트를 진행할 수 있습니다.

1. **테스트 실행:** `log_rolling.txn.Test` 또는 `log_rolling.ao.Test` 실행
2. **로그 적재 확인:**
* `logs/txn/` 폴더 내 `txn.log` 확인 (실시간 로그)
* `logs/txn/archive/` 폴더 내 `.gz` 압축 파일 확인 (10MB 초과 시 생성)


3. **결과 예시:**

**TXN 로그 (마스킹 적용됨):**

```text
2026-02-04 15:30:00.123 INFO txnId=abc-123... account=3333-******-1234 amount=50000 result=SUCCESS

```

**AO 로그 (Trace ID 포함됨):**

```text
2026-02-04 15:30:00 [main] [trace-id-xyz] INFO l.r.ao.ApplicationLogger - [GET] /api/user - OK (Exec: 50ms)

```
