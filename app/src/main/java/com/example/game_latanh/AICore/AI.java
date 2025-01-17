package com.example.game_latanh.AICore;

import android.util.Log;

import com.example.game_latanh.Info.Pos;
import com.example.game_latanh.ChessMove.Rule;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.game_latanh.HomeActivity.zobrist;
import static com.example.game_latanh.PvMActivity.transformTable;

public class AI {
    public static int[][][] pieceValue = new int[][][]{
            //Tướng Đen
            {
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
            },
            //Sĩ đen
            {
                    {0, 0, 0, 20, 0, 20, 0, 0, 0},
                    {0, 0, 0, 0, 23, 0, 0, 0, 0},
                    {0, 0, 0, 20, 0, 20, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
            },
            //Tượng đen
            {
                    {0, 0, 20, 0, 0, 0, 20, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {18, 0, 0, 0, 23, 0, 0, 0, 18},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 20, 0, 0, 0, 20, 0, 0},

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
            },
            //Mã đen
            {
                    {88, 85, 90, 88, 90, 88, 90, 85, 88},
                    {85, 90, 92, 93, 78, 93, 92, 90, 85},
                    {93, 92, 94, 95, 92, 95, 94, 92, 93},
                    {92, 94, 98, 95, 98, 95, 98, 94, 92},
                    {90, 98, 101, 102, 103, 102, 101, 98, 90},

                    {90, 100, 99, 103, 104, 103, 99, 100, 90},
                    {93, 108, 100, 107, 100, 107, 100, 108, 93},
                    {92, 98, 99, 103, 99, 103, 99, 98, 92},
                    {90, 96, 103, 97, 94, 97, 103, 96, 90},
                    {90, 90, 90, 96, 90, 96, 90, 90, 90},
            },
            //Xe đen
            {
                    {194, 206, 204, 212, 200, 212, 204, 206, 194},
                    {200, 208, 206, 212, 200, 212, 206, 208, 200},
                    {198, 208, 204, 212, 212, 212, 204, 208, 198},
                    {204, 209, 204, 212, 214, 212, 204, 209, 204},
                    {208, 212, 212, 214, 215, 214, 212, 212, 208},

                    {208, 211, 211, 214, 215, 214, 211, 211, 208},
                    {206, 213, 213, 216, 216, 216, 213, 213, 206},
                    {206, 208, 207, 214, 216, 214, 207, 208, 206},
                    {206, 212, 209, 216, 233, 216, 209, 212, 206},
                    {206, 208, 207, 213, 214, 213, 207, 208, 206},
            },
            //Pháo đen
            {
                    {96, 96, 97, 99, 99, 99, 97, 96, 96},
                    {96, 97, 98, 98, 98, 98, 98, 97, 96},
                    {97, 96, 100, 99, 101, 99, 100, 96, 97},
                    {96, 96, 96, 96, 96, 96, 96, 96, 96},
                    {95, 96, 99, 96, 100, 96, 99, 96, 95},

                    {96, 96, 96, 96, 100, 96, 96, 96, 96},
                    {96, 99, 99, 98, 100, 98, 99, 99, 96},
                    {97, 97, 96, 91, 92, 91, 96, 97, 97},
                    {98, 98, 96, 92, 89, 92, 96, 98, 98},
                    {100, 100, 96, 91, 90, 91, 96, 100, 100},
            },
            //Tốt đen
            {

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {7, 0, 7, 0, 15, 0, 7, 0, 7},
                    {7, 0, 13, 0, 16, 0, 13, 0, 7},

                    {14, 18, 20, 27, 29, 27, 20, 18, 14},
                    {19, 23, 27, 29, 30, 29, 27, 23, 19},
                    {19, 24, 32, 37, 37, 37, 32, 24, 19},
                    {19, 24, 34, 42, 44, 42, 34, 24, 19},
                    {9, 9, 9, 11, 13, 11, 9, 9, 9},
            },
            //Tướng đỏ
            {
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0}
            },
            //Sĩ đỏ
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 20, 0, 20, 0, 0, 0},
                    {0, 0, 0, 0, 23, 0, 0, 0, 0},
                    {0, 0, 0, 20, 0, 20, 0, 0, 0}
            },
            //Tượng đỏ
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},

                    {0, 0, 20, 0, 0, 0, 20, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {18, 0, 0, 0, 23, 0, 0, 0, 18},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 20, 0, 0, 0, 20, 0, 0}
            },
            //Mã đỏ
            {
                    {90, 90, 90, 96, 90, 96, 90, 90, 90},
                    {90, 96, 103, 97, 94, 97, 103, 96, 90},
                    {92, 98, 99, 103, 99, 103, 99, 98, 92},
                    {93, 108, 100, 107, 100, 107, 100, 108, 93},
                    {90, 100, 99, 103, 104, 103, 99, 100, 90},

                    {90, 98, 101, 102, 103, 102, 101, 98, 90},
                    {92, 94, 98, 95, 98, 95, 98, 94, 92},
                    {93, 92, 94, 95, 92, 95, 94, 92, 93},
                    {85, 90, 92, 93, 78, 93, 92, 90, 85},
                    {88, 85, 90, 88, 90, 88, 90, 85, 88}
            },
            //Xe đỏ
            {
                    {206, 208, 207, 213, 214, 213, 207, 208, 206},
                    {206, 212, 209, 216, 233, 216, 209, 212, 206},
                    {206, 208, 207, 214, 216, 214, 207, 208, 206},
                    {206, 213, 213, 216, 216, 216, 213, 213, 206},
                    {208, 211, 211, 214, 215, 214, 211, 211, 208},

                    {208, 212, 212, 214, 215, 214, 212, 212, 208},
                    {204, 209, 204, 212, 214, 212, 204, 209, 204},
                    {198, 208, 204, 212, 212, 212, 204, 208, 198},
                    {200, 208, 206, 212, 200, 212, 206, 208, 200},
                    {194, 206, 204, 212, 200, 212, 204, 206, 194}
            },
            //Pháo đỏ
            {

                    {100, 100, 96, 91, 90, 91, 96, 100, 100},
                    {98, 98, 96, 92, 89, 92, 96, 98, 98},
                    {97, 97, 96, 91, 92, 91, 96, 97, 97},
                    {96, 99, 99, 98, 100, 98, 99, 99, 96},
                    {96, 96, 96, 96, 100, 96, 96, 96, 96},

                    {95, 96, 99, 96, 100, 96, 99, 96, 95},
                    {96, 96, 96, 96, 96, 96, 96, 96, 96},
                    {97, 96, 100, 99, 101, 99, 100, 96, 97},
                    {96, 97, 98, 98, 98, 98, 98, 97, 96},
                    {96, 96, 97, 99, 99, 99, 97, 96, 96}
            },
            //Tốt đỏ
            {
                    {9, 9, 9, 11, 13, 11, 9, 9, 9},
                    {19, 24, 34, 42, 44, 42, 34, 24, 19},
                    {19, 24, 32, 37, 37, 37, 32, 24, 19},
                    {19, 23, 27, 29, 30, 29, 27, 23, 19},
                    {14, 18, 20, 27, 29, 27, 20, 18, 14},

                    {7, 0, 13, 0, 16, 0, 13, 0, 7},
                    {7, 0, 7, 0, 15, 0, 7, 0, 7},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}
            }
    };
    public static final int INF = 0x3f3f3f3f;
    public static final int Win = 88888;
    public static final int hashfEXACT = 1;
    public static final int hashfALPHA = 2;
    public static final int hashfBETA = 3;
    public static boolean isMachineRed;
    public static int ZobristKey;
    public static long ZobristKeyCheck;
    public static int maxDepth;
    public static int goodValue;
    public static int[][][][] historyTable = new int[9][10][9][10];
    public static int searchCnt = 0;

    public AI() {

    }

    public static Move getBestMove(int[][] piece, boolean isRedGo, int depth, int startZobristKey, long startZobristKeyCheck, Map<Long, Integer> ZobristInfo) {
        int[][] pieceClone = new int[10][9];
        for (int i = 0; i <= 9; i++) {
            pieceClone[i] = piece[i].clone();
        }

        searchCnt = 0;
        Move goodMove = new Move(new Pos(-1, -1), new Pos(-1, -1));
        Move bestMove = new Move(new Pos(-1, -1), new Pos(-1, -1));
        clearHistory();
        for (int i = 2; i <= depth; i += 2) {
            goodMove = getGoodMove(pieceClone, isRedGo, i, startZobristKey, startZobristKeyCheck, ZobristInfo);
            if (goodValue >= -Win / 2) {
                bestMove = goodMove;
            }
        }

        if (bestMove.fromPos.equals(new Pos(-1, -1)) == true) {
            bestMove = getLegalMove(pieceClone, isRedGo);
        }
        return bestMove;
    }


    public static Move getGoodMove(int[][] piece, boolean isRedGo, int depth, int startZobristKey, long startZobristKeyCheck, Map<Long, Integer> ZobristInfo) {
        Move goodMove = new Move(new Pos(-1, -1), new Pos(-1, -1));

        isMachineRed = isRedGo;
        boolean FoundPv = false;
        int value = -INF;
        int alpha = -INF, beta = INF;
        boolean isKingAlive = true;
        ZobristKey = startZobristKey;
        ZobristKeyCheck = startZobristKeyCheck;
        maxDepth = depth;

        LinkedList<Move> sortedMoves = allPossibleMoves(piece, isRedGo, depth, alpha, beta);
        Iterator<Move> it = sortedMoves.iterator();
        while (it.hasNext()) {
            Move move = it.next();
            Pos fromPos = move.fromPos;
            Pos toPos = move.toPos;
            int pieceID = piece[toPos.y][toPos.x];
            piece[toPos.y][toPos.x] = piece[fromPos.y][fromPos.x];
            piece[fromPos.y][fromPos.x] = 0;

            updateZobrist(fromPos, toPos, piece[toPos.y][toPos.x], pieceID);

            if (pieceID == 1 || pieceID == 8) {
                isKingAlive = false;
            }

            if (ZobristInfo.get(ZobristKeyCheck) != null) {
                value = -INF;
            } else {
                if (FoundPv) {
                    value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -alpha - 1, -alpha, isKingAlive);
                    if ((value > alpha) && (value < beta)) {
                        value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
                    }
                } else {
                    value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
                }
            }

            updateZobrist(fromPos, toPos, piece[toPos.y][toPos.x], pieceID);

            piece[fromPos.y][fromPos.x] = piece[toPos.y][toPos.x];
            piece[toPos.y][toPos.x] = pieceID;
            if (pieceID == 1 || pieceID == 8) {
                isKingAlive = true;
            }

            if (value > alpha) {
                alpha = value;
                FoundPv = true;
                goodMove = new Move(fromPos, toPos);
            }
        }
        goodValue = alpha;
        addHistory(goodMove.fromPos, goodMove.toPos, (maxDepth - depth + 1) * (maxDepth - depth + 1));
        return goodMove;
    }

    public static int PVS(int[][] piece, boolean isRedGo, int depth, int alpha, int beta, boolean isKingAlive) {
        int value = transformTable.readTransformTable(ZobristKey, ZobristKeyCheck, maxDepth - depth, alpha, beta);
        if (value != INF) {
            return value;
        }

        if (isKingAlive == false) {
            if (isMachineRed == true && isRedGo == true) {
                return -Win + (maxDepth - depth);
            } else if (isMachineRed == true && isRedGo == false) {
                return Win - (maxDepth - depth);
            } else if (isMachineRed == false && isRedGo == true) {
                return Win - (maxDepth - depth);
            } else {
                return -Win + (maxDepth - depth);
            }
        }

        if (depth <= 0) {
            searchCnt++;
            if (Rule.isDead(piece, isMachineRed)) {
                value = -Win + (maxDepth - depth + 2);
            } else {
                value = evaluate(piece);
            }
            return value;
        }

        boolean FoundPv = false;
        int hashf = hashfALPHA;
        int fatherZobristKey = ZobristKey;
        long fatherZobristKeyCheck = ZobristKeyCheck;
        value = -INF;
        Move goodMove = new Move(new Pos(-1, -1), new Pos(-1, -1));

        LinkedList<Move> sortedMoves = allPossibleMoves(piece, isRedGo, depth, alpha, beta);
        Iterator<Move> it = sortedMoves.iterator();
        while (it.hasNext()) {
            Move move = it.next();
            Pos fromPos = move.fromPos;
            Pos toPos = move.toPos;
            int pieceID = piece[toPos.y][toPos.x];
            if (maxDepth == 6 && depth == 1 && pieceID == 0) continue;

            piece[toPos.y][toPos.x] = piece[fromPos.y][fromPos.x];
            piece[fromPos.y][fromPos.x] = 0;

            updateZobrist(fromPos, toPos, piece[toPos.y][toPos.x], pieceID);

            if (pieceID == 1 || pieceID == 8) {
                isKingAlive = false;
            }

            if (FoundPv) {
                value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -alpha - 1, -alpha, isKingAlive);
                if ((value > alpha) && (value < beta)) {
                    value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
                }
            } else {
                value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
            }

            updateZobrist(fromPos, toPos, piece[toPos.y][toPos.x], pieceID);

            piece[fromPos.y][fromPos.x] = piece[toPos.y][toPos.x];
            piece[toPos.y][toPos.x] = pieceID;
            if (pieceID == 1 || pieceID == 8) {
                isKingAlive = true;
            }

            if (value >= beta) {
                transformTable.saveTransformTable(fatherZobristKey, fatherZobristKeyCheck, maxDepth - depth, beta, hashfBETA);
                addHistory(fromPos, toPos, (maxDepth - depth + 1) * (maxDepth - depth + 1));
                return beta;
            }
            if (value > alpha) {
                alpha = value;
                FoundPv = true;
                hashf = hashfEXACT;
                goodMove = new Move(fromPos, toPos);
            }
        }

        transformTable.saveTransformTable(fatherZobristKey, fatherZobristKeyCheck, maxDepth - depth, alpha, hashf);
        if (hashf == hashfEXACT) {
            addHistory(goodMove.fromPos, goodMove.toPos, (maxDepth - depth + 1) * (maxDepth - depth + 1));
        }
        return alpha;
    }

    public static LinkedList<Move> allPossibleMoves(int[][] piece, boolean isRedGo, int depth, int alpha, int beta) {
        LinkedList<Move> sortedMoves = new LinkedList<Move>();
        LinkedList<Move> transformMoves = new LinkedList<Move>();
        LinkedList<Move> eatMoves = new LinkedList<Move>();
        LinkedList<Move> historyMoves = new LinkedList<Move>();
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 8; j++) {
                if ((isRedGo == false && piece[i][j] >= 1 && piece[i][j] <= 7) || (isRedGo == true && piece[i][j] >= 8 && piece[i][j] <= 14)) {
                    List<Pos> ret = Rule.PossibleMoves(piece, j, i, piece[i][j]);
                    Pos fromPos = new Pos(j, i);
                    Iterator<Pos> it = ret.iterator();
                    while (it.hasNext()) {
                        Pos toPos = it.next();
                        updateZobrist(fromPos, toPos, piece[fromPos.y][fromPos.x], piece[toPos.y][toPos.x]);
                        if (transformTable.readTransformTable(ZobristKey, ZobristKeyCheck, maxDepth - depth, alpha, beta) != INF) {
                            transformMoves.add(new Move(fromPos, toPos));
                        } else {
                            if (piece[toPos.y][toPos.x] != 0) {
                                eatMoves.add(new Move(fromPos, toPos));
                            } else {
                                if (historyMoves.isEmpty()) {
                                    historyMoves.add(new Move(fromPos, toPos));
                                } else {
                                    Move firstMove = historyMoves.getFirst();
                                    int firstVal = getHistory(firstMove.fromPos, firstMove.toPos);
                                    int curVal = getHistory(fromPos, toPos);
                                    if (curVal >= firstVal) {
                                        historyMoves.addFirst(new Move(fromPos, toPos));
                                    } else {
                                        historyMoves.addLast(new Move(fromPos, toPos));
                                    }
                                }
                            }
                        }
                        updateZobrist(fromPos, toPos, piece[fromPos.y][fromPos.x], piece[toPos.y][toPos.x]);
                    }
                }
            }
        }
        sortedMoves.addAll(transformMoves);
        sortedMoves.addAll(eatMoves);
        sortedMoves.addAll(historyMoves);
        return sortedMoves;
    }

    public static void clearHistory() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 9; k++) {
                    for (int p = 0; p < 10; p++) {
                        historyTable[i][j][k][p] = historyTable[i][j][k][p] = 0;
                    }
                }
            }
        }
    }

    public static int getHistory(Pos fromPos, Pos toPos) {
        return historyTable[fromPos.x][fromPos.y][toPos.x][toPos.y];
    }

    public static void addHistory(Pos fromPos, Pos toPos, int val) {
        historyTable[fromPos.x][fromPos.y][toPos.x][toPos.y] += val;
    }
    //PVS+置换表
    /*public static Move getGoodMove(int[][] piece, boolean isRedGo, int depth, int startZobristKey, long startZobristKeyCheck, Map<Long,Integer> ZobristInfo) {
        Move goodMove = new Move(new Pos(-1, -1), new Pos(-1, -1));

        isMachineRed = isRedGo;
        boolean FoundPv = false;
        int value = -INF;
        int alpha = -INF, beta = INF;
        boolean isKingAlive = true;
        ZobristKey = startZobristKey;
        ZobristKeyCheck = startZobristKeyCheck;
        maxDepth = depth;

        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 8; j++) {
                if ((isRedGo == false && piece[i][j] >= 1 && piece[i][j] <= 7) || (isRedGo == true && piece[i][j] >= 8 && piece[i][j] <= 14)) {
                    List<Pos> ret = Rule.PossibleMoves(piece, j, i, piece[i][j]);
                    Pos fromPos = new Pos(j, i);
                    Iterator<Pos> it = ret.iterator();
                    while (it.hasNext()) {
                        Pos toPos = it.next();

                        int pieceID = piece[toPos.y][toPos.x];
                        piece[toPos.y][toPos.x] = piece[fromPos.y][fromPos.x];
                        piece[fromPos.y][fromPos.x] = 0;

                        updateZobrist( fromPos, toPos, piece[toPos.y][toPos.x], pieceID);

                        if (pieceID == 1 || pieceID == 8) {
                            isKingAlive = false;
                        }

                        if(ZobristInfo.get(ZobristKeyCheck)!=null){
                            value=-INF;
                        }
                        else{
                            if (FoundPv) {
                                value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -alpha - 1, -alpha, isKingAlive);
                                if ((value > alpha) && (value < beta)) {
                                    value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
                                }
                            } else {
                                value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
                            }
                        }

                        updateZobrist( fromPos, toPos, piece[toPos.y][toPos.x], pieceID);

                        piece[fromPos.y][fromPos.x] = piece[toPos.y][toPos.x];
                        piece[toPos.y][toPos.x] = pieceID;
                        if (pieceID == 1 || pieceID == 8) {
                            isKingAlive = true;
                        }

                        if (value > alpha) {
                            alpha = value;
                            FoundPv = true;
                            goodMove = new Move(fromPos, toPos);
                        }
                    }
                }
            }
        }
        goodValue=alpha;
        return goodMove;
    }

    public static int PVS(int[][] piece, boolean isRedGo, int depth, int alpha, int beta, boolean isKingAlive) {
        searchCnt++;
        int value = transformTable.readTransformTable(ZobristKey, ZobristKeyCheck, maxDepth - depth, alpha, beta);
        if (value != INF) {
            return value;
        }

        if (isKingAlive == false) {
            if (isMachineRed == true && isRedGo == true) {
                return -Win + (maxDepth - depth);
            } else if (isMachineRed == true && isRedGo == false) {
                return Win - (maxDepth - depth);
            } else if (isMachineRed == false && isRedGo == true) {
                return Win - (maxDepth - depth);
            } else {
                return -Win + (maxDepth - depth);
            }
        }

        if (depth <= 0) {
            if (Rule.isDead(piece, isMachineRed)) {
                value = -Win + (maxDepth - depth) + 2;
            } else {
                value = evaluate(piece);
            }
            return value;
        }

        boolean FoundPv = false;
        int hashf = hashfALPHA;
        int fatherZobristKey = ZobristKey;
        long fatherZobristKeyCheck = ZobristKeyCheck;
        value = -INF;


        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 8; j++) {
                if ((isRedGo == false && piece[i][j] >= 1 && piece[i][j] <= 7) || (isRedGo == true && piece[i][j] >= 8 && piece[i][j] <= 14)) {
                    List<Pos> ret = Rule.PossibleMoves(piece, j, i, piece[i][j]);
                    Pos fromPos = new Pos(j, i);
                    Iterator<Pos> it = ret.iterator();
                    while (it.hasNext()) {
                        Pos toPos = it.next();

                        int pieceID = piece[toPos.y][toPos.x];
                        if (maxDepth == 6 && depth == 1 && pieceID == 0) continue;

                        piece[toPos.y][toPos.x] = piece[fromPos.y][fromPos.x];
                        piece[fromPos.y][fromPos.x] = 0;

                        updateZobrist(fromPos, toPos, piece[toPos.y][toPos.x], pieceID);

                        if (pieceID == 1 || pieceID == 8) {
                            isKingAlive = false;
                        }

                        if (FoundPv) {
                            value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -alpha - 1, -alpha, isKingAlive);
                            if ((value > alpha) && (value < beta)) {
                                value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
                            }
                        } else {
                            value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
                        }

                        updateZobrist(fromPos, toPos, piece[toPos.y][toPos.x], pieceID);

                        piece[fromPos.y][fromPos.x] = piece[toPos.y][toPos.x];
                        piece[toPos.y][toPos.x] = pieceID;
                        if (pieceID == 1 || pieceID == 8) {
                            isKingAlive = true;
                        }

                        if (value >= beta) {
                            transformTable.saveTransformTable(fatherZobristKey, fatherZobristKeyCheck, maxDepth - depth, beta, hashfBETA);
                            return beta;
                        }
                        if (value > alpha) {
                            alpha = value;
                            FoundPv = true;
                            hashf = hashfEXACT;
                        }
                    }
                }
            }
        }

        transformTable.saveTransformTable(fatherZobristKey, fatherZobristKeyCheck, maxDepth - depth, alpha, hashf);
        return alpha;
    }*/

    //Alpha-Beta搜索
    /*public static Move getGoodMove(int[][] piece,boolean isRedGo,int depth){
        Move goodMove=new Move(new Pos(-1,-1),new Pos(-1,-1));
        int[][] pieceClone=new int[10][9];
        for(int i=0;i<=9;i++){
            pieceClone[i]=piece[i].clone();
        }

        isMachineRed=isRedGo;
        int value=-INF;
        int alpha=-INF,beta=INF;
        boolean isKingAlive=true;


        for(int i=0;i<=9;i++){
            for(int j=0;j<=8;j++){
                if((isRedGo==false&&pieceClone[i][j]>=1&&pieceClone[i][j]<=7)||(isRedGo==true&&pieceClone[i][j]>=8&&pieceClone[i][j]<=14)){
                    List<Pos> ret=Rule.PossibleMoves(pieceClone,j,i,pieceClone[i][j]);
                    Pos fromPos=new Pos(j,i);
                    Iterator<Pos> it=ret.iterator();
                    while(it.hasNext()){
                        Pos toPos=it.next();

                        int tmp= pieceClone[toPos.y][toPos.x];
                        pieceClone[toPos.y][toPos.x]=pieceClone[fromPos.y][fromPos.x];
                        pieceClone[fromPos.y][fromPos.x]=0;

                        if(tmp==1||tmp==8){
                            isKingAlive=false;
                        }

                        value=-AlphaBeta(pieceClone,(isRedGo==true)?false:true,depth-1,-beta,-alpha,isKingAlive);

                        pieceClone[fromPos.y][fromPos.x]=pieceClone[toPos.y][toPos.x];
                        pieceClone[toPos.y][toPos.x]=tmp;
                        if(tmp==1||tmp==8){
                            isKingAlive=true;
                        }

                        if(value>alpha){
                            alpha=value;
                            goodMove=new Move(fromPos,toPos);
                        }
                    }
                }
            }
        }
        return goodMove;
    }
    public static int AlphaBeta(int[][] piece,boolean isRedGo,int depth,int alpha,int beta,boolean isKingAlive){
        if(isKingAlive==false||depth<=0){
            return evaluate(piece);
        }
        int value=-INF;

        for(int i=0;i<=9;i++){
            for(int j=0;j<=8;j++){
                if((isRedGo==false&&piece[i][j]>=1&&piece[i][j]<=7)||(isRedGo==true&&piece[i][j]>=8&&piece[i][j]<=14)){
                    List<Pos> ret=Rule.PossibleMoves(piece,j,i,piece[i][j]);
                    Pos fromPos=new Pos(j,i);
                    Iterator<Pos> it=ret.iterator();
                    while(it.hasNext()){
                        Pos toPos=it.next();
                        int tmp= piece[toPos.y][toPos.x];
                        piece[toPos.y][toPos.x]=piece[fromPos.y][fromPos.x];
                        piece[fromPos.y][fromPos.x]=0;

                        if(tmp==1||tmp==8){
                            isKingAlive=false;
                        }

                        value=-AlphaBeta(piece,(isRedGo==true)?false:true,depth-1,-beta,-alpha,isKingAlive);

                        piece[fromPos.y][fromPos.x]=piece[toPos.y][toPos.x];
                        piece[toPos.y][toPos.x]=tmp;
                        if(tmp==1||tmp==8){
                            isKingAlive=true;
                        }

                        if(value>=beta){
                            return beta;
                        }
                        if(value>alpha){
                            alpha=value;
                        }
                    }
                }
            }
        }
        return alpha;
    }*/

    public static void updateZobrist(Pos fromPos, Pos toPos, int fromID, int toID) {
        ZobristKey = ZobristKey ^ zobrist.ZobristTable[fromID - 1][fromPos.y][fromPos.x];
        ZobristKeyCheck = ZobristKeyCheck ^ zobrist.ZobristTableCheck[fromID - 1][fromPos.y][fromPos.x];
        ZobristKey = ZobristKey ^ zobrist.ZobristTable[fromID - 1][toPos.y][toPos.x];
        ZobristKeyCheck = ZobristKeyCheck ^ zobrist.ZobristTableCheck[fromID - 1][toPos.y][toPos.x];
        if (toID > 0) {
            ZobristKey = ZobristKey ^ zobrist.ZobristTable[toID - 1][toPos.y][toPos.x];
            ZobristKeyCheck = ZobristKeyCheck ^ zobrist.ZobristTableCheck[toID - 1][toPos.y][toPos.x];
        }
    }

    public static int evaluate(int[][] piece) {
        int score = 0;
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 8; j++) {
                if (piece[i][j] >= 1 && piece[i][j] <= 7) {
                    score += pieceValue[piece[i][j] - 1][i][j];
                } else if (piece[i][j] >= 8 && piece[i][j] <= 14) {
                    score -= pieceValue[piece[i][j] - 1][i][j];
                }
            }
        }
        if (isMachineRed) score *= -1;
        return score;
    }

    public static Move getLegalMove(int piece[][], boolean isRedKing) {
        if (isRedKing == true) {
            for (int i = 0; i <= 9; i++) {
                for (int j = 0; j <= 8; j++) {
                    if (piece[i][j] >= 8 && piece[i][j] <= 14) {
                        List<Pos> moves = Rule.PossibleMoves(piece, j, i, piece[i][j]);
                        Iterator<Pos> it = moves.iterator();
                        while (it.hasNext()) {
                            Pos pos = it.next();
                            int tmp = piece[pos.y][pos.x];
                            if (tmp == 1) {
                                return new Move(new Pos(j, i), pos);
                            }
                            piece[pos.y][pos.x] = piece[i][j];
                            piece[i][j] = 0;
                            if (Rule.isKingDanger(piece, true) == false) {
                                piece[i][j] = piece[pos.y][pos.x];
                                piece[pos.y][pos.x] = tmp;
                                return new Move(new Pos(j, i), pos);
                            }
                            piece[i][j] = piece[pos.y][pos.x];
                            piece[pos.y][pos.x] = tmp;
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i <= 9; i++) {
                for (int j = 0; j <= 8; j++) {
                    if (piece[i][j] >= 1 && piece[i][j] <= 7) {
                        List<Pos> moves = Rule.PossibleMoves(piece, j, i, piece[i][j]);
                        Iterator<Pos> it = moves.iterator();
                        while (it.hasNext()) {
                            Pos pos = it.next();
                            int tmp = piece[pos.y][pos.x];
                            if (tmp == 8) {
                                return new Move(new Pos(j, i), pos);
                            }
                            piece[pos.y][pos.x] = piece[i][j];
                            piece[i][j] = 0;
                            if (Rule.isKingDanger(piece, false) == false) {
                                piece[i][j] = piece[pos.y][pos.x];
                                piece[pos.y][pos.x] = tmp;
                                return new Move(new Pos(j, i), pos);
                            }
                            piece[i][j] = piece[pos.y][pos.x];
                            piece[pos.y][pos.x] = tmp;
                        }
                    }
                }
            }
        }
        return new Move(new Pos(-1, -1), new Pos(-1, -1));
    }
}
