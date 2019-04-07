package com.bittorrent.main;

import com.bittorrent.dtos.BitTorrentState;
import com.bittorrent.dtos.PeerState;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.*;

public class FileHandler {

	public static void makeFiles(String peerId){
		try {
			String fileNameWithPath = PropertiesEnum.PROPERTIES_CREATED_FILE_PATH.getValue() + peerId
					+ File.separatorChar + BitTorrentState.fileName;
			File createdFile = new File(fileNameWithPath);
			createdFile.getParentFile().mkdirs(); // Will create parent directories if not exists
			createdFile.createNewFile();
		} catch (IOException e) {
			System.out.println("Failed to create new fileSplitMap while receiving the fileSplitMap from host peer");
			e.printStackTrace();
		}
	}


	private static ConcurrentHashMap<Integer, byte[]> readFilePieces(DataInputStream dataInputStream) throws IOException{
		int numberOfPieces = BitTorrentState.getNumberOfPieces();
		ConcurrentHashMap<Integer, byte[]> fileSplitMap = new ConcurrentHashMap<>();

		for (int i = 0; i < numberOfPieces; i++) {
			int pieceSize = i != numberOfPieces - 1 ? BitTorrentState.getPieceSize()
					: (int) (BitTorrentState.getFileSize() % BitTorrentState.getPieceSize());
			byte[] piece = new byte[pieceSize];
			dataInputStream.readFully(piece);
			fileSplitMap.put(i, piece);
		}
		return fileSplitMap;
	}

	public static ConcurrentHashMap<Integer, byte[]> splitFile() {
		File file = new File(PropertiesEnum.PROPERTIES_FILE_PATH.getValue() + BitTorrentState.fileName);
		FileInputStream fileInputStream = null;
		DataInputStream dataInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			dataInputStream = new DataInputStream(fileInputStream);

			return readFilePieces(dataInputStream);

		}
		catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileInputStream.close();
				dataInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void writeToFile(PeerState peerState) {
		String fileNameWithPath = PropertiesEnum.PROPERTIES_CREATED_FILE_PATH.getValue() + peerState.getPeerId()
				+ File.separatorChar + BitTorrentState.fileName;
		System.out.println("Writing to file " + fileNameWithPath);
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(fileNameWithPath);
			for (int i = 0; i < peerState.getFileSplitMap().size(); i++) {
				try {
					outputStream.write(peerState.getFileSplitMap().get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {
				outputStream.flush();
			}
			catch (Exception ex){
				System.out.println("OutputStreamed failed to clear, beginning retry...");
				ex.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		// For testing only...
//		BitTorrentState.setStateFromConfigFiles();
//		PeerState peerState = new PeerState();
//		FileHandler fileHandler = new FileHandler("1001");
//		fileHandler.makeFiles();
//		peerState.setFileSplitMap(fileHandler.splitFile());
//		fileHandler.writeToFile(peerState.getFileSplitMap());
	}
}
