package com.bittorrent.main;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerDTO;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Class that represents the individual peer in BitTorrent system
 */
public class Node {
	private static Node current = new Node();
	public static boolean FileReceived = false;
	private PeerDTO peerDTO;

	private Node() {
		peerDTO = BitTorrentState.getPeer(BitTorrentExecutor.peerId);
	}

	public PeerDTO getPeerDTO() {
		return peerDTO;
	}

	public static Node getInstance() {
		return current;
	}
	
	// Start monitoring incoming connections from user
	
	public void startConnection()  {
			
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(peerDTO.getPort());
			while (!FileReceived) {
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

		Map<String, PeerDTO> map = BitTorrentState.getPeers();
		int networkId = peerDTO.getNetworkId();
		for (String peerId : map.keySet()) {

			PeerDTO peerInfo = map.get(peerId);
			if (peerDTO.getNetworkId() < networkId) {

				new Thread() {
					@Override
					public void run() {

						createConnection(peerInfo);
					}
				}.start();

			}
		}
	}
		//Check if all peers received file
	private void checkpeersRcvdFile(){
		if(FileReceived){
			if(current!=null){
				System.out.println("all peers Have recieved file.");
			}
		}
	}

	private void createConnection(PeerDTO peerInfo) {
		int peerPort = peerInfo.getPort();
		String peerHost = peerInfo.getHostName();
		try {
			Socket clientSocket = new Socket(peerHost, peerPort);
			//TODO createConnection
			Thread.sleep(300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public void close(){
		try{
			checkpeersRcvdFile();
		}
		catch (Exception ex){
			System.out.println("Could not be verified : All peers had file.");
		}
	}
}
