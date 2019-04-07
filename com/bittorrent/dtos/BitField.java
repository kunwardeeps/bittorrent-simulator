package com.bittorrent.dtos;

import com.bittorrent.utils.Logger;

import java.util.BitSet;

public class BitField {

	private BitSet bitSet;

	public BitField(int pieces) {
		this.bitSet = new BitSet(pieces);
	}

	private void init() {
		//TODO
	}

}
