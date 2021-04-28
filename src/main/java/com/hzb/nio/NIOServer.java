package com.hzb.nio;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws IOException {

        // 创建一个ServerSocketChannel   管理多个ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个Selector
        Selector selector = Selector.open();

        // 绑定一个端口6666 在服务器监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //这里将 serverSocketChannel 注册到 selector ,同时关心事件设置成 op_accept（链接事件）   这个是serverSocketChannel作为全局的管理 socket 同样也要注册到select ,
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("注册后的selectctionkey 数量= "+selector.keys().size());


        while(true){
            // selector 等待时长为1秒钟，如果没有时间发生就返回。
            if(selector.select(1000) == 0){
                System.out.println("服务器等待了1秒，无法连接");
                continue;
            }

            // selector.selectedKeys();返回关注事件的集合，大于0 表示获取关注的事件  直接获取keys,一个key对应一个channel
            //
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys 数量 = " + selectionKeys.size());

            // 获取遍历的迭代器
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();

                if(key.isAcceptable()){ // 如果是op_accept（链接）  代表 有新客户进行链接
                    // 当前客户端生产一个SocketChannel，
                    SocketChannel socketChannel = serverSocketChannel.accept(); // 虽然这个传统的acept() 是一个阻塞的方法，但是这里是事件驱动的，这个正是要进行链接的。
                    System.out.println("客户端连接成功 生成了一个socketChannel"+socketChannel.hashCode());

                    // 将 socketChannel 设置成非阻塞状态
                    socketChannel.configureBlocking(false);
                    // 将socketeChannl 注册到 Selector ，针对于这个channel 我们就是关心事件为 OP_READ（读事件）
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    System.out.println("客户端连接后 ，注册的selectionkey 数量=" + selector.keys().size()); //
                }

                if(key.isReadable()) {  //发生 OP_READ

                    //通过key 反向获取到对应channel
                    SocketChannel channel = (SocketChannel)key.channel();

                    //获取到该channel关联的buffer， attachment
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    channel.read(buffer);
                    System.out.println("form 客户端 " + new String(buffer.array()));
                }
                // 手动从集合中移动到当前的selectKey ,防止重复操作
               keyIterator.remove();
            }


        }



    }
}
