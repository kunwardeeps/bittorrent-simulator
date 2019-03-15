package com.bittorrent.dtos;

import java.nio.ByteBuffer;

import com.bittorrent.utils.Logger;


public class MessageDTO{
//Declaring Class Variables
	private static BitField bf;
	private Logger logger;
	protected ByteBuffer msgbytebuffer;
	protected byte msgtype;
	protected byte[] msgcontent;
	protected byte[] msgLength = new byte[4];
	protected byte[] message;

	public static BitField getBf() {
		return bf;
	}

	public static void setBf(BitField bf) {
		MessageDTO.bf = bf;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public ByteBuffer getMsgbytebuffer() {
		return msgbytebuffer;
	}

	public void setMsgbytebuffer(ByteBuffer msgbytebuffer) {
		this.msgbytebuffer = msgbytebuffer;
	}

	public byte getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(byte msgtype) {
		this.msgtype = msgtype;
	}

	public byte[] getMsgcontent() {
		return msgcontent;
	}

	public void setMsgcontent(byte[] msgcontent) {
		this.msgcontent = msgcontent;
	}

	public void setMsgLength(byte[] msgLength) {
		this.msgLength = msgLength;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	//Defining Message Types
	public static enum Type {
		HANDSHAKE,CHOKE, 
		UNCHOKE, INTERESTED, 
		NOTINTERESTED, HAVE, 
		BITFIELD, REQUEST,
		PIECE ;
	}
}
