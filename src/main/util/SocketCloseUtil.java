package main.util;

import static main.util.MyLogger.log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class SocketCloseUtil {

    public static void closeAll(Socket socket, InputStream inputStream, OutputStream outputStream) {
        close(inputStream);
        close(outputStream);
        close(socket);
    }

    private static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                log(e.getMessage());
            }
        }
    }

    private static void close(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                log(e.getMessage());
            }
        }
    }

    private static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                log(e.getMessage());
            }
        }
    }
}
