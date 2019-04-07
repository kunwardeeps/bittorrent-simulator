package com.bittorrent.messaging;

public class HandshakeMessage extends Message {

    private final String header = "P2PFILESHARINGPROJ";
    private final String ZERO_BITS = "0000000000";
    private String peerId;

    public HandshakeMessage(String peerId) {
        this.setMessageType(MessageType.HANDSHAKE);
        this.peerId = peerId;
    }

    public String toString(){
        return this.header + this.ZERO_BITS + this.peerId;
    }

    public String getPeerId(){
        return peerId;
    }

    public boolean validate(String peerId) {
        return header == "P2PFILESHARINGPROJ" && peerId == this.peerId;
    }
}
