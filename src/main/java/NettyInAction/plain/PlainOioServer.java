package NettyInAction.plain;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class PlainOioServer {
    public void serve(int port) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);
        try {
            for (;;) {
                final Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);

                // 연결을 처리할 새로운 스레드를 생성
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream outputStream;
                        try {
                            outputStream = clientSocket.getOutputStream();
                            // 연결된 클라이언트로 메시지를 출력
                            outputStream.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                            outputStream.flush();
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException ex) {

                            }
                        }
                    }
                }).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
