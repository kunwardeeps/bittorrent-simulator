package com.bittorrent.main;

import com.bittorrent.dtos.PeerState;

import java.io.*;
import java.net.Socket;

public class PeerConnectionHandler implements Runnable{

    private Socket peerSocket = null;
    private PeerState peerState = null;
    private DataInputStream is;
    private DataOutputStream os;

    public PeerConnectionHandler(Socket peerSocket, PeerState peerState) {
        this.peerSocket = peerSocket;
        this.peerState = peerState;
    }

    @Override
    public void run() {
        try
        {
            is = new DataInputStream(peerSocket.getInputStream());
            os = new DataOutputStream(peerSocket.getOutputStream());

            sendMessage("Hello");
            String response = receiveMessage();
            System.out.println("Received message" + response);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public String receiveMessage() {
        try {
            return is.readUTF();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendMessage(String message) {
        System.out.println("Sending message" + message);
        try {
            os.writeUTF(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
