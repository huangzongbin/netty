package com.hzb.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {

        // 这里创建一个线程池
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        final ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动成功!");

        while(true){
            System.out.println("连接id="+Thread.currentThread().getId()+"名字："+Thread.currentThread().getName());
            // 监听 等待客户连接
            System.out.println("等待连接。。。。"); //如果没有连接成功会在这里等待。
            final Socket accept = serverSocket.accept();
            System.out.println("连接一个客户端");
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    // 处理客户端发送的数据；
                    handler(accept);
                }
            });
        }
    }

    // 编写一个handler方法和客户端通信
    public static void handler(Socket socket){
        try {
            System.out.println("连接id="+Thread.currentThread().getId()+"名字："+Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            // 循环读取输入数据
            while(true) {
                // 这里比较输入数据的线程和连接的线程是同一个。
                System.out.println("连接id="+Thread.currentThread().getId()+"名字："+Thread.currentThread().getName());

                System.out.println("等待数据发送。。。。"); // 链接成功了，没有数据发送会一直等待数据发送
                int read = inputStream.read(bytes);   // 将读取的数据存入到byte中，返回读取了多少个数据
                if (read != -1) { // 当不等于-1的时候说明还在读取
                    System.out.println(new String(bytes, 0, read));// 输出客户端发送的数据
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
                try {
                    socket.close();
                    System.out.println("关闭链接！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
