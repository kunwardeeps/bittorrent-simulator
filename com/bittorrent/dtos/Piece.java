package com.bittorrent.dtos;

public class Piece {

    public String id = "";
    public String content = "";
    public boolean present = false;

    public Piece(String id, String content, boolean present) {
        this.id = id;
        this.content = content;
        this.present = present;
    }
}
