package com.example.game_latanh.Info;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.game_latanh.HomeActivity.zobrist;

public class ChessInfo implements Cloneable, Serializable {
    private static final long serialVersionUID = 5810196428873080501L;

    public int[] Select = new int[]{-1, -1};
    public boolean IsRedGo = true;
    public boolean IsChecked = false;
    public boolean IsReverse = false;
    public Pos prePos = new Pos(-1, -1);
    public Pos curPos = new Pos(-1, -1);
    public int status = 1;    //1 2
    public List<Pos> ret = new ArrayList<Pos>();
    public int[][] piece = new int[][]{
            {5, 4, 3, 2, 1, 2, 3, 4, 5},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 6, 0, 0, 0, 0, 0, 6, 0},
            {7, 0, 7, 0, 7, 0, 7, 0, 7},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {14, 0, 14, 0, 14, 0, 14, 0, 14},
            {0, 13, 0, 0, 0, 0, 0, 13, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {12, 11, 10, 9, 8, 9, 10, 11, 12},
    };
    public int ZobristKey = 2054193152;
    public long ZobristKeyCheck = 7691135808095748096L;
    public int peaceRound = 0;
    public int attackNum_R = 11;
    public int attackNum_B = 11;
    public boolean isMachine = false;

    public ChessInfo() {

    }

    public void setInfo(ChessInfo chessInfo) throws CloneNotSupportedException {
        this.Select = chessInfo.Select.clone();
        this.IsRedGo = chessInfo.IsRedGo;
        this.IsChecked = chessInfo.IsChecked;
        this.IsReverse = chessInfo.IsReverse;
        this.prePos = (Pos) chessInfo.prePos.clone();
        this.curPos = (Pos) chessInfo.curPos.clone();
        this.status = chessInfo.status;
        this.ret = new ArrayList<Pos>(chessInfo.ret);
        for (int i = 0; i < this.piece.length; i++) {
            this.piece[i] = chessInfo.piece[i].clone();
        }
        this.ZobristKey = chessInfo.ZobristKey;
        this.ZobristKeyCheck = chessInfo.ZobristKeyCheck;
        this.peaceRound = chessInfo.peaceRound;
        this.attackNum_R = chessInfo.attackNum_R;
        this.attackNum_B = chessInfo.attackNum_B;
        this.isMachine = chessInfo.isMachine;
    }

    public void updateAllInfo(Pos fromPos, Pos toPos, int fromID, int toID) {
        updateZobrist(fromPos, fromID);
        updateZobrist(toPos, fromID);
        updateZobrist(toPos, toID);
        updatePeaceRound(toID);
        updateAttackNum(toID);
    }

    public void updateZobrist(Pos pos, int pieceID) {
        if (pieceID == 0) return;
        ZobristKey = ZobristKey ^ zobrist.ZobristTable[pieceID - 1][pos.y][pos.x];
        ZobristKeyCheck = ZobristKeyCheck ^ zobrist.ZobristTableCheck[pieceID - 1][pos.y][pos.x];
    }

    public void updatePeaceRound(int toID) {
        if (toID == 0) {
            peaceRound++;
        } else {
            peaceRound = 0;
        }
    }

    public void updateAttackNum(int toID) {
        if (toID >= 4 && toID <= 7) {
            attackNum_B--;
        } else if (toID >= 11 && toID <= 14) {
            attackNum_R--;
        }
    }
}
