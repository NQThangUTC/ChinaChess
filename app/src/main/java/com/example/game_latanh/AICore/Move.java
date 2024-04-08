package com.example.game_latanh.AICore;

import com.example.game_latanh.Info.Pos;

import java.io.Serializable;


public class Move implements Serializable {
    private static final long serialVersionUID = -1608509463525143473L;

    public Pos fromPos;
    public Pos toPos;

    public Move(Pos fromPos, Pos toPos) {
        this.fromPos = fromPos;
        this.toPos = toPos;
    }
}
