package com.bittorrent.main;

import com.bittorrent.dtos.PeerState;
import com.bittorrent.messaging.HandshakeMessage;
import com.bittorrent.messaging.Message;

import java.io.*;
import java.net.Socket;

public class PeerConnectionHandler implements Runnable{

    private Socket peerSocket = null;
    private PeerState peerState = null;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    public PeerConnectionHandler(Socket peerSocket, PeerState peerState) {
        this.peerSocket = peerSocket;
        this.peerState = peerState;
    }

    @Override
    public void run() {
        try
        {
            os = new ObjectOutputStream(peerSocket.getOutputStream());

            sendMessage(new HandshakeMessage(this.peerState.getPeerId()));

            Message response = null;
            while ((response = receiveMessage()) != null) {
                switch (response.getMessageType()) {
                    case HANDSHAKE: {
                        System.out.println("Received message type: " + response.getMessageType().name() + ", message: " + response.toString());
                        break;
                    }
                    default:
                        System.out.println("Not implemented!");
                }
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public Message receiveMessage() {
        try {
            is = new ObjectInputStream(peerSocket.getInputStream());
            return (Message) is.readObject();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void sendMessage(Message message) {
        System.out.println("Sending message: " + message.toString());
        try {
            os.writeObject(message);
            os.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
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
