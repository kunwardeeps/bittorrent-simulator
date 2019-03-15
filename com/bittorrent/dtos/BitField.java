package com.bittorrent.dtos;

import com.bittorrent.utils.Logger;

public class BitField {

	private static BitField bf;
	private Logger logger;

	private BitField() {
		init();
	}

	private void init() {
		//TODO
	}

	public static BitField getObject() {
		synchronized (BitField.class) {
			if (bf == null) {
				bf = new BitField();
			}
		}
		return bf;
	}

}
