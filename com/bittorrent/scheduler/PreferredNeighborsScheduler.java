package com.bittorrent.scheduler;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;
import com.bittorrent.messaging.ChokeMessage;
import com.bittorrent.messaging.UnchokeMessage;

import java.util.*;

public class PreferredNeighborsScheduler extends TimerTask {

    private String currentPeerId;
    private PriorityQueue<PeerState> maxHeap;

    public PreferredNeighborsScheduler(String currentPeerId) {
        this.currentPeerId = currentPeerId;
        this.maxHeap = new PriorityQueue<>(BitTorrentState.numberOfPreferredNeighbors,
                new Comparator<PeerState>() {
                    @Override
                    public int compare(PeerState peerState1, PeerState peerState2) {
                        return (int) (peerState2.getDataRate() - peerState1.getDataRate());
                    }
                });
    }

    @Override
    public void run() {
        System.out.println("PreferredNeighborsTask: start");

        maxHeap.clear();
        maxHeap.addAll(BitTorrentState.getPeers().values());
        System.out.println("PreferredNeighborsTask: maxHeapSize - "+maxHeap.size());

        PeerState currentPeerState = BitTorrentState.getPeers().get(this.currentPeerId);

        Map<String, String> oldPreferredNeighbours = new HashMap<>();
        oldPreferredNeighbours.putAll(currentPeerState.getPreferredNeighbours());

        Map<String, String> newPreferredNeighbours = new HashMap<>();
        for (int i = 0; i < BitTorrentState.numberOfPreferredNeighbors; i++) {
            String peerId = maxHeap.poll().getPeerId();
            if (currentPeerId.equals(peerId)) {
                i--;
                continue;
            }
            newPreferredNeighbours.put(peerId, peerId);
            if (!oldPreferredNeighbours.containsKey(peerId)){
                System.out.println(this.currentPeerId + ": sending UNCHOKE to "+peerId);
                BitTorrentState.getPeers().get(peerId).getQueue().add(new UnchokeMessage());
            }
        }
        for (String peerId: oldPreferredNeighbours.values()) {
            if (!newPreferredNeighbours.containsKey(peerId)) {
                System.out.println(this.currentPeerId + ": sending CHOKE to "+peerId);
                BitTorrentState.getPeers().get(peerId).getQueue().add(new ChokeMessage());
            }
        }
        currentPeerState.getPreferredNeighbours().clear();
        currentPeerState.getPreferredNeighbours().putAll(newPreferredNeighbours);
    }
}
