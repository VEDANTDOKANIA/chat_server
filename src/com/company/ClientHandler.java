package com.company;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw ;
    private String clientusername ;

    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bw= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())) ;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientusername = br.readLine();
            clientHandlers.add(this);
        } catch (Exception e) {
            CloseEverything(socket,br,bw);
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        String messagefromclient ;
        String options = null;
        String uniqueid = null;
        try {
            options = br.readLine();
            if(options.equals("Unique_id_available"))
            {
                 uniqueid = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (socket.isConnected()){
            try{
                //System.out.println(options);
                messagefromclient = br.readLine();
                if(options.equals("Unique_id_available"))
                {
                    System.out.println(uniqueid);
                    personalmessage(uniqueid,messagefromclient);
                }
                else if(options.equals("unique_id_notavailable"))
                {
                        Broadcastmessage(messagefromclient);
                }



            }
            catch (Exception e)
            {
                CloseEverything(socket,br,bw);
                break;

            }
        }

    }



    private void Broadcastmessage(String message) {
        for(ClientHandler clientHandler : clientHandlers)
        {
            try{
                if(!(clientHandler.clientusername.equals(clientusername)))
                {
                    clientHandler.bw.write("Broadcast:" + message);
                    clientHandler.bw.newLine();
                    clientHandler.bw.flush();
                }
            } catch (Exception e) {
                CloseEverything(socket ,br,bw);
                e.printStackTrace();
            }
        }

    }

    private void personalmessage(String uniquename , String message)
    {

        for(ClientHandler clientHandler : clientHandlers)
        {
           // System.out.println(clientHandlers);
            //System.out.println(clientHandler.clientusername);
           // System.out.println(message);
            try{
                if((clientHandler.clientusername.equals(uniquename)))
                {
                    clientHandler.bw.write(message);
                    clientHandler.bw.newLine();
                    clientHandler.bw.flush();
                }
            } catch (Exception e) {
                CloseEverything(socket ,br,bw);
                e.printStackTrace();
            }
        }

    }

    public void removeClienthandler(){
        clientHandlers.remove(this);
        Broadcastmessage("Server :" + clientusername + " has left the chat");
    }

    private void CloseEverything(Socket socket, BufferedReader br, BufferedWriter bw) {
        removeClienthandler();
        try {
            if(br!= null)
            {
                br.close();
            }
            if(bw!= null)
            {
                bw.close();
            }
            if(socket!= null)
            {
                socket.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
