package com.bittorent.main;

import java.io.DataInputStream;
import java.io.EOFException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Client implements Runnable {
	private DataInputStream inputStream;
	private boolean DownloadActive;
	private DataController sharedData;
	private Socket currentSocket;


		public Client(Socket socket, DataController data) {
		this.currentSocket = socket;
		sharedData = data;
		DownloadActive = true;
		// Data Input Stream
		try {
			inputStream = new DataInputStream(socket.getInputStream());
		}
		catch (Exception e) {
			CommonProperties.DisplayMessageForUser(this,e.getMessage());
		}
	}

	@Override
	public void run() {

		receiveMessage();
	}

	public void receiveMessage() {
		while (DownloadActive()) {
			// Receive Message
			int messageLength = Integer.MIN_VALUE;
			messageLength = rcvMessageLength();
			if (!DownloadActive()) {
				continue;
			}
			byte[] message = new byte[messageLength];
			receiveMessageData(message);
			
			// Add Payload
			sharedData.addPayload(message);
		}

	}
	
	private synchronized boolean DownloadActive() {

		return DownloadActive;
	}
	
	public void close()
	{
		boolean terminate = terminateClient();
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
			CommonProperties.DisplayMessageForUser(this,e.getMessage());
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

	public boolean terminateClient(){
		try{
			if(currentSocket!=null){
				synchronized (this){
					currentSocket.close();
					return true;
				}
			}
		}
		catch (Exception ex){
			CommonProperties.DisplayMessageForUser(null," Client not terminated properly");
			return false;
		}
		return false;
	}

}
