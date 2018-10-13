package com.iit;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;


public class LeafNode {
    ServerSocketChannel listener = null;
    public void sendfile()  {
        String host = "localhost";
        int port = 9026;
        SocketAddress sad = new InetSocketAddress(host, port);
        try{
        SocketChannel sc = SocketChannel.open();
        sc.connect(sad);
        sc.configureBlocking(true);

        String fname = "/Users/diesel/Desktop/a.txt";
        long fsize = Files.size(new File(fname).toPath());

        FileChannel fc = new FileInputStream(fname).getChannel();
        long start = System.currentTimeMillis();
        long curnset = 0;
        curnset =  fc.transferTo(0, fsize, sc);
        System.out.println("total bytes transferred--"+curnset+" and time taken in MS--"+(System.currentTimeMillis() - start));
        fc.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }
    protected void socketSetup()
    {
        InetSocketAddress listenAddr =  new InetSocketAddress(9026);

        try {
            listener = ServerSocketChannel.open();
            ServerSocket ss = listener.socket();
            ss.setReuseAddress(true);
            ss.bind(listenAddr);
            System.out.println("Listening on port : "+ listenAddr.toString());
        } catch (IOException e) {
            System.out.println("Failed to bind, is port : "+ listenAddr.toString()
                    + " already in use ? Error Msg : "+e.getMessage());
            e.printStackTrace();
        }

    }
    private void readData()  {
        ByteBuffer dst = ByteBuffer.allocate(4096);
        FileChannel channel;
        try {

            while(true) {
                System.out.println("Im executed");
                channel = new FileOutputStream("/Users/diesel/Desktop/copy.txt", true).getChannel();
                SocketChannel conn = listener.accept();
                System.out.println("Accepted : "+conn);
                conn.configureBlocking(true);
                int nread = 0;
                while (nread != -1)  {
                    try {
                        nread = conn.read(dst);
                        channel.write(dst);
                    } catch (IOException e) {
                        e.printStackTrace();
                        nread = -1;
                    }
                    dst.rewind();
                }
                channel.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void recievefile() {
        LeafNode dns = new LeafNode();
        dns.socketSetup();
        dns.readData();

    }
    public static void main(String[] args){
        if(args.length == 0){
            System.out.println("Argument moissing while calling leaf node");
        } else if( args[0].equals("send")){
            new LeafNode().sendfile();
        } else if( args[0].equals("recieve")){
            new LeafNode().recievefile();
        }
    }
}
