package io.github.et.reminder.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerStream implements Runnable{
    public static ServerSocket socket=null;
    public static Socket socket_cli=null;
    public static PrintWriter pw=null;

    @Override
    public void run(){
        try {
            socket = new ServerSocket(5738);

            while (true) {
                socket_cli = socket.accept();
                pw=new PrintWriter(socket_cli.getOutputStream(), true);
            }
        } catch (IOException e){
            System.exit(0);
        }

    }
}