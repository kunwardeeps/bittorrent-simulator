package com.bittorrent.handlers;

import com.bittorrent.dtos.PeerState;

import java.net.ServerSocket;
import java.net.Socket;

public class IncomingConnectionHandler implements Runnable{

    private Socket clientSocket;
    private PeerState peerState;

    public IncomingConnectionHandler(PeerState peerState) {
        this.peerState = peerState;
    }

    @Override
    public void run() {
        while(true)
        {
            try(ServerSocket serverSocket = new ServerSocket(peerState.getPort()))
            {
                System.out.println("Peer Id " + peerState.getPeerId() + " accepting connections");
                clientSocket = serverSocket.accept();
                System.out.println("Connection is established to " + peerState.getPort() + " from " + clientSocket.getRemoteSocketAddress());
                Thread t = new Thread(new PeerConnectionHandler(clientSocket, peerState));
                t.start();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
