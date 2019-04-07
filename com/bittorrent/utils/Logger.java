package com.bittorrent.utils;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.ConnectionDTO;
import com.bittorrent.dtos.PeerState;
import com.bittorrent.main.Peer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/***
 * Logger utility class designed as Singleton to
 * avoid concurrency issues
 */
public class Logger {

	private static Map<String, Logger> map = new HashMap<>();

	public PrintWriter printWriter = null;

	public static Logger getLogger(String peerId) {
		synchronized (Logger.class) {
			if (map.get(peerId) == null) {
				map.put(peerId, new Logger(peerId));
			}
		}
		return map.get(peerId);
	}

	/**
	 * Constructor: Creates directories for logging
	 * and initializes PrintWriter
	 */
	private Logger(String peerId) {
		try {
			System.out.println("Logger instantiated for peer: "
					+ peerId);
			File file = makeLogDirectoryForPeer(peerId);
			initPrintWriter(file);
		}
		catch (Exception ex) {
			System.out.println("Exception "+ ex.getMessage());
		}
	}

	private File makeLogDirectoryForPeer(String peerId) throws Exception{

		String path = BitTorrentState.getPeerLogFilePath() + peerId
				+ BitTorrentState.getPeerLogFileExtension();

		File file = new File(path);
		file.getParentFile().mkdirs();

		return file;
	}

	private void initPrintWriter(File file) throws IOException{

		file.createNewFile();
		FileOutputStream fileOutputStream = new FileOutputStream(file, false);
		printWriter = new PrintWriter(fileOutputStream, true);
	}

	private String getTimeStamp() {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp.toString();
	}

	private void writeFile(String message) {

		synchronized (this) {
			printWriter.println(message);
		}
	}

	public void logReceivedHaveMessage(String toId, String fromId, int pieceIndex) {

		writeFile(getTimeStamp()
				+ ": Peer "
				+ toId
				+ " received the 'have' messaging from "
				+ fromId
				+ " for the piece "
				+ pieceIndex + ".");
	}



	public void logTcpConnectionTo(String fromId, String toId) {
		writeFile(getTimeStamp()
				+ ": Peer "
				+ fromId
				+ " makes a connection to Peer "
				+ toId
				+ ".");
	}

	public void logTcpConnectionFrom(String fromId, String toId) {
		writeFile(getTimeStamp()
				+ ": Peer "
				+ toId
				+ " is connected from Peer "
				+ fromId
				+ ".");
	}

	public void logChangePreferredNeighbors(String peerId, List<ConnectionDTO> prefNeighbors) {

		StringBuilder message = new StringBuilder();
		message.append(getTimeStamp());
		message.append(": Peer ");
		message.append(peerId);
		message.append(" has changed the preferred neighbors ");
		String separator = "";
		Iterator<ConnectionDTO> iter = prefNeighbors.iterator();

		while (iter.hasNext()) {

			message.append(separator);
			separator = ", ";
			message.append(iter.next().getRemotePeerId());

		}
		writeFile(message.toString() + ".");
	}


	public void logNewOptimisticallyUnchokedNeighbor(String source, String unchokedNeighbor) {

		writeFile(getTimeStamp()
				+ ": Peer "
				+ source
				+ " has the optimistically unchoked neighbor "
				+ unchokedNeighbor
				+ ".");
	}

	public void logUnchokingEvent(String peerId1, String peerId2) {

		writeFile(getTimeStamp()
				+ ": Peer "
				+ peerId1
				+ " is unchoked by "
				+ peerId2
				+ ".");
	}

	public void logChokingEvent(String peerId1, String peerId2) {

		writeFile(getTimeStamp()
				+ ": Peer "
				+ peerId1
				+ " is choked by "
				+ peerId2
				+ ".");
	}


	public void logInterestedMessageReceived(String to, String from) {

		writeFile(getTimeStamp()
				+ ":Peer "
				+ to
				+ " received the 'interested' messaging from "
				+ from
				+ ".");
	}


	public void logNotInterestedMessageReceived(String to, String from) {

		writeFile(getTimeStamp()
				+ "Peer "
				+ to
				+ " received the 'not interested' messaging from "
				+ from
				+ ".");
	}


	public void logPieceDownloadComplete(String to, String from, int pieceIndex, int numberOfPieces) {

		writeFile(getTimeStamp()
				+ "Peer "
				+ to
				+ " has downloaded the piece "
				+ pieceIndex
				+ " from "
				+ from
				+ "."
				+ "Now the number of pieces it has is "
				+ numberOfPieces);

	}

	public void logDownloadComplete(String peerId) {

		writeFile(getTimeStamp()
				+ "Peer "
				+ peerId
				+ " has downloaded the complete file.");
	}



}