package com.bittorrent.main;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;
import com.bittorrent.messaging.Message;
import com.bittorrent.utils.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class PeerProcessExecutor implements Runnable{
	private PeerState peerState;
	private Logger logger;

	public PeerProcessExecutor(String peerId) {
		BitTorrentState.setStateFromConfigFiles();
		this.peerState = BitTorrentState.getPeerState(peerId);
		this.logger = Logger.getLogger(peerId);
	}

	public void init() {
		FileHandler.makeFiles(this.peerState.getPeerId());
		if (peerState.isHasSharedFile()) {
			System.out.println("Shared file found with :"+ peerState.getPeerId());
			this.peerState.setFileSplitMap(FileHandler.splitFile());
		}
		else {
			this.peerState.setFileSplitMap(new ConcurrentHashMap<>());
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

		for (PeerState remotePeer : peers.values()) {

			if (currentSeqId > remotePeer.getSequenceId()) {

				try {
					logger.logTcpConnectionTo(this.peerState.getPeerId(), remotePeer.getPeerId());
					Socket clientSocket = new Socket(remotePeer.getHostName(), remotePeer.getPort());
					PeerConnectionHandler peerConnectionHandler = new PeerConnectionHandler(clientSocket, peerState);
					peerConnectionHandler.setRemotePeerId(remotePeer.getPeerId());
					Thread t = new Thread(peerConnectionHandler);
					t.start();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}


}
