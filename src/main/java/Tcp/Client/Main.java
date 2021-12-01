package Tcp.Client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("usage: java -jar TcpClient.jar TargetIp Port FilePath");
            return;
        }

        try {
            TcpClient tcpClient = new TcpClient(args);
            tcpClient.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
