package main.server.command;

import static main.server.config.ServerConstant.SYSTEM;

import java.io.IOException;
import java.util.Arrays;
import main.server.config.ServerConstant;
import main.server.session.Session;

public class DefaultCommand implements Command {

    @Override
    public void execute(String[] args, Session session) throws IOException {
        session.send(SYSTEM, "처리할 수 없는 명령어 입니다: " + Arrays.toString(args));
    }
}
