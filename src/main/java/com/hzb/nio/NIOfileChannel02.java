package com.hzb.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *  这里我们将上面写入到文件中的数据，显示到控制台上面来
 */
public class NIOfileChannel02 {
    public static void main(String[] args) throws IOException {
        File file = new File("file.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        // 获取一个通道用于读取缓冲区里面的数据
        FileChannel fileChannel = fileInputStream.getChannel();
        // 这里创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate((int)file.length());

        // 将通道里面的数据读入到buffer上
        fileChannel.read(buffer);
        // 这里把数据读取懂buffer
        System.out.println(new String(buffer.array()));
        fileInputStream.close();

    }
}
