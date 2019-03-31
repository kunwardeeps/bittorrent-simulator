package com.bittorrent.main;

import java.io.DataInputStream;
import java.io.EOFException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Thread that runs a peer as a client for accepting requests
 */
public class ServerProcess implements Runnable {

	private DataInputStream inputStream;
	private boolean downloadActive;
	private Socket currentSocket;

	public ServerProcess(Socket socket) {
		this.currentSocket = socket;
		downloadActive = true;
		// Data Input Stream
		try {
			inputStream = new DataInputStream(socket.getInputStream());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {

		receiveMessage();
	}

	public void receiveMessage() {

		while (downloadActive) {
			// Receive Message
			int messageLength = Integer.MIN_VALUE;
			messageLength = rcvMessageLength();
			if (!downloadActive) {
				continue;
			}
			byte[] message = new byte[messageLength];
			receiveMessageData(message);

		}

	}

	private int rcvMessageLength() {
		int lengthofResponse = Integer.MIN_VALUE;
		byte[] messageLength = new byte[4];
		try {
			try {
				inputStream.readFully(messageLength);
			}
			catch (EOFException e) {
			
				System.exit(0);
			}
			catch (Exception e) {
				//System.out.println("No data to read");

			}
			lengthofResponse = ByteBuffer.wrap(messageLength).getInt();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return lengthofResponse;
	}

	private void receiveMessageData(byte[] message) {
		try {
			inputStream.readFully(message);
		}
		catch (EOFException e) {
			System.exit(0);
		}
		catch (Exception e) {
			//System.out.println("No data to read");

		}
	}

	public boolean terminate(){
		try{
			if(currentSocket!=null){
				synchronized (this){
					currentSocket.close();
					return true;
				}
			}
		}
		catch (Exception ex){
			System.out.println(" ClientProcess not terminated properly");
			return false;
		}
		return false;
	}

}
