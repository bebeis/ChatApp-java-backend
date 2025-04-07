package main.server.command;
import static main.server.config.ServerConstant.SYSTEM;

import main.server.config.ServerConstant;
import java.io.IOException;
import java.util.Optional;
import main.server.session.Session;

public enum Command {

    JOIN("join") {
        @Override
        public void execute(Session session, String content) {
            session.registerUsername(content);
            session.sendMessage(SYSTEM, "등록된 이름: " + session.getUserName());
        }
    },
    MESSAGE("message") {
        @Override
        public void execute(Session session, String content) {
            if (session.getUserName() == null) {
                session.sendMessage(SYSTEM, ServerConstant.REQUEST_USERNAME_REGISTRATION_MESSAGE);
            } else {
                session.getSessionManager().sendMessageToAll(session.getUserName(), content);
            }
        }
    },
    CHANGE("change") {
        @Override
        public void execute(Session session, String content) {
            if (session.getUserName() == null) {
                session.sendMessage(SYSTEM, ServerConstant.REQUEST_USERNAME_REGISTRATION_MESSAGE);
            } else {
                session.changeUsername(content);
                session.sendMessage(SYSTEM, "변경된 이름: " + session.getUserName());
            }
        }
    },
    USERS("users") {
        @Override
        public void execute(Session session, String content) {
            StringBuilder sb = new StringBuilder();
            for (String username : session.getSessionManager().findConnectedUserAll()) {
                sb.append(username).append("\n");
            }
            session.sendMessage(SYSTEM, sb.toString());
        }
    },
    EXIT("exit") {
        @Override
        public void execute(Session session, String content) {
            session.sessionCloseProcess();
        }
    };

    private final String command;

    Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public abstract void execute(Session session, String content);

    public static Optional<Command> fromString(String str) {
        for (Command cmd : values()) {
            if (cmd.getCommand().equalsIgnoreCase(str)) {
                return Optional.of(cmd);
            }
        }
        return Optional.empty();
    }
}