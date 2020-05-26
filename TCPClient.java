import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        int port = 7000;
        String host = "localhost";
        System.out.println("正在连接服务器。。。");
        Socket socket = new Socket(host, port);
        System.out.println("已连接服务器");
        new Thread(new Transmitter(socket)).start();
        new Thread(new Receiver(socket)).start();
    }

    static class Transmitter implements Runnable {
        String content;
        Scanner scanner = new Scanner(System.in);
        PrintWriter printWriter;
        Socket socket;

        Transmitter(Socket socket) throws IOException {
            this.socket = socket;
            this.printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        }

        @Override
        public void run() {
            try {
                do {
//                    System.out.print("请输入要发送的内容：");
                    content = scanner.nextLine();
                    printWriter.println(content);
                    printWriter.flush();
//                    System.out.println("已向服务器发送：" + content + ",长度：" + content.length());
                } while (!content.equals(""));
            } finally {
                try {
                    printWriter.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Receiver implements Runnable {
        Socket socket;
        BufferedReader bufferedReader;
        Receiver(Socket socket) throws IOException {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String content = bufferedReader.readLine();
                    System.out.println(content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bufferedReader.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
