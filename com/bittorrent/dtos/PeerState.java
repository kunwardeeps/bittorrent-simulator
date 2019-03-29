package com.bittorrent.dtos;

public class PeerState {

	private int networkId;
	private String peerId;
	private String hostName;
	private int port;
	private boolean hasSharedFile;
	private boolean fileReceived = false;

	public int getNetworkId() {
		return networkId;
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

	public void setNetworkId(int networkId) {
		this.networkId = networkId;
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

	public PeerState(int networkId, String peerId, String hostName, int port, boolean hasSharedFile) {
		this.networkId = networkId;
		this.peerId = peerId;
		this.hostName = hostName;
		this.port = port;
		this.hasSharedFile = hasSharedFile;
	}

	public PeerState(){}

	@Override
	public String toString() {
		return "PeerDTO{" +
				"networkId=" + networkId +
				", peerId='" + peerId + '\'' +
				", hostName='" + hostName + '\'' +
				", port=" + port +
				", hasSharedFile=" + hasSharedFile +
				", fileReceived=" + fileReceived +
				'}';
	}
}
