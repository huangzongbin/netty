package com.hzb.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 简单的案例
 *  把数据写出到一个文件中
 */
public class NIOfileChannel01 {

    public static void main(String[] args) throws IOException {

        String str = "hello 黄宗滨";
        FileOutputStream outputStream = new FileOutputStream("file.txt");

        // 获取一个fileChannel
        FileChannel fileChannel = outputStream.getChannel();

        // 创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        buffer.put(str.getBytes());
        buffer.flip();
        // 重缓冲区中读取到channel
        fileChannel.write(buffer);
        // 关闭流
        outputStream.close();
        System.out.println("文件生产成功！");


    }
}
