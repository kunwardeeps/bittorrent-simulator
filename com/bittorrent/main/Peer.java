package com.bittorrent.main;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;

import java.net.Socket;
import java.util.Map;

/**
 * Class that represents the individual peer in BitTorrent system
 */
public class Peer {

	private PeerState peerState;
	private static Peer peerInstance = null;

	public static Peer getInstance() {
	    if (peerInstance == null) {
            peerInstance = new Peer();
        }
	    return peerInstance;
    }

	private Peer() {
		//peerState = BitTorrentState.getPeerState(PeerProcessExecutor.peerId);
	}

	public PeerState getPeerState() {
	    return peerState;
    }



	private void createConnection(PeerState peerState) {
		int peerPort = peerState.getPort();
		String peerHost = peerState.getHostName();
		try {
			Socket clientSocket = new Socket(peerHost, peerPort);
			//TODO createConnection
			Thread.sleep(300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
