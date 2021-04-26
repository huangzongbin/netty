package com.hzb.nio;

import java.nio.IntBuffer;

/**
 * 这里是一个buffer案例，对于每一个基本类型都存在一种buffer
 */
public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i);
        }
        // 进行从写到读的切换
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
