package main.server.command;


import static main.server.config.ServerConstant.SYSTEM;

import main.server.config.ServerConstant;
import main.server.session.Session;
import main.server.session.SessionManager;

public class JoinCommand implements Command {

    private final SessionManager sessionManager;

    public JoinCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(String[] args, Session session) {
        String username = args[1];
        session.setUsername(username);
        sessionManager.sendAll(SYSTEM, username + "님이 입장했습니다.");
    }
}
