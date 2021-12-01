package Tcp.Client;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpClient {

    private static String HOST = null;
    private static int PORT = 0;
    private static String FILEPATH = null;
    private FileRead fileRead;

    public TcpClient(String[] args) throws IOException {
        HOST = args[0];
        PORT = Integer.parseInt(args[1]) ;
        FILEPATH = args[2];
    }

    public void start() {
        fileRead = new FileRead(FILEPATH);
        if (fileRead.fileExists()) {
            send();
        } else {
            System.out.println("[" + FILEPATH + "] 해당 파일이나 디렉토리가 없습니다");
        }
    }

    public void send() {
        String message = fileRead.getMessage();

        try (Socket socket = new Socket()){

            socket.connect(new InetSocketAddress(HOST, PORT));
            byte[] bytes = null;

            OutputStream outputStream = socket.getOutputStream();
            bytes = message.getBytes("EUC-KR");
            outputStream.write(bytes);
            outputStream.flush();
            System.out.println("[송신 데이터]");
            System.out.println(message);

            InputStream inputStream = socket.getInputStream();
            bytes = new byte[100];
            int readByteCount = inputStream.read(bytes);
            message = new String(bytes, 0, readByteCount, "EUC-KR");
            System.out.println("[수신 데이터]");
            System.out.println(message);

            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
