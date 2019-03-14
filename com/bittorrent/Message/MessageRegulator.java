package Message;

import java.nio.ByteBuffer;

import Message.MessageTemplate.Type;
import DAL.MainModule.*;

public class MessageRegulator {
	private static MessageRegulator messageAdmin;
	private FileHandler fileHandler;

	private MessageRegulator() {
		fileHandler = FileHandler.getInstance();
	}

	public synchronized int processLength(byte[] msgLength) {
		if(msgLength==null) 
			return 0;
		else{
			return ByteBuffer.wrap(msgLength).getInt();
		}
		
	}
	private static BitField getObject() {
		synchronized (BitField.class) {
			if (bf == null) {
				bf = new BitField();
			}
		}
		return bf;
	}
	

	public static MessageRegulator getInstance() {
		synchronized (MessageRegulator.class) {
			if (messageAdmin == null) {
				messageAdmin = new MessageRegulator();
			}
		}
		return messageAdmin;
	}

	public synchronized int getMessageLength(Type msgType, int pieceIndex) {
		switch (msgType) {
		case CHOKE:
		case UNCHOKE:
		case INTERESTED:
		case NOTINTERESTED:
			return 1;
		case REQUEST:
		case HAVE:
			return 5;
		case BITFIELD:
			BitField bf= BitField.getObject();
			return bf.getMsgLength();
		case PIECE:
			System.out.println("Shared file" + fileHandler.getFileChunkByIndex(pieceIndex) + " requesting piece " + pieceIndex);
			int payload = 5 + FileHandler.getInstance().getFileChunkByIndex(pieceIndex).length;
			return payload;
		case HANDSHAKE:
			return 32;
		}
		return -1;
	}

	public synchronized ByteBuffer processData(byte[] message) {
		if(message==null)
			return null;
		else{
			return ByteBuffer.wrap(message);
		}
		
	}

	public synchronized byte[] getMessagePayload(Type msgType, int pieceIndex) {
		byte[] responseMessage = new byte[5];

		switch (msgType) {
		case CHOKE:
			return new byte[] { 0 };
		case UNCHOKE:
			return new byte[] { 1 };
		case INTERESTED:
			return new byte[] { 2 };
		case NOTINTERESTED:
			return new byte[] { 3 };
		case HAVE:
			responseMessage[0] = 4;
			byte[] PieceIndex = ByteBuffer.allocate(4).putInt(pieceIndex).array();
			System.arraycopy(PieceIndex, 0, responseMessage, 1, 4);
			break;
		case BITFIELD:
			BitField bitfield = BitField.getObject();
			responseMessage = bitfield.getMsgData();
			break;
		case REQUEST:
			responseMessage[0] = 6;
			byte[] index = ByteBuffer.allocate(4).putInt(pieceIndex).array();
			System.arraycopy(index, 0, responseMessage, 1, 4);
			break;
		case PIECE:
			responseMessage = processPiece(pieceIndex);
			break;
		case HANDSHAKE:
			return MessageTemplate.getMessage();
		default:
				break;
		}
		return responseMessage;
	}

	private byte[] processPiece(int fileChunkIndex){
		byte[] responseMessage;
		byte[] piece = fileHandler.getFileChunkByIndex(fileChunkIndex);
		int pieceSize = piece.length;
		int totalLength = 5 + pieceSize;
		responseMessage = new byte[totalLength];
		responseMessage[0] = 7;
		byte[] data = ByteBuffer.allocate(4).putInt(fileChunkIndex).array();
		System.arraycopy(data, 0, responseMessage, 1, 4);
		System.arraycopy(piece, 0, responseMessage, 5, pieceSize);
		return responseMessage;
	}

	public synchronized Type getType(byte type) {

		Type response = null;
		switch (type) {
			case 0:
				response = Type.CHOKE;
				break;
			case 1:
				response = Type.UNCHOKE;
				break;
			case 2:
				response = Type.INTERESTED;
				break;
			case 3:
				response= Type.NOTINTERESTED;
				break;
			case 4:
				response = Type.HAVE;
				break;
			case 5:
				response = Type.BITFIELD;
				break;
			case 6:
				response = Type.REQUEST;
				break;
			case 7:
				response = Type.PIECE;
				break;
			default:
				break;
		}

		return response;
	}
}
