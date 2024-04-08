package com.example.game_latanh.AICore;

import java.io.Serializable;

public class BestMove implements Serializable {
    private static final long serialVersionUID = -3102985870943786145L;

    int ZobristKey;
    long ZobristKeyCheck;
    int depth;
    Move move;

    public BestMove(int ZobristKey, long ZobristKeyCheck, int depth, Move move) {
        this.ZobristKey = ZobristKey;
        this.ZobristKeyCheck = ZobristKeyCheck;
        this.depth = depth;
        this.move = move;
    }
}
