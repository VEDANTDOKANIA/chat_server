package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;




public class Server {
    private ServerSocket serverSocket ;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startserver(){
        try{

            while(!(serverSocket.isClosed()))
            {
                Socket socket = serverSocket.accept();
                System.out.println("New Client Connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(5555);
        Server server = new Server(serverSocket);
        server.startserver();
    }
}
