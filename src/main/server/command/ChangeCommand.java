package main.server.command;

import static main.server.config.ServerConstant.SYSTEM;

import java.io.IOException;
import main.server.config.ServerConstant;
import main.server.session.Session;
import main.server.session.SessionManager;

public class ChangeCommand implements Command {

    private final SessionManager sessionManager;

    public ChangeCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(String[] args, Session session) throws IOException {
        String changeName = args[1];
        sessionManager.sendAll(SYSTEM, session.getUsername() + "님이 " + changeName +"로 이름을 변경했습니다.");
        session.setUsername(changeName);
    }
}
