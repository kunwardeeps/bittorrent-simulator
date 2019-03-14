package Message;

import Common.MainModule.CommonProperties;
import DAL.MainModule.*;

import java.util.BitSet;

public class BitField extends MessageTemplate {

	private static BitField bf;
	private FileHandler fileHandler;

	private BitField() {
		init();
	}

	private void init() {
		msgtype = 5;
		message = new byte[CommonProperties.numberOfChunks + 1];
		msgcontent = new byte[CommonProperties.numberOfChunks];
		fileHandler = FileHandler.getInstance();
		message[0] = msgtype;
		BitSet Pieces = fileHandler.getFilePieces();
		for (int chunks = 0; chunks < CommonProperties.numberOfChunks; chunks++) {
			if (Pieces.get(chunks)) {
				message[chunks + 1] = 1;
			}
		}
	}

	public static BitField getObject() {
		synchronized (BitField.class) {
			if (bf == null) {
				bf = new BitField();
			}
		}
		return bf;
	}

	@Override
	protected synchronized int getMsgLength() {
		init();
		return message.length;
	}

	@Override
	protected synchronized byte[] getMsgData() {
		return message;
	}

}
