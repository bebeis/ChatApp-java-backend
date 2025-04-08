package main.server.command;

import java.io.IOException;
import main.server.session.Session;

public class ExitCommand implements Command {

    @Override
    public void execute(String[] args, Session session) throws IOException {
        throw new IOException("exit");
    }
}
