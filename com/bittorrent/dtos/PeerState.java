package com.bittorrent.dtos;

import com.bittorrent.handlers.PeerConnectionHandler;
import com.bittorrent.messaging.Message;

import java.net.ServerSocket;
import java.util.BitSet;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class PeerState {

	private int sequenceId;
	private String peerId;
	private String hostName;
	private int port;
	private boolean hasSharedFile;
	private boolean fileReceived = false;
	private BitSet bitField;
	private ConcurrentHashMap<Integer, byte[]> fileSplitMap;
	private ConcurrentHashMap<String, String> preferredNeighbours = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, String> chokedNeighbours = new ConcurrentHashMap<>();
	private Map<String, String> interestedNeighbours = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, PeerConnectionHandler> connections = new ConcurrentHashMap<>();
	private BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
	private double dataRate = 0;
	private Timer timer1;
	private Timer timer2;
	private ServerSocket serverSocket;
	private AtomicBoolean downloadComplete = new AtomicBoolean(false);

	public boolean getDownloadComplete() {
		return downloadComplete.get();
	}

	public void setDownloadComplete(boolean value) {
		this.downloadComplete.set(value);
	}

	public ConcurrentHashMap<String, PeerConnectionHandler> getConnections() {
		return connections;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void setTimer1(Timer timer1) {
		this.timer1 = timer1;
	}

	public void setTimer2(Timer timer2) {
		this.timer2 = timer2;
	}

	public void stopScheduledTasks() {
		System.out.println(getPeerId() + ": stopping scheduler tasks");
		timer1.cancel();
		timer1.purge();
		timer2.cancel();
		timer2.purge();
	}

	public void setInterestedNeighbours(Map<String, String> interestedNeighbours) {
		this.interestedNeighbours = interestedNeighbours;
	}

	public double getDataRate() {
		return dataRate;
	}

	public void setDataRate(double dataRate) {
		this.dataRate = dataRate;
	}

	public BlockingQueue<Message> getQueue(){
		return queue;
	}

	public synchronized void putFileSplitMap(int index, byte[] piece) {
		this.fileSplitMap.put(index, piece);
		this.bitField.set(index);
	}

	public Map<String, String> getInterestedNeighbours() {
		return interestedNeighbours;
	}

	public int preferredNeighboursCount(){
		return preferredNeighbours.size();
	}

	public void removeInterestedNeighbours(String peerId) {
		interestedNeighbours.remove(peerId);
	}

	public void putInterestedNeighbours(String peerId) {
		this.interestedNeighbours.put(peerId, peerId);
	}

	public void putPreferredNeighbours(String peerId) {
		preferredNeighbours.put(peerId, peerId);
	}

	public void setBitField(BitSet bitField) {
		this.bitField = bitField;
	}

	public ConcurrentHashMap<Integer, byte[]> getFileSplitMap() {
		return fileSplitMap;
	}

	public void setFileSplitMap(ConcurrentHashMap<Integer, byte[]> fileSplitMap) {
		this.fileSplitMap = fileSplitMap;
	}

	public ConcurrentHashMap<String, String> getPreferredNeighbours() {
		return preferredNeighbours;
	}

	public void setPreferredNeighbours(ConcurrentHashMap<String, String> preferredNeighbours) {
		this.preferredNeighbours = preferredNeighbours;
	}

	public ConcurrentHashMap<String, String> getChokedNeighbours() {
		return chokedNeighbours;
	}

	public void setChokedNeighbours(ConcurrentHashMap<String, String> chokedNeighbours) {
		this.chokedNeighbours = chokedNeighbours;
	}

	public BitSet getBitField() {
		return bitField;
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
		if (hasSharedFile) {
			if (this.bitField == null) {
				this.bitField = new BitSet(BitTorrentState.getNumberOfPieces());
			}
			this.bitField.set(0, BitTorrentState.getNumberOfPieces());
		}

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

	public PeerState(){
		this.bitField = new BitSet(BitTorrentState.getNumberOfPieces());
	}

	@Override
	public String toString() {
		return "PeerState{" +
				"sequenceId=" + sequenceId +
				", peerId='" + peerId + '\'' +
				", hostName='" + hostName + '\'' +
				", port=" + port +
				", hasSharedFile=" + hasSharedFile +
				", fileReceived=" + fileReceived +
				'}';
	}

	public static void main(String args[]) {
//		BitSet bitSet = new BitSet(5);
//		BitSet bitSet1 = new BitSet(5);
//		bitSet.set(0);
//		bitSet1.set(0,4);
//		System.out.println(bitSet.g);
	}
}
