package main.server.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    private List<Session> sessions = new ArrayList<>();

    public synchronized void add(Session session) {
        sessions.add(session);
    }

    public synchronized void remove(Session session) {
        sessions.remove(session);
    }

    public synchronized void closeAll() {
        for (Session session : sessions) {
            session.close();
        }
        sessions.clear();
    }

    public void sendMessageToAll(String userName, String message) throws IOException {
        for (Session session : sessions) {
            session.sendMessage(userName, message);
        }
    }

    public List<String> findConnectedUserAll() {
        List<String> usernames = new ArrayList<>();
        for (Session session : sessions) {
            usernames.add(session.getUserName());
        }
        return usernames;
    }
}
