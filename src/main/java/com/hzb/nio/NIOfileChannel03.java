package com.hzb.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 这里将一个文件内容复制到另一个文件上
 */
public class NIOfileChannel03 {
    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("file.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("file02.txt");

        // 获取对应的channel
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true){
            // 这里将channel 里面是数据读取到byteBuffer
            // 这里需要清空一下数据
            /*
            public final Buffer clear() {
                position = 0;
                limit = capacity;
                mark = -1;
                return this;
            }
             */
            byteBuffer.clear();
            int read = inputStreamChannel.read(byteBuffer);
            if(read == -1){
                break;
            }
            // 这里转化成读数据
            byteBuffer.flip();
            // 这里将buffer 里面的数据读取到stream
            outputStreamChannel.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();

    }
}
