package com.bittorrent.main;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;
import com.bittorrent.handlers.IncomingConnectionHandler;
import com.bittorrent.handlers.PeerConnectionHandler;
import com.bittorrent.scheduler.OptimisticUnchokingScheduler;
import com.bittorrent.scheduler.PreferredNeighborsScheduler;
import com.bittorrent.utils.FileHandler;
import com.bittorrent.utils.Logger;

import java.net.Socket;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

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

		// Periodically select preferred neighbors for this peer
		Timer timer1 = new Timer();
		PreferredNeighborsScheduler preferredNeighborsScheduler = new PreferredNeighborsScheduler(peerState.getPeerId());
		timer1.scheduleAtFixedRate(preferredNeighborsScheduler, 500, BitTorrentState.getUnchokingInterval() * 1000);
		this.peerState.setTimer1(timer1);

		// Start OptimisticUnchokedPeerScheduler
		Timer timer2 = new Timer();
		OptimisticUnchokingScheduler optimisticUnchokingScheduler = new OptimisticUnchokingScheduler(peerState.getPeerId());
		timer2.scheduleAtFixedRate(optimisticUnchokingScheduler, 500, BitTorrentState.getOptimisticUnchokingInterval() * 1000);
		this.peerState.setTimer2(timer2);

		try {
			t.join();
			System.out.println("Exiting PeerProcessExecutor");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
