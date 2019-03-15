
package com.bittorrent.main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class MainServer implements Runnable {
	protected List<Integer> outboundMessageLengthQueue;
	protected List<byte[]> outboundMessageQueue;
	private Socket socket;
	private DataOutputStream outputDataStream;
	private boolean isConnectionActive;

	// client thread initialization
	public MainServer(Socket clientSocket, String id) {

		outboundMessageLengthQueue = new ArrayList<>();
		outboundMessageQueue = new ArrayList<>();
		isConnectionActive = true;
		this.socket = clientSocket;
		try {
			outputDataStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception ex){
			//Subside.
		}
	}

	// server thread initialization
	public MainServer(Socket clientSocket) {


			//needed to update the controller.

		outboundMessageLengthQueue = new ArrayList<>();
		outboundMessageQueue = new ArrayList<>();
		isConnectionActive = true;
		this.socket = clientSocket;
		try {
			outputDataStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception ex){
			//Subside.
		}
	}

	private void init(Socket clientSocket) {

		if(outputDataStream!=null) {
			try {
				outputDataStream = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception ex) {
				//Subside.
			}
		}
	}

	@Override
	public String toString(){
		return "Socket is Active : "+isConnectionActive;
	}

	@Override
	public void run() {
		while (isConnectionActive) {
			try {
				sendMessageLength();
				sendMessageData();
			}
			catch (SocketException e) {
				isConnectionActive = false;
			}
			catch (Exception e) {
			}
		}
	}

	private void sendMessageLength() throws Exception{
		int messageLength = outboundMessageLengthQueue.size();
		outputDataStream.writeInt(messageLength);
		outputDataStream.flush();
	}

	private void sendMessageData() throws Exception{
		byte[] message = outboundMessageQueue.get(0);
		outputDataStream.write(message);
		outputDataStream.flush();
	}

	public void addMessage(int length, byte[] payload) {
		outboundMessageLengthQueue.add(length);
		outboundMessageQueue.add(payload);
	}
}
