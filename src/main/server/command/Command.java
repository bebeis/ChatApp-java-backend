package main.server.command;

import java.io.IOException;
import main.server.session.Session;

public interface Command {

    void execute(String[] args, Session session) throws IOException;
}
