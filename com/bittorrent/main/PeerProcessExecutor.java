package com.bittorrent.main;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;
import com.bittorrent.utils.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class PeerProcessExecutor implements Runnable{
	private PeerState peerState;

	public PeerProcessExecutor(String peerId) {
		BitTorrentState.setStateFromConfigFiles();
		this.peerState = BitTorrentState.getPeerState(peerId);
	}

	public void init() {
		if (peerState.isHasSharedFile()) {
			System.out.println("Shared file found with :"+ peerState.getPeerId());
		}
		System.out.println("Peer ID :"+ peerState.getPeerId());
		BitTorrentState.showConfiguration();
		System.out.println(peerState);

		// accept incoming connections
		Thread t = new Thread(new IncomingConnectionHandler(peerState));
		t.start();

		// create outgoing connections
		createOutgoingConnections();
	}

	public void run() {
		init();
	}

	public void createOutgoingConnections() {

		Map<String, PeerState> peers = BitTorrentState.getPeers();

		int currentSeqId = this.peerState.getSequenceId();

		for (PeerState existingPeer : peers.values()) {

			if (currentSeqId > existingPeer.getSequenceId()) {

				try {
					Socket clientSocket = new Socket(existingPeer.getHostName(), existingPeer.getPort());
					Thread t = new Thread(new PeerConnectionHandler(clientSocket, peerState));
					t.start();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}


}
