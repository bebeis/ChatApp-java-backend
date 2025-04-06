package main.server.session;

import static main.server.config.ServerConstant.CHANGE_USERNAME;
import static main.server.config.ServerConstant.CLOSE_CONNECTION;
import static main.server.config.ServerConstant.FIND_ALL_USER;
import static main.server.config.ServerConstant.INVALID_COMMAND_FORMAT_MESSAGE;
import static main.server.config.ServerConstant.INVALID_COMMAND_MESSAGE;
import static main.server.config.ServerConstant.JOIN;
import static main.server.config.ServerConstant.MESSAGE_TIME_FORMATTER;
import static main.server.config.ServerConstant.REQUEST_USERNAME_REGISTRATION_MESSAGE;
import static main.server.config.ServerConstant.SEND_MESSAGE;
import static main.server.config.ServerConstant.SYSTEM;
import static main.util.MyLogger.log;
import static main.util.SocketCloseUtil.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

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

        String command = findCommandFromMessage(received);
        String content = findContentFromMessage(received);

        if (isInvalidCommand(command)) {
            sendMessage(SYSTEM, INVALID_COMMAND_MESSAGE);
            return;
        }

        if (command.equals(JOIN)) {
            registerUsername(content);
            sendMessage(SYSTEM, "등록된 이름: " + userName);
            return;
        }

        if (userName == null) {
            sendMessage(SYSTEM, REQUEST_USERNAME_REGISTRATION_MESSAGE);
            return;
        }

        if (command.equals(SEND_MESSAGE)) {
            sessionManager.sendMessageToAll(userName, content);
            return;
        }

        if (command.equals(CHANGE_USERNAME)) {
            changeUsername(content);
            sendMessage(SYSTEM, "변경된 이름: " + userName);
            return;
        }

        if (command.equals(FIND_ALL_USER)) {
            List<String> usernames = sessionManager.findConnectedUserAll();
            StringBuilder sb = new StringBuilder();
            for (String username : usernames) {
                sb.append(username).append("\n");
            }
            output.writeUTF(sb.toString());
            return;
        }

        if (command.equals(CLOSE_CONNECTION)) {
            sessionCloseProcess();
            return;
        }

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

    private void changeUsername(String username) {
        this.userName = username;
    }

    private void registerUsername(String username) {
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

    public void sendMessage(String from, String message) throws IOException {
        String time = LocalDateTime.now().format(MESSAGE_TIME_FORMATTER);
        String formattedMessage = String.format("[%s] %s: %s", time, from, message);
        output.writeUTF(formattedMessage);
    }

    public String getUserName() {
        return userName;
    }

    private void sessionCloseProcess() {
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
