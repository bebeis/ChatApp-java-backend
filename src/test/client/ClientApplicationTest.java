package test.client;

import static org.junit.jupiter.api.Assertions.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClientApplicationTest {
    private static final int TEST_PORT = 12345;
    private ServerSocket serverSocket;
    private Thread serverThread;

    @BeforeEach
    public void setUp() throws IOException {
        serverSocket = new ServerSocket(TEST_PORT);
        // 서버 스레드 시작: 클라이언트 연결을 대기하고 메시지 교환을 수행
        serverThread = new Thread(() -> {
            try (Socket socket = serverSocket.accept();
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                // 클라이언트로부터 메시지 읽기
                String received = input.readUTF();
                System.out.println("서버 수신: " + received);
                // 응답 전송 (필요에 따라)
                output.writeUTF("서버 응답: " + received);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }

    @AfterEach
    public void tearDown() throws IOException, InterruptedException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        serverThread.join();
    }

    @Test
    public void testClientConnectionAndMessage() throws IOException {
        // 테스트를 위해 클라이언트 소켓을 직접 생성하여 서버와 통신을 검증
        try (Socket socket = new Socket("localhost", TEST_PORT);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())) {

            String testMessage = "테스트 메시지";
            output.writeUTF(testMessage);

            // 서버에서 보낸 응답을 읽어 테스트 검증
            String response = input.readUTF();
            assertEquals("서버 응답: " + testMessage, response);
        }
    }
}