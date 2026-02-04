package log_rolling.ao;

import java.util.UUID;
import java.util.Random;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        ApplicationLogger logger = new ApplicationLogger();


        System.out.println("Starting Application Operation Log Simulation...");

        for (int i = 1; i <= 10000000; i++) {

            AoLog logData = randomAoLog(i);
            logger.log(logData);
        }

        System.out.println("Done. Check logs/ao/app-ops.log and archive folder.");
    }

    private static AoLog randomAoLog(int i) {
        Random random = new Random();
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        String[] methods = {"GET", "POST", "PUT"}; 
        String[] uris = {"/api/v1/users", "/api/v1/account/balance", "/api/v1/transfer", "/auth/login"}; 

        String method = methods[random.nextInt(methods.length)];
        String uri = uris[random.nextInt(uris.length)];

        String logLevel = "INFO";
        String message = "Processing request successfully"; 

        if (random.nextInt(100) < 10) {
            logLevel = "WARN"; 
            message = "Response delayed or data not found";
        }

        long execTime = 10 + random.nextInt(490);

        return new AoLog(traceId, method, uri, logLevel, message, execTime);
    }
}