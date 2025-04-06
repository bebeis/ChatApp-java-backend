package main.client;

import static main.util.MyLogger.log;

import java.io.DataInputStream;
import java.io.IOException;

public class ReceiveTask implements Runnable {

    private final DataInputStream input;

    public ReceiveTask(DataInputStream input) {
        this.input = input;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String receivedMessage = input.readUTF();
                System.out.println("[수신] " + receivedMessage);
            }
        } catch (IOException e) {
            log(e);
        }
    }
}
