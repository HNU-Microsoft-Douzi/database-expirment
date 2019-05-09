/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: GBNServer
 * Author:   Administrator
 * Date:     2019/5/6 0006 21:06
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * 服务器端
 */
public class GBNServer {
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    public GBNServer() throws IOException {

        try {
            int port = 80;
            datagramSocket = new DatagramSocket(port);
            int exceptedSeq = 1;
            while (true) {
                byte[] receivedData = new byte[4096];
                datagramPacket = new DatagramPacket(receivedData, receivedData.length);
                datagramSocket.receive(datagramPacket);
                //收到的数据
                String received = new String(receivedData, 0, receivedData.length);//offset是初始偏移量
                System.out.println(received);
                //收到了预期的数据
                if (Integer.parseInt(received.substring(received.indexOf("编号:") + 3).trim()) == exceptedSeq) {
                    //发送ack
                    sendAck(exceptedSeq);
                    System.out.println("服务端期待的数据编号:" + exceptedSeq);
                    //期待值加1
                    exceptedSeq++;
                    System.out.println('\n');
                } else {
                    System.out.println("服务端期待的数据编号:" + exceptedSeq);
                    System.out.println("+++++++++++++++++++++未收到预期数据+++++++++++++++++++++");
                    // 仍发送之前的ack
                    sendAck(exceptedSeq - 1);
                    System.out.println('\n');
                }
            }
        }catch(SocketException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new GBNServer();
    }

    //向客户端发送ack
    private void sendAck(int ack) throws IOException {
        String response = " ack:"+ack;
        byte[] responseData = response.getBytes();
        InetAddress responseAddress = datagramPacket.getAddress();
        int responsePort = datagramPacket.getPort();
        datagramPacket = new DatagramPacket(responseData,responseData.length,responseAddress,responsePort);
        datagramSocket.send(datagramPacket);
    }
}