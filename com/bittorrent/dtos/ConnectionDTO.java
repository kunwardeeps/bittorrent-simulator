package com.bittorrent.dtos;

import com.bittorrent.main.ClientProcess;
import com.bittorrent.main.ServerProcess;

import java.net.Socket;

public class ConnectionDTO {

	private ServerProcess serverProcess;
	private ClientProcess clientProcess;
	private double bytesTransferred;
	private Socket peerSocket;
	private String remotePeerId;
	private boolean isConnectionChoked;

    public ServerProcess getServerProcess() {
        return serverProcess;
    }

    public void setServerProcess(ServerProcess serverProcess) {
        this.serverProcess = serverProcess;
    }

    public ClientProcess getClientProcess() {
        return clientProcess;
    }

    public void setClientProcess(ClientProcess clientProcess) {
        this.clientProcess = clientProcess;
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

    public ConnectionDTO(ServerProcess serverProcess, ClientProcess clientProcess, double bytesTransferred,
                         Socket peerSocket, String remotePeerId, boolean isConnectionChoked) {
        this.serverProcess = serverProcess;
        this.clientProcess = clientProcess;
        this.bytesTransferred = bytesTransferred;
        this.peerSocket = peerSocket;
        this.remotePeerId = remotePeerId;
        this.isConnectionChoked = isConnectionChoked;
    }

    @Override
    public String toString() {
        return "ConnectionDTO{" +
                "serverProcess=" + serverProcess +
                ", clientProcess=" + clientProcess +
                ", bytesTransferred=" + bytesTransferred +
                ", peerSocket=" + peerSocket +
                ", remotePeerId='" + remotePeerId + '\'' +
                ", isConnectionChoked=" + isConnectionChoked +
                '}';
    }
}
