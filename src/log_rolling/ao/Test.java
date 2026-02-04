package log_rolling.ao; // 이 파일이 저장된 폴더 위치입니다.

import java.util.UUID;
import java.util.Random;

public class Test {

    // main 함수: 프로그램이 시작되는 입구입니다.
    // throws InterruptedException: 중간에 '잠깐 멈춤(sleep)' 기능을 쓸 건데, 
    // 혹시 멈추다가 에러가 나면 밖으로 던져버리겠다는 뜻입니다.
    public static void main(String[] args) throws InterruptedException {
        
        // 1. 로거(기록 담당자) 생성
        // 이제부터 'logger'에게 "기록해!"라고 명령할 수 있습니다.
        ApplicationLogger logger = new ApplicationLogger();
        
        
        System.out.println("Starting Application Operation Log Simulation...");

        // 2. 1000번 반복하기 (로그 1000줄 쌓기)
        for (int i = 1; i <= 1000; i++) {
            
            // [단계 1] 가짜 로그 데이터 하나 만들기
            // 아래에 있는 'randomAoLog' 함수를 시켜서 임의의 데이터를 받아옵니다.
            AoLog logData = randomAoLog(i);
            
            // [단계 2] 실제로 기록하기
            // 받아온 데이터를 로거에게 넘겨서 파일에 적게 합니다.
            logger.log(logData);
            
            
            // [단계 3] 잠깐 쉬기 (0.005초)
            // 컴퓨터가 너무 빨라서 순식간에 끝나버리면 로그 시간을 확인하기 어려우니
            // 아주 잠깐씩 텀을 줍니다.
            Thread.sleep(5); 
        }

        System.out.println("Done. Check logs/ao/app-ops.log and archive folder.");
    }

    // 이 함수는 '랜덤한 로그 데이터(AoLog)'를 하나 만들어서 돌려줍니다.
    private static AoLog randomAoLog(int i) {
        Random random = new Random(); // 랜덤 값을 뽑을 기계를 준비합니다.
        
        // 1. 추적 ID(Trace ID) 만들기
        // UUID는 전 세계에서 유일한 ID를 만드는 기술입니다.
        // 너무 기니까 앞의 8글자만 잘라서 씁니다. (예: "a1b2c3d4")
        // *목적: 수많은 로그 중에 이 요청이 어디서부터 시작됐는지 꼬리표를 달아주는 것.
        String traceId = UUID.randomUUID().toString().substring(0, 8);

        // 2. 가상의 API 상황 연출
        // 어떤 요청이 들어왔는지 랜덤으로 정하기 위한 재료들입니다.
        String[] methods = {"GET", "POST", "PUT"}; // HTTP 메소드(조회, 등록, 수정)
        String[] uris = {"/api/v1/users", "/api/v1/account/balance", "/api/v1/transfer", "/auth/login"}; // API 주소
        
        // 재료들 중에서 하나를 제비뽑기하듯 무작위로 고릅니다.
        String method = methods[random.nextInt(methods.length)];
        String uri = uris[random.nextInt(uris.length)];

        // 3. 로그 내용과 중요도(Level) 정하기
        // 기본값은 "정상(INFO)"으로 설정해 둡니다.
        String logLevel = "INFO";
        String message = "Processing request successfully"; // "요청이 정상 처리됨"
        
        // 10% 확률로 "경고(WARN)" 상황 만들기
        // 0부터 99까지 숫자 중 하나를 뽑았는데 10보다 작다면(0~9), 즉 10% 확률입니다.
        if (random.nextInt(100) < 10) {
            logLevel = "WARN"; // 레벨을 경고로 바꿈
            message = "Response delayed or data not found"; // 메시지도 "지연됨/데이터 없음"으로 바꿈
        }
        
        // 4. 실행 시간 시뮬레이션
        // 이 작업이 걸린 시간을 10ms ~ 500ms 사이 랜덤 값으로 정합니다.
        long execTime = 10 + random.nextInt(490);

        // 위에서 만든 모든 정보를 담은 'AoLog' 객체를 포장해서 반환(리턴)합니다.
        return new AoLog(traceId, method, uri, logLevel, message, execTime);
    }
}