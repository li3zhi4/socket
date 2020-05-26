import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        int port = 7000;
        List<Socket> sockets = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            int clientNo = 0;
            while (true) {
                System.out.println("正在接受客户端连接请求。。。");
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                System.out.println("已接收到客户端" + clientNo);
                executorService.execute(new SingleServer(sockets, socket, clientNo));
                clientNo++;
            }
        }
    }

    static class SingleServer implements Runnable {
        private List<Socket> sockets;
        private Socket socket;
        private int clientNo;

        SingleServer(List<Socket> sockets, Socket socket, int clientNo) {
            this.sockets = sockets;
            this.socket = socket;
            this.clientNo = clientNo;
        }

        @Override
        public void run() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                String content;
                do {
                    System.out.println("正在接受客户端" + clientNo + "的数据。。。");
                    content = bufferedReader.readLine();
                    System.out.println("从客户端" + clientNo + "读到的内容：" + content + ",长度：" + content.length());
                    for (Socket socket:sockets){
                        try {
                            if (socket!=this.socket){
                                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                                printWriter.println(clientNo + ":" + content);
                                printWriter.flush();
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                } while (!content.equals(""));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    sockets.remove(socket);
                    System.out.println(clientNo+"已断开，已移除");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

