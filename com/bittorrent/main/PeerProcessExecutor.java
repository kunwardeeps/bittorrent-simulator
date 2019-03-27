package com.bittorrent.main;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.utils.Logger;

import java.io.IOException;

public class PeerProcessExecutor {
	public static String peerId;

	public PeerProcessExecutor(String peerId) {
		this.peerId = peerId;
	}

	public void init() {
		BitTorrentState.setStateFromConfigFiles();
		if (BitTorrentState.getPeer(peerId).isHasSharedFile()) {
			System.out.println("Shared file found with :"+ peerId);
		}
		System.out.println("Peer ID :"+ peerId);
		BitTorrentState.showConfiguration();
		Node current = Node.getInstance();
		// TODO Start execution
	}

}
