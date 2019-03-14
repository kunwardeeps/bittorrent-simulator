package com.bittorent.main;

import Connection.MainModule.*;
import java.net.ServerSocket;
import Common.MainModule.CommonProperties;
import java.util.HashMap;
import java.net.Socket;

public class Node {
	private static Node current = new Node();
	public static boolean FileReceived = false;
	private NetworkModel networkModel;
	ConnectionController connectionController;

	private Node() {
		networkModel = CommonProperties.getPeer(BitTorrentMainController.peerId);
		connectionController = ConnectionController.getInstance();
	}

	public NetworkModel getNetwork() {
		return networkModel;
	}

	public static Node getInstance() {
		return current;
	}
	
	// Start monitoring incoming connections from user
	
	public void startConnection()  {
			
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(networkModel.port);
			while (!FileReceived) {
				Socket peerSocket = socket.accept();
				connectionController.createConnection(peerSocket);
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
		HashMap<String, NetworkModel> map = CommonProperties.getPeerList();
		int myNumber = networkModel.networkId;
		for (String peerId : map.keySet()) {
			NetworkModel peerInfo = map.get(peerId);
			if (peerInfo.networkId < myNumber) {
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
				CommonProperties.DisplayMessageForUser(null, "all peers Have recieved file.");
			}
		}
	}

	private void createConnection(NetworkModel peerInfo) {
		int peerPort = peerInfo.port;
		String peerHost = peerInfo.hostName;
		try {
			Socket clientSocket = new Socket(peerHost, peerPort);
			connectionController.createConnection(clientSocket, peerInfo.getPeerId());
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
