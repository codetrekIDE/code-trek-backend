package webide.codeeditor.file.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class CodeExecutionService {

    // 파이썬 코드를 실행하고 결과를 반환하는 메서드
    public String executePythonCode(String code) throws IOException {

        //temp.py 임시 저장 파일 생성
        String filePath = "temp.py";
        Files.write(Paths.get(filePath), code.getBytes()); //code문자열을 바이트배열로 파일에 저장

        // ProcessBuilder를 사용해 Python 코드를 실행
        ProcessBuilder processBuilder = new ProcessBuilder("python3", filePath);
        processBuilder.redirectErrorStream(true); // 표준 출력과 오류 출력을 하나로 합침
        // redirectErrorStream : 개별 오류 처리 로직을 짜지 않고도 알아서 발생하는 오류를 읽어와줌

        Process process = processBuilder.start(); // 파이썬 코드 시작

        // 출력 및 오류를 읽어오기 위한 스트림
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            //reader : BufferedReader와 InputStreamReader를 사용해 프로세스의 출력 을 한 줄씩 읽어옴

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) { //한 줄씩 끝까지 읽어옴
                output.append(line).append("\n");
            }

            //오류 스트림도 읽어서 출력에 추가
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            process.waitFor(); // 프로세스가 끝날 때까지 기다림
            return output.toString(); // Python 코드 실행 결과 반환
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Python script execution", e);
        } finally {
            // 임시 파일 삭제
            Files.deleteIfExists(Paths.get(filePath));
        }
    }
}
