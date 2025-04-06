package main.server.session;

import static main.util.MyLogger.log;
import static main.util.SocketCloseUtil.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable {

    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final SessionManager sessionManager;
    private boolean isClosed;

    public Session(Socket socket, SessionManager sessionManager) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.sessionManager = sessionManager;
        isClosed = false;
    }

    @Override
    public void run() {

        try {
            while (true) {
                // TODO: 비즈니스 로직 구현
            }
        } finally {
            sessionManager.remove(this);
            close();
        }
    }

    public void close() {
        if (isClosed) {
            return;
        }

        closeAll(socket, input, output);
        isClosed = true;
        log("연결 종료: " + socket);
    }
}
