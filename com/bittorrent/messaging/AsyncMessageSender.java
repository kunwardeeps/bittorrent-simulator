package com.bittorrent.messaging;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;
import com.bittorrent.handlers.PeerConnectionHandler;
import com.bittorrent.messaging.Message;

import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncMessageSender implements Runnable {

    private String peerId;
    private AtomicBoolean running = new AtomicBoolean(false);

    public AsyncMessageSender(String peerId){
        this.peerId = peerId;
    }

    @Override
    public void run() {
        try {
            running.set(true);
            System.out.println("Async running "+BitTorrentState.getPeers().keySet().toString());
            PeerState peerState = BitTorrentState.getPeers().get(this.peerId);
            while (running.get()) {
                Message message = peerState.getQueue().take();
                System.out.println(peerId + ": Removed from queue " + message.getMessageType());
                //peerConnectionHandler.sendMessage(message);
            }
        } catch (InterruptedException e) {
            System.out.println("Ending AsyncMessageSender");
        }
    }

    public void stop() {
        this.running.set(false);
    }
}
