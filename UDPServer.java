import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) {
        String content = "hello client";
        byte[] buf = new byte[1024];
        int portReceiver = 7002;
        int portTransmitter = 7001;
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(portReceiver);
            DatagramPacket datagramPacketReceiver = new DatagramPacket(buf,1024);
            while (true){
                datagramSocket.receive(datagramPacketReceiver);
                String content2 = new String(datagramPacketReceiver.getData(), 0, datagramPacketReceiver.getLength());
                String hostAddress = datagramPacketReceiver.getAddress().getHostAddress();
                int port = datagramPacketReceiver.getPort();
                System.out.println(hostAddress+":"+port+":"+content2);
                DatagramPacket datagramPacketTransmitter = new DatagramPacket(content.getBytes(),content.length(),datagramPacketReceiver.getAddress(),portTransmitter);
                datagramSocket.send(datagramPacketTransmitter);
                datagramPacketReceiver.setLength(1024);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            assert datagramSocket != null;
            datagramSocket.close();
        }

    }
}
