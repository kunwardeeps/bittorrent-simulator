
package com.bittorrent.main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Thread that runs in a peer as a server for sending requests
 */
public class ClientProcess implements Runnable {
	private Socket socket;
	private DataOutputStream outputDataStream;
	private boolean isConnectionActive;

	// server thread initialization
	public ClientProcess(Socket clientSocket) {

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

	@Override
	public String toString(){
		return "Socket is Active : "+isConnectionActive;
	}

	@Override
	public void run() {
		try {
			while (isConnectionActive) {
				sendMessageData("Test messaging".getBytes());
				System.out.println("Client Process running");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendMessageData(byte[] message) throws Exception{
		outputDataStream.write(message);
		outputDataStream.flush();
	}
}
