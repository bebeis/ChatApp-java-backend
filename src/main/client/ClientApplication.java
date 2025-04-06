package main.client;

import static main.util.MyLogger.log;

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

            Thread thread = new Thread(new ReceiveTask(input), "Receive");
            thread.start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String command = scanner.nextLine();
                output.writeUTF(command);
            }

        } catch (IOException e) {
            log(e);
        }
    }
}
