package com.bittorrent.scheduler;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;
import com.bittorrent.messaging.ChokeMessage;
import com.bittorrent.messaging.UnchokeMessage;
import com.bittorrent.utils.Logger;

import java.util.*;

public class PreferredNeighborsScheduler extends TimerTask {
    private PeerState currentPeerState;
    private PriorityQueue<PeerState> maxHeap;

    public PreferredNeighborsScheduler(PeerState currentPeerState) {
        this.currentPeerState = currentPeerState;
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
        System.out.println("PreferredNeighborsTask " + this.currentPeerState.getPeerId() + ": interested neighbors - " +
                this.currentPeerState.getInterestedNeighbours().values());

        if (currentPeerState.getInterestedNeighbours().isEmpty()) {
            System.out.println("PreferredNeighborsTask: No interested neighbors for " + this.currentPeerState.getPeerId());
            return;
        }

        maxHeap.clear();
        for (String interestedNeighbor: currentPeerState.getInterestedNeighbours().values()) {
            maxHeap.add(BitTorrentState.getPeers().get(interestedNeighbor));
        }
        System.out.println("PreferredNeighborsTask: maxHeapSize - "+maxHeap.size());



        Map<String, String> oldPreferredNeighbours = new HashMap<>();
        oldPreferredNeighbours.putAll(currentPeerState.getPreferredNeighbours());

        Map<String, String> newPreferredNeighbours = new HashMap<>();
        for (int i = 0; i < BitTorrentState.getNumberOfPreferredNeighbors(); i++) {
            if (maxHeap.size() > 0) {
                String peerId = maxHeap.poll().getPeerId();
                if (currentPeerState.getPeerId().equals(peerId)) {
                    // this should not happen
                    i--;
                    continue;
                }
                newPreferredNeighbours.put(peerId, peerId);
                if (!oldPreferredNeighbours.containsKey(peerId)) {
                    //System.out.println(this.currentPeerId + ": sending UNCHOKE to "+peerId);
                    if (currentPeerState.getConnections().size() > 0) {
                        currentPeerState.getConnections().get(peerId).sendMessage(new UnchokeMessage());
                    }
                }
            }
            else {
                System.out.println("maxHeap empty");
            }
        }
        for (String peerId: oldPreferredNeighbours.values()) {
            if (currentPeerState.getOptimisticUnchokedPeerId() != null &&
                currentPeerState.getOptimisticUnchokedPeerId().equals(peerId)) {
                continue;
            }
            if (!newPreferredNeighbours.containsKey(peerId)) {
                //System.out.println(this.currentPeerId + ": sending CHOKE to "+peerId);
                if (currentPeerState.getConnections().size() > 0) {
                    currentPeerState.getConnections().get(peerId).sendMessage(new ChokeMessage());
                }
            }
        }
        currentPeerState.getPreferredNeighbours().clear();
        currentPeerState.getPreferredNeighbours().putAll(newPreferredNeighbours);
        Logger.getLogger(this.currentPeerState.getPeerId()).logChangePreferredNeighbors(newPreferredNeighbours);
    }
}
