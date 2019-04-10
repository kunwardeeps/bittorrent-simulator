package com.bittorrent.scheduler;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;
import com.bittorrent.messaging.ChokeMessage;
import com.bittorrent.messaging.UnchokeMessage;

import java.util.*;

public class OptimisticUnchokingScheduler extends TimerTask {

    private String currentPeerId;

    public OptimisticUnchokingScheduler(String currentPeerId) {
        this.currentPeerId = currentPeerId;
    }

    @Override
    public void run() {
        System.out.println("OptimisticUnchokingTask: start");
        PeerState currentPeerState = BitTorrentState.getPeers().get(currentPeerId);
        List<String> chokedNeighbours = new ArrayList<>();



        for (String peerId: BitTorrentState.getPeers().keySet()) {
            if (peerId.equals(currentPeerId)) {
                continue;
            }
            if (!currentPeerState.getPreferredNeighbours().containsKey(peerId)) {
                chokedNeighbours.add(peerId);
            }
        }
        if (chokedNeighbours.isEmpty()) {
            System.out.println("OptimisticUnchokingTask: No choked neighbors!");
            return;
        }
        Collections.shuffle(chokedNeighbours);
        String optimisticUnchokedPeerId = chokedNeighbours.get(0);
        BitTorrentState.getPeers().get(optimisticUnchokedPeerId).getQueue().add(new UnchokeMessage());

    }
}
