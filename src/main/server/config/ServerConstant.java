package main.server.config;

import java.time.format.DateTimeFormatter;

public interface ServerConstant {
    public static final int PORT = 1234;
    public static final DateTimeFormatter MESSAGE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final String SYSTEM = "시스템";
    public static final String INVALID_COMMAND_FORMAT_MESSAGE = "잘못된 명령어 형식입니다";
    public static final String INVALID_COMMAND_MESSAGE = "잘못된 명령어입니다";
    public static final String REQUEST_USERNAME_REGISTRATION_MESSAGE = "이름을 입력해주세요";
    public static final String JOIN = "join";
    public static final String SEND_MESSAGE = "message";
    public static final String CHANGE_USERNAME = "change";
    public static final String FIND_ALL_USER = "users";
    public static final String CLOSE_CONNECTION = "exit";
}
