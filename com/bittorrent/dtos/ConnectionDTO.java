package com.bittorrent.dtos;

import com.bittorrent.main.Client;
import com.bittorrent.main.MainServer;

import java.net.Socket;

public class ConnectionDTO {

	private MainServer server;
	private Client client;
	private double bytesTransferred;
	private Socket peerSocket;
	private String remotePeerId;
	private boolean isConnectionChoked;

    public MainServer getServer() {
        return server;
    }

    public void setServer(MainServer server) {
        this.server = server;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getBytesTransferred() {
        return bytesTransferred;
    }

    public void setBytesTransferred(double bytesTransferred) {
        this.bytesTransferred = bytesTransferred;
    }

    public Socket getPeerSocket() {
        return peerSocket;
    }

    public void setPeerSocket(Socket peerSocket) {
        this.peerSocket = peerSocket;
    }

    public String getRemotePeerId() {
        return remotePeerId;
    }

    public void setRemotePeerId(String remotePeerId) {
        this.remotePeerId = remotePeerId;
    }

    public boolean isConnectionChoked() {
        return isConnectionChoked;
    }

    public void setConnectionChoked(boolean connectionChoked) {
        isConnectionChoked = connectionChoked;
    }

    public ConnectionDTO(MainServer server, Client client, double bytesTransferred,
                         Socket peerSocket, String remotePeerId, boolean isConnectionChoked) {
        this.server = server;
        this.client = client;
        this.bytesTransferred = bytesTransferred;
        this.peerSocket = peerSocket;
        this.remotePeerId = remotePeerId;
        this.isConnectionChoked = isConnectionChoked;
    }

    @Override
    public String toString() {
        return "ConnectionDTO{" +
                "server=" + server +
                ", client=" + client +
                ", bytesTransferred=" + bytesTransferred +
                ", peerSocket=" + peerSocket +
                ", remotePeerId='" + remotePeerId + '\'' +
                ", isConnectionChoked=" + isConnectionChoked +
                '}';
    }
}
