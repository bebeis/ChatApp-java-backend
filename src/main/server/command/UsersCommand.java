package main.server.command;

import static main.server.config.ServerConstant.*;

import java.io.IOException;
import java.util.List;
import main.server.session.Session;
import main.server.session.SessionManager;

public class UsersCommand implements Command {

    private final SessionManager sessionManager;

    public UsersCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(String[] args, Session session) throws IOException {
        List<String> usernames = sessionManager.findConnectedUserAll();
        StringBuilder sb = new StringBuilder();
        sb.append("전체 접속자 : ").append(usernames.size()).append("\n");
        for (String username : usernames) {
            sb.append(" - ").append(username).append("\n");
        }
        session.send(SYSTEM, sb.toString());
    }
}
