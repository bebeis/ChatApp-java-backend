package main.server.session;

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

    public void sendAll(String userName, String message) {
        for (Session session : sessions) {
            session.send(userName, message);
        }
    }

    public List<String> findConnectedUserAll() {
        List<String> usernames = new ArrayList<>();
        for (Session session : sessions) {
            usernames.add(session.getUsername());
        }
        return usernames;
    }
}
