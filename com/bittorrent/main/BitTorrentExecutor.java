package com.bittorrent.main;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.utils.Logger;

import java.io.IOException;

public class BitTorrentExecutor {
	public static String peerId;

	public static void main(String args[]) throws IOException {
		if (args.length > 0) {
			peerId = args[0];
		}
		else {
			//default value
			peerId = "1001";
		}
		init();

	}

	private static void init() {
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
