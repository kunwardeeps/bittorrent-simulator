package com.bittorrent.main;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;
import com.bittorrent.messaging.Message;

public class AsyncMessageSender implements Runnable {

    private PeerState peerState1;
    private PeerConnectionHandler peerConnectionHandler;

    public AsyncMessageSender(PeerState peerState1, PeerConnectionHandler peerConnectionHandler){
        this.peerState1 = peerState1;
        this.peerConnectionHandler = peerConnectionHandler;
    }

    @Override
    public void run() {
        System.out.println("Async running "+BitTorrentState.getPeers().keySet().toString());
        PeerState peerState = BitTorrentState.getPeers().get(peerState1.getPeerId());
        while (true) {
            if (!peerState.getQueue().isEmpty()){
                System.out.println("Removing from queue");
                Message message = peerState.getQueue().poll();
                peerConnectionHandler.sendMessage(message);
            }
        }
    }
}
