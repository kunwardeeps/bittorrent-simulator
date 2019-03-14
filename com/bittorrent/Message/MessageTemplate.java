package Message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import DAL.MainModule.FileHandler;


public abstract class MessageTemplate{
//Declaring Class Variables
	private static BitField bf;
	private FileHandler fileHandler;
	protected ByteBuffer msgbytebuffer;
	protected byte msgtype;
	protected byte[] msgcontent;
	protected byte[] msgLength = new byte[4];
	protected byte[] message;
	
	private class MessageRegulator {
		
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
	}
	
	
	
	
	
	
	
	//Defining Message Types
	public static enum Type {
		HANDSHAKE,CHOKE, 
		UNCHOKE, INTERESTED, 
		NOTINTERESTED, HAVE, 
		BITFIELD, REQUEST,
		PIECE ;
	}
//Declaring Message Properties 
	abstract protected int getMsgLength();
	abstract protected byte[] getMsgData();
	private static final String HandshakeHeader = "P2PFILESHARINGPROJ";
	private static String handshakeMessage = "";

	public static synchronized String getRemotePeerId(byte[] byt) {
		int to = byt.length;
		int from = to - 4;
		byte[] bytes = Arrays.copyOfRange(byt, from, to);
		String newstr = new String(bytes, StandardCharsets.UTF_8);
		return newstr;
	}

	private static synchronized void initHandshake(String peerId) {

		handshakeMessage += HandshakeHeader + peerId;
	}

	public static synchronized byte[] getMessage() {
		byte[] handshakemsg = new byte[32];
		ByteBuffer bytbuf = ByteBuffer.wrap(handshakeMessage.getBytes());
		bytbuf.get(handshakemsg);
		return handshakemsg;
	}

	public static synchronized void setId(String unitpeerId) {
		initHandshake(unitpeerId);
	}

	public static synchronized boolean verify(byte[] message, String unitpeerId) {
		String receivedMessage = new String(message);
		return receivedMessage.indexOf(unitpeerId) != -1 && receivedMessage.contains(HandshakeHeader);
	}

	public static synchronized String getId(byte[] msg) {
		byte[] remPeerId = Arrays.copyOfRange(msg, msg.length - 4, msg.length);
		return new String(remPeerId);
	}

}
