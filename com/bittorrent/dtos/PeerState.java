package com.bittorrent.dtos;

import java.util.concurrent.ConcurrentHashMap;

public class PeerState {

	private int sequenceId;
	private String peerId;
	private String hostName;
	private int port;
	private boolean hasSharedFile;
	private boolean fileReceived = false;
	private ConcurrentHashMap<String, Piece> bitField = new ConcurrentHashMap<>();

	public boolean hasPiece(String id) {
		return bitField.contains(id);
	}

	public Piece getPiece(String id) {
		return bitField.get(id);
	}

	public Piece setPiece(Piece piece) {
		return bitField.put(piece.id, piece);
	}

	public String getPeerId() {
		return peerId;
	}

	public String getHostName() {
		return hostName;
	}

	public int getPort() {
		return port;
	}

	public boolean isHasSharedFile() {
		return hasSharedFile;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setHasSharedFile(boolean hasSharedFile) {
		this.hasSharedFile = hasSharedFile;
	}

	public boolean isFileReceived() {
		return fileReceived;
	}

	public void setFileReceived(boolean fileReceived) {
		this.fileReceived = fileReceived;
	}

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public PeerState(String peerId, String hostName, int port, boolean hasSharedFile) {
		this.peerId = peerId;
		this.hostName = hostName;
		this.port = port;
		this.hasSharedFile = hasSharedFile;
	}

	public PeerState(){}

	@Override
	public String toString() {
		return "PeerState{" +
				"sequenceId=" + sequenceId +
				", peerId='" + peerId + '\'' +
				", hostName='" + hostName + '\'' +
				", port=" + port +
				", hasSharedFile=" + hasSharedFile +
				", fileReceived=" + fileReceived +
				", bitField=" + bitField +
				'}';
	}
}
