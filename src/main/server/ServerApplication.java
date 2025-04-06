package main.server;

import static main.server.config.ServerConstant.*;
import static main.util.MyLogger.log;

import main.server.session.Session;
import main.server.session.SessionManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {

    public static void main(String[] args) throws IOException {
        log("서버 시작");

        ServerSocket serverSocket = new ServerSocket(PORT);
        SessionManager sessionManager = new SessionManager();
        log("서버 소켓 시작 - 리스닝 포트: " + PORT);

        ShutDownHook shutDownHook = new ShutDownHook(sessionManager, serverSocket);
        Runtime.getRuntime().addShutdownHook(new Thread(shutDownHook, "shutdown"));

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                log("소켓 연결: " + socket);

                Thread thread = new Thread(new Session(socket, sessionManager));
                thread.start();
            }
        } catch (IOException e) {
            log("서버 소켓 종료: " + e);
        }

    }

    static class ShutDownHook implements Runnable {

        private final SessionManager sessionManager;
        private final ServerSocket serverSocket;

        public ShutDownHook(SessionManager sessionManager, ServerSocket serverSocket) {
            this.sessionManager = sessionManager;
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            log("ShutDownHook 실행");
            try {
                sessionManager.closeAll();
                serverSocket.close();

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("e = " + e);
            }
        }
    }
}
