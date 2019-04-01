package com.bittorrent.main;

import java.io.*;


public class HandshakeMessage implements Serializable {


    public String peerMessageHeader;
    public int peerId;


    public HandshakeMessage(int peerId) {
        super();
        this.peerId = peerId;
        this.peerMessageHeader = getHeader();
    }


    public int getPeerId() {
        return peerId;
    }

    public void setPeerId(int peerId) {
        this.peerId = peerId;
    }

    public String getPeerMessageHeader() {
        return peerMessageHeader;
    }

            // Header...Format?

        public static String getHeader()
        {
            return header;
        }


    public String toString() {
        StringBuilder s = new StringBuilder();
        return s.append("[Header :").append(getHeader()).append("]\n").append("[Peer ID: ").append(this.peerId).append("]")
                .toString();
    }

    public void SendHandShake(OutputStream out) throws IOException {
        ObjectOutputStream opStream = new ObjectOutputStream(out);
        opStream.writeObject(this);
        System.out.println("Send handshake message to peer " + this.peerId);
    }

    // return value could be changed to HandShakeMsg if header is also needed
    public int ReceiveHandShake(InputStream in) throws IOException {
        try {
            ObjectInputStream ipStream = new ObjectInputStream(in);
            HandshakeMessage Response = (HandshakeMessage) ipStream.readObject();
            return Response != null ? Response.peerId : -1;
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return -1;
    }

}
