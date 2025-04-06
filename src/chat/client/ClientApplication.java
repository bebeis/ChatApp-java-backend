package chat.client;

import static chat.util.MyLogger.log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientApplication {

    private final static int PORT = 1234;

    public static void main(String[] args) {

        try (Socket socket = new Socket();
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            socket.connect(new InetSocketAddress("localhost", PORT), 5000);
            log("소켓 연결: " + socket);

            while (true) {

            }

        } catch (IOException e) {
            log(e);
        }
    }
}
