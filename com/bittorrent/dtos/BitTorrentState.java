package com.bittorrent.dtos;

import com.bittorrent.main.PropertiesEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class BitTorrentState {

	public static int numberOfChunks;
	public static int numberOfPreferredNeighbors;
	public static int unchokingInterval;
	public static int optimisticUnchokingInterval;
	public static String fileName;
	public static long fileSize;
	public static int chunkSize;

	private static Map<String, PeerDTO> peers = new HashMap<>();

	public static PeerDTO getPeer(String id) {
		return peers.get(id);
	}

	public static Map<String, PeerDTO> getPeers() {
		return peers;
	}

	public static int numberOfPeers() {
		return peers.size();
	}

	public static int getNumberOfChunks() {
		return numberOfChunks;
	}

	public static void setNumberOfChunks(int numberOfChunks) {
		BitTorrentState.numberOfChunks = numberOfChunks;
	}

	public static int getNumberOfPreferredNeighbors() {
		return numberOfPreferredNeighbors;
	}

	public static void setNumberOfPreferredNeighbors(int numberOfPreferredNeighbors) {
		BitTorrentState.numberOfPreferredNeighbors = numberOfPreferredNeighbors;
	}

	public static int getUnchokingInterval() {
		return unchokingInterval;
	}

	public static void setUnchokingInterval(int unchokingInterval) {
		BitTorrentState.unchokingInterval = unchokingInterval;
	}

	public static int getOptimisticUnchokingInterval() {
		return optimisticUnchokingInterval;
	}

	public static void setOptimisticUnchokingInterval(int optimisticUnchokingInterval) {
		BitTorrentState.optimisticUnchokingInterval = optimisticUnchokingInterval;
	}

	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		BitTorrentState.fileName = fileName;
	}

	public static long getFileSize() {
		return fileSize;
	}

	public static void setFileSize(long fileSize) {
		BitTorrentState.fileSize = fileSize;
	}

	public static int getChunkSize() {
		return chunkSize;
	}

	public static void setChunkSize(int chunkSize) {
		BitTorrentState.chunkSize = chunkSize;
	}

	public static void setPeers(Map<String, PeerDTO> peers) {
		BitTorrentState.peers = peers;
	}

	public static void calculateAndSetNumberOfPieces() {
		if ((int) (fileSize % chunkSize) == 0) {
			numberOfChunks = (int) (fileSize / chunkSize) + 1;
		}
		else {
			numberOfChunks = (int) (fileSize / chunkSize);
		}
		System.out.println("BitTorrent current state - Number of pieces: " + numberOfChunks);
	}

	public static String getPeerLogFilePath() {
		return PropertiesEnum.PEER_LOG_FILE_PATH.getValue();
	}

	public static String getPeerLogFileExtension() {
		return PropertiesEnum.PEER_LOG_FILE_EXTENSION.getValue();
	}

	static {
		setStateFromConfigFiles();
		int networkId = 1;
		Scanner sc = null;
		try {
			sc = new Scanner(new File(PropertiesEnum.PEER_PROPERTIES_CONFIG_PATH.getValue()));
			while (sc.hasNextLine()) {
				String arr[] = sc.nextLine().split(" ");
				PeerDTO peer = new PeerDTO();
				peer.setNetworkId(networkId++);
				peer.setPeerId(arr[0]);
				peer.setHostName(arr[1]);
				peer.setPort(Integer.parseInt(arr[2]));
				if (arr[3].equals("1")) {
					peer.setHasSharedFile(true);
				}
				else {
					peer.setHasSharedFile(false);
				}
				peers.put(arr[0], peer);
			}
		} catch (IOException e) {
			System.out.println("PeerInfo.cfg missing");
		}
		finally {
			sc.close();
		}
	}

	public static void showSenderMessage(Object senderObj, String message){
		try {
			String sender = "";
			if (senderObj != null)
				sender = senderObj.toString();
			else
				sender = "Unknown";
			System.out.println("Sender " + sender + " Message : " + message);
		}
		catch (Exception ex){
			System.out.println(message);
		}
	}

	public static void showConfiguration() {
		System.out.println( "PeerProperties [numberOfPreferredNeighbors="
				+ numberOfPreferredNeighbors
				+ ", unchokingInterval="
				+ unchokingInterval
				+ ", optimisticUnchokingInterval="
				+ optimisticUnchokingInterval
				+ ", fileName="
				+ fileName
				+ ", fileSize="
				+ fileSize
				+ ", pieceSize="
				+ chunkSize
				+ "]");
	}

	public static void setStateFromConfigFiles() {

		Properties properties = new Properties();
		try {
			FileInputStream in = new FileInputStream(PropertiesEnum.COMMON_PROPERTIES_CONFIG_PATH.getValue());
			properties.load(in);
		}
		catch (Exception ex) {
			System.out.println("File not found : " + ex.getMessage());
		}

		fileName = properties.get(PropertiesEnum.FILENAME.getValue()).toString();
		fileSize = Long.parseLong(properties.get(PropertiesEnum.FILESIZE.getValue()).toString());
		numberOfPreferredNeighbors = 
				Integer.parseInt(properties.get(PropertiesEnum.NUMBER_OF_PREFERRED_NEIGHBORS.getValue()).toString());
		optimisticUnchokingInterval =
				Integer.parseInt(properties.get(PropertiesEnum.OPTIMISTIC_UNCHOKING_INTERVAL.getValue()).toString());
		chunkSize = Integer.parseInt(properties.getProperty(PropertiesEnum.PIECESIZE.getValue()).toString());
		unchokingInterval =
				Integer.parseInt(properties.getProperty(PropertiesEnum.UNCHOKING_INTERVAL.getValue()).toString());
		calculateAndSetNumberOfPieces();

		System.out.println(PropertiesEnum.PROPERTIES_FILE_PATH.getValue());
		System.out.println(PropertiesEnum.PROPERTIES_FILE_PATH.getValue() + BitTorrentState.fileName);

	}

}
