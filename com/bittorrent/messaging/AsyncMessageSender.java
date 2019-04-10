package com.bittorrent.messaging;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;
import com.bittorrent.handlers.PeerConnectionHandler;
import com.bittorrent.messaging.Message;

public class AsyncMessageSender implements Runnable {

    private String peerId;
    private PeerConnectionHandler peerConnectionHandler;

    public AsyncMessageSender(String peerId, PeerConnectionHandler peerConnectionHandler){
        this.peerId = peerId;
        this.peerConnectionHandler = peerConnectionHandler;
    }

    @Override
    public void run() {
        System.out.println("Async running "+BitTorrentState.getPeers().keySet().toString());
        PeerState peerState = BitTorrentState.getPeers().get(this.peerId);
        while (true) {
            Message message = null;
            try {
                message = peerState.getQueue().take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(peerId + ": Removed from queue " + message.getMessageType());
            peerConnectionHandler.sendMessage(message);
        }
    }
}
