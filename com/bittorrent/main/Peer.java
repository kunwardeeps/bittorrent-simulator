package com.bittorrent.main;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;

import java.net.ServerSocket;
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
		peerState = BitTorrentState.getPeer(PeerProcessExecutor.peerId);
	}

	public PeerState getPeerState() {
	    return peerState;
    }
	
	// Start monitoring incoming connections from user
	
	public void startConnection()  {
			
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(peerState.getPort());
			while (!peerState.isFileReceived()) {
				Socket peerSocket = socket.accept();
			}
		}
		catch (Exception e) {
			System.out.println("Closed exception");
		}
		finally {
			try{
				socket.close();
			}
			catch (Exception e) {
				System.out.println("Closed exception");
				e.printStackTrace();
			}
		}
	}
		// Start Outgoing Connections
	public void startOutgoingConnection() {

		Map<String, PeerState> map = BitTorrentState.getPeers();
		int networkId = peerState.getNetworkId();
		for (String peerId : map.keySet()) {

			PeerState peerState = map.get(peerId);
			if (this.peerState.getNetworkId() < networkId) {

				new Thread() {
					@Override
					public void run() {
						createConnection(peerState);
					}
				}.start();

			}
		}
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
