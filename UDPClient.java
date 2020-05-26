import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPClient {


    public static void main(String[] args) throws IOException {
        int number = 5;
        int timeout = 5000;
        int portReceiver = 7001;
        int portTransmitter = 7002;
        byte[] buf = new byte[1024];
        DatagramSocket datagramSocket = new DatagramSocket(portReceiver);
        String content = "hello server";
        InetAddress localHost = InetAddress.getLocalHost();
        DatagramPacket datagramPacketTransmitter = new DatagramPacket(content.getBytes(StandardCharsets.UTF_8), content.length(), localHost, portTransmitter);
        DatagramPacket datagramPacketReceiver = new DatagramPacket(buf, 1024);
        datagramSocket.setSoTimeout(timeout);
        int tries = 0;
        boolean receivedResponse = false;
        InetAddress address = null;
        while (!receivedResponse && tries < number){
            datagramSocket.send(datagramPacketTransmitter);
            try {
                datagramSocket.receive(datagramPacketReceiver);
                address = datagramPacketReceiver.getAddress();
                if (!address.equals(localHost)){
                    throw new IOException("接收到未知源的信息");
                }
                receivedResponse = true;
            }catch (IOException e){
                tries ++;
                e.printStackTrace();
            }
        }
        if (receivedResponse){
            String content2 = new String(datagramPacketReceiver.getData(), 0, datagramPacketReceiver.getLength());
            int port = datagramPacketReceiver.getPort();
            System.out.println(address+":"+port+":"+content2);
            datagramPacketReceiver.setLength(1024);
        }else {
            System.out.println("未获得信息");
        }
        datagramSocket.close();
    }
}
