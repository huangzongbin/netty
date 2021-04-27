package com.hzb.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 *  都是通过一个 Buffer 完成的，NIO 还支持通过多个 Buffer（即 Buffer数组）完成读写操作，
 *  即 Scattering 和 Gathering
 *
 *  同时这里我们采用 serverSocketChannel
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {

        // 这里处于一个监听
        // 使用serverSocketChannel 和socketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定一个端口
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8 ;
        while(true){
            int byteRead = 0;
            while(byteRead<messageLength){
                // 将通道的数据读取缓冲区
                long read = socketChannel.read(byteBuffers);
                if(read == -1){
                    break;
                }
                byteRead+=read;
                System.out.println("byteRead="+byteRead);
                Arrays.stream(byteBuffers).map(buffer->"pasition="+buffer.position()+",limit="+buffer.limit()).forEach(System.out::println);

            }
            // 将所有的buffer 进行flip 转化成
            Arrays.stream(byteBuffers).forEach(stream -> stream.flip());
            // 将读取到数据写出到客户端
            long byteWrite = 0;
            while(byteWrite<messageLength){
                long length = socketChannel.write(byteBuffers);
                byteWrite= byteWrite+length;
            }
            // 将所有的buffer 进行clear
            Arrays.stream(byteBuffers).forEach(stream -> stream.clear());
            System.out.println("byteRead = " + byteRead + ", byteWrite = " + byteWrite + ", messagelength = " + messageLength);
        }
    }
}
