package main.server.session;

import static main.server.config.ServerConstant.INVALID_COMMAND_FORMAT_MESSAGE;
import static main.server.config.ServerConstant.INVALID_COMMAND_MESSAGE;
import static main.server.config.ServerConstant.JOIN;
import static main.server.config.ServerConstant.MESSAGE_TIME_FORMATTER;
import static main.server.config.ServerConstant.REQUEST_USERNAME_REGISTRATION_MESSAGE;
import static main.server.config.ServerConstant.SYSTEM;
import static main.util.MyLogger.log;
import static main.util.SocketCloseUtil.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import main.server.command.Command;

public class Session implements Runnable {

    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final SessionManager sessionManager;
    private boolean isClosed = false;
    private String userName = null;

    public Session(Socket socket, SessionManager sessionManager) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.sessionManager = sessionManager;
        sessionManager.add(this);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String received = input.readUTF();
                log(received);
                logic(received);
            }
        } catch (IOException e) {
            log(e);
        } finally {
            sessionCloseProcess();
        }
    }

    private void logic(String received) throws IOException {
        if (isInvalidCommandFormat(received)) {
            sendMessage(SYSTEM, INVALID_COMMAND_FORMAT_MESSAGE);
            return;
        }

        String commandStr = findCommandFromMessage(received);
        String content = findContentFromMessage(received);

        if (userName == null && !commandStr.equals(JOIN)) {
            sendMessage(SYSTEM, REQUEST_USERNAME_REGISTRATION_MESSAGE);
            return;
        }

        Command.fromString(commandStr)
                .ifPresentOrElse(
                        command -> command.execute(this, content),
                        () -> sendMessage(SYSTEM, INVALID_COMMAND_MESSAGE)
                );

    }

    private String findContentFromMessage(String received) {
        if (!received.contains("|")) {
            return null;
        }
        return received.split("\\|", 2)[1];
    }

    private String findCommandFromMessage(String received) {
        if (received.contains("|")) {
            return received.substring(1).split("\\|", 2)[0];
        }
        return received.substring(1);
    }

    public void changeUsername(String username) {
        this.userName = username;
    }

    public void registerUsername(String username) {
        this.userName = username;
    }

    private boolean isInvalidCommand(String command) {
        if (command.equals("join")
                || command.equals("message")
                || command.equals("change")
                || command.equals("users")
                || command.equals("exit")) {
            return false;
        }
        return true;
    }

    private boolean isInvalidCommandFormat(String received) {
        return !received.startsWith("/");
    }

    public void sendMessage(String from, String message) {
        String time = LocalDateTime.now().format(MESSAGE_TIME_FORMATTER);
        String formattedMessage = String.format("[%s] %s: %s", time, from, message);
        try {
            output.writeUTF(formattedMessage);
        } catch (IOException e) {
            log("메시지 전송 중 오류 발생: " + e);
        }
    }

    public String getUserName() {
        return userName;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void sessionCloseProcess() {
        sessionManager.remove(this);
        close();
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
