package com.example.game_latanh;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Looper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.game_latanh.AICore.AI;
import com.example.game_latanh.AICore.KnowledgeBase;
import com.example.game_latanh.AICore.Move;
import com.example.game_latanh.AICore.TransformTable;
import com.example.game_latanh.ChessMove.Rule;
import com.example.game_latanh.CustomDialog.CommonDialog;
import com.example.game_latanh.CustomDialog.RetryDialog;
import com.example.game_latanh.CustomDialog.SettingDialog_PvM;
import com.example.game_latanh.CustomView.RoundView;
import com.example.game_latanh.Info.ChessInfo;
import com.example.game_latanh.Info.InfoSet;
import com.example.game_latanh.Info.Pos;
import com.example.game_latanh.CustomView.ChessView;

import static com.example.game_latanh.AICore.AI.getBestMove;
import static com.example.game_latanh.HomeActivity.MIN_CLICK_DELAY_TIME;
import static com.example.game_latanh.HomeActivity.curClickTime;
import static com.example.game_latanh.HomeActivity.lastClickTime;
import static com.example.game_latanh.HomeActivity.setting;
import static com.example.game_latanh.HomeActivity.sharedPreferences;


public class PvMActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    public RelativeLayout relativeLayout;
    public ChessInfo chessInfo;
    public InfoSet infoSet;
    public ChessView chessView;
    public RoundView roundView;
    public static KnowledgeBase knowledgeBase;
    public static TransformTable transformTable;

    public Thread AIThread = new Thread(new Runnable() {
        @Override
        public void run() {
            AIMove(chessInfo.IsRedGo, chessInfo.status);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvm);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        chessInfo = new ChessInfo();
        infoSet = new InfoSet();
        transformTable = new TransformTable();
        knowledgeBase = new KnowledgeBase();

        roundView = new RoundView(this, chessInfo);
        relativeLayout.addView(roundView);

        RelativeLayout.LayoutParams paramsRound = (RelativeLayout.LayoutParams) roundView.getLayoutParams();
        paramsRound.addRule(RelativeLayout.CENTER_IN_PARENT);
        paramsRound.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        paramsRound.setMargins(30, 30, 30, 30);
        roundView.setLayoutParams(paramsRound);
        roundView.setId(R.id.roundView);

        chessView = new ChessView(this, chessInfo);
        chessView.setOnTouchListener(this);
        relativeLayout.addView(chessView);

        RelativeLayout.LayoutParams paramsChess = (RelativeLayout.LayoutParams) chessView.getLayoutParams();
        paramsChess.addRule(RelativeLayout.BELOW, R.id.roundView);
        chessView.setLayoutParams(paramsChess);
        chessView.setId(R.id.chessView);

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout buttonGroup = (RelativeLayout) inflater.inflate(R.layout.button_group, relativeLayout, false);
        relativeLayout.addView(buttonGroup);

        RelativeLayout.LayoutParams paramsV = (RelativeLayout.LayoutParams) buttonGroup.getLayoutParams();
        paramsV.addRule(RelativeLayout.BELOW, R.id.chessView);
        buttonGroup.setLayoutParams(paramsV);

        for (int i = 0; i < buttonGroup.getChildCount(); i++) {
            Button btn = (Button) buttonGroup.getChildAt(i);
            btn.setOnClickListener(this);
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        lastClickTime = System.currentTimeMillis();
        if (lastClickTime - curClickTime < MIN_CLICK_DELAY_TIME) {
            return false;
        }
        curClickTime = lastClickTime;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            if (chessInfo.status == 1) {
                if (x >= 0 && x <= chessView.Board_width && y >= 0 && y <= chessView.Board_height) {
                    chessInfo.Select = getPos(event);
                    Pos realPos = Rule.reversePos(new Pos(chessInfo.Select[0], chessInfo.Select[1]), chessInfo.IsReverse);
                    int i = realPos.x, j = realPos.y;

                    if (i >= 0 && i <= 8 && j >= 0 && j <= 9) {
                        if (chessInfo.IsRedGo == true && setting.isPlayerRed == true) {
                            if (chessInfo.IsChecked == false) {
                                if (chessInfo.piece[j][i] >= 8 && chessInfo.piece[j][i] <= 14) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.IsChecked = true;
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                }
                            } else {
                                if (chessInfo.piece[j][i] >= 8 && chessInfo.piece[j][i] <= 14) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                } else if (chessInfo.ret.contains(new Pos(i, j))) {
                                    int tmp = chessInfo.piece[j][i];
                                    chessInfo.piece[j][i] = chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x];
                                    chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = 0;
                                    if (Rule.isKingDanger(chessInfo.piece, true)) {
                                        chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = chessInfo.piece[j][i];
                                        chessInfo.piece[j][i] = tmp;
                                        Toast toast = Toast.makeText(PvMActivity.this, "Tướng đang bị chiếu", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    } else {
                                        chessInfo.IsChecked = false;
                                        chessInfo.IsRedGo = false;
                                        chessInfo.curPos = new Pos(i, j);

                                        chessInfo.updateAllInfo(chessInfo.prePos, chessInfo.curPos, chessInfo.piece[j][i], tmp);

                                        try {
                                            infoSet.pushInfo(chessInfo);
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                        int key = 0;
                                        if (Rule.isKingDanger(chessInfo.piece, false)) {
                                            key = 1;
                                        }
                                        if (Rule.isDead(chessInfo.piece, false)) {
                                            key = 2;
                                        }
                                        if (key == 1) {
                                            Toast toast = Toast.makeText(PvMActivity.this, "Chiếu Tướng", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        } else if (key == 2) {
                                            chessInfo.status = 2;
                                            resultDialog("Quân Đỏ Thắng");
                                        }

                                        if (chessInfo.status == 1) {
                                            if (chessInfo.peaceRound >= 60 ||                                    // Đi 60 nước chưa ăn được con nào -> hòa
                                                    chessInfo.attackNum_B == 0 && chessInfo.attackNum_R == 0 || // 2 bên không còn quân tấn công -> hòa
                                                    infoSet.ZobristInfo.get(chessInfo.ZobristKeyCheck)>=4)      // Vị trí lặp lại 4 lần -> hòa
                                            {
                                                chessInfo.status = 2;
                                                resultDialog("Cờ Hòa");
                                            }
                                        }

                                        AIThread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(400);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                chessInfo.isMachine = true;

                                                AIMove(chessInfo.IsRedGo, chessInfo.status);
                                            }
                                        });
                                        AIThread.start();
                                    }
                                }
                            }
                        } else if (chessInfo.IsRedGo == false && setting.isPlayerRed == false) {
                            if (chessInfo.IsChecked == false) {
                                if (chessInfo.piece[j][i] >= 1 && chessInfo.piece[j][i] <= 7) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.IsChecked = true;
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                }
                            } else {
                                if (chessInfo.piece[j][i] >= 1 && chessInfo.piece[j][i] <= 7) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                } else if (chessInfo.ret.contains(new Pos(i, j))) {
                                    int tmp = chessInfo.piece[j][i];
                                    chessInfo.piece[j][i] = chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x];
                                    chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = 0;
                                    if (Rule.isKingDanger(chessInfo.piece, false)) {
                                        chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = chessInfo.piece[j][i];
                                        chessInfo.piece[j][i] = tmp;
                                        Toast toast = Toast.makeText(PvMActivity.this, "Tướng đang bị chiếu", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    } else {
                                        chessInfo.IsChecked = false;
                                        chessInfo.IsRedGo = true;
                                        chessInfo.curPos = new Pos(i, j);

                                        chessInfo.updateAllInfo(chessInfo.prePos, chessInfo.curPos, chessInfo.piece[j][i], tmp);

                                        try {
                                            infoSet.pushInfo(chessInfo);
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                        int key = 0;
                                        if (Rule.isKingDanger(chessInfo.piece, true)) {
                                            key = 1;
                                        }
                                        if (Rule.isDead(chessInfo.piece, true)) {
                                            key = 2;
                                        }
                                        if (key == 1) {
                                            Toast toast = Toast.makeText(PvMActivity.this, "Chiếu Tướng", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        } else if (key == 2) {
                                            chessInfo.status = 2;
                                            resultDialog("Quân Đen Thắng");
                                        }

                                        if (chessInfo.status == 1) {
                                            if (chessInfo.peaceRound >= 60 ||                                    // Đi 60 nước chưa ăn được con nào -> hòa
                                                    chessInfo.attackNum_B == 0 && chessInfo.attackNum_R == 0 || // 2 bên không còn quân tấn công -> hòa
                                                    infoSet.ZobristInfo.get(chessInfo.ZobristKeyCheck)>=4)      // Vị trí lặp lại 4 lần -> hòa
                                            {
                                                chessInfo.status = 2;
                                                resultDialog("Cờ Hòa");
                                            }
                                        }

                                        AIThread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(400);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                chessInfo.isMachine = true;

                                                AIMove(chessInfo.IsRedGo, chessInfo.status);
                                            }
                                        });
                                        AIThread.start();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public int[] getPos(MotionEvent e) {
        int[] pos = new int[2];
        double x = e.getX();
        double y = e.getY();
        int[] dis = new int[]{
                chessView.Scale(3), chessView.Scale(41), chessView.Scale(80), chessView.Scale(85)
        };
        x = x - dis[0];
        y = y - dis[1];
        if (x % dis[3] <= dis[2] && y % dis[3] <= dis[2]) {
            pos[0] = (int) Math.floor(x / dis[3]);
            pos[1] = (int) Math.floor(y / dis[3]);
            if (pos[0] >= 9 || pos[1] >= 10) {
                pos[0] = pos[1] = -1;
            }
        } else {
            pos[0] = pos[1] = -1;
        }
        return pos;
    }

    @Override
    public void onClick(View view) {
        lastClickTime = System.currentTimeMillis();
        if (lastClickTime - curClickTime < MIN_CLICK_DELAY_TIME) {
            return;
        }
        curClickTime = lastClickTime;

        int viewId = view.getId();
        if (viewId == R.id.btn_retry || viewId == R.id.btn_recall) {
            if (chessInfo.isMachine && chessInfo.status == 1) {
                Toast toast = Toast.makeText(PvMActivity.this, "Xin vui lòng đợi máy tính suy nghĩ", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                if (viewId == R.id.btn_retry) {
                    final RetryDialog retryDialog = new RetryDialog(this);
                    retryDialog.setOnClickBottomListener(new RetryDialog.OnClickBottomListener() {
                        @Override
                        public void onPositiveClick() {
                            try {
                                chessInfo.setInfo(new ChessInfo());
                                infoSet.newInfo();
                                transformTable.transformInfos.clear();
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if (setting.isPlayerRed != retryDialog.isPlayerRed) {
                                setting.isPlayerRed = retryDialog.isPlayerRed;
                                editor.putBoolean("isPlayerRed", retryDialog.isPlayerRed);
                                editor.commit();
                            }
                            retryDialog.dismiss();
                            chessInfo.IsReverse = (setting.isPlayerRed) ? false : true;
                            if (chessInfo.IsReverse) {
                                try {
                                    infoSet.curInfo.setInfo(chessInfo);
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!setting.isPlayerRed) {
                                try {
                                    Thread.sleep(400);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                AIFirstGo();
                            }
                        }
                        @Override
                        public void onNegtiveClick() {
                            retryDialog.dismiss();
                        }
                    });
                    retryDialog.show();
                } else if (viewId == R.id.btn_recall) {
                    int cnt = 0;
                    int total = 2;
                    if (chessInfo.status == 2 && chessInfo.isMachine) {
                        total = 1;
                    }
                    if (infoSet.preInfo.size() < total) {
                        return;
                    }
                    while (!infoSet.preInfo.empty()) {
                        ChessInfo tmp = infoSet.preInfo.pop();
                        cnt++;
                        try {
                            infoSet.recallZobristInfo(chessInfo.ZobristKeyCheck);
                            chessInfo.setInfo(tmp);
                            infoSet.curInfo.setInfo(tmp);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        if (cnt >= total) {
                            break;
                        }
                    }
                }
            }
        } else if (viewId == R.id.btn_setting) {
            final SettingDialog_PvM settingDialog_pvm = new SettingDialog_PvM(this);
            settingDialog_pvm.setOnClickBottomListener(new SettingDialog_PvM.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    boolean flag = false;
                    if (setting.mLevel != settingDialog_pvm.mLevel) {
                        setting.mLevel = settingDialog_pvm.mLevel;
                        editor.putInt("mLevel", settingDialog_pvm.mLevel);
                        flag = true;
                    }
                    if (flag) {
                        editor.commit();
                    }
                    settingDialog_pvm.dismiss();
                }

                @Override
                public void onNegtiveClick() {
                    settingDialog_pvm.dismiss();
                }
            });
            settingDialog_pvm.show();
        } else if (viewId == R.id.btn_back) {
            final CommonDialog backDialog = new CommonDialog(this, "Thoát", "Xác nhận rằng bạn muốn thoát?");
            backDialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    backDialog.dismiss();
                    finish();
                }

                @Override
                public void onNegtiveClick() {
                    backDialog.dismiss();
                }
            });
            backDialog.show();
        }

    }

    public void AIMove(boolean isRed, int status) {
        if (status != 1) return;
        int depth = setting.mLevel * 2;
        if (isRed == true) {
            Move move = knowledgeBase.readBestMoves(chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, depth);
            if (move == null) {
                long t1 = System.currentTimeMillis();
                move = getBestMove(chessInfo.piece, true, depth, chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, infoSet.ZobristInfo);
                long t2 = System.currentTimeMillis();
                Log.i("Thông báo", "????" + String.valueOf(t2 - t1) + "ms");
                knowledgeBase.saveBestMove(chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, depth, move);
            }

            Pos fromPos = move.fromPos;
            Pos toPos = move.toPos;
            int tmp = chessInfo.piece[toPos.y][toPos.x];
            chessInfo.piece[toPos.y][toPos.x] = chessInfo.piece[fromPos.y][fromPos.x];
            chessInfo.piece[fromPos.y][fromPos.x] = 0;
            chessInfo.IsChecked = false;
            chessInfo.IsRedGo = false;
            chessInfo.Select = new int[]{-1, -1};
            chessInfo.ret.clear();
            chessInfo.prePos = new Pos(fromPos.x, fromPos.y);
            chessInfo.curPos = new Pos(toPos.x, toPos.y);

            chessInfo.updateAllInfo(chessInfo.prePos, chessInfo.curPos, chessInfo.piece[toPos.y][toPos.x], tmp);
            chessInfo.isMachine = false;

            try {
                infoSet.pushInfo(chessInfo);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }


            int key = 0;
            if (Rule.isKingDanger(chessInfo.piece, false)) {
                key = 1;
            }
            if (Rule.isDead(chessInfo.piece, false)) {
                key = 2;
            }
            if (key == 1) {
                Looper.prepare();
                Toast toast = Toast.makeText(PvMActivity.this, "Chiếu tướng", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Looper.loop();
            } else if (key == 2) {
                chessInfo.status = 2;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultDialog("Quân Đỏ Thắng");
                    }
                });
            }

            if (chessInfo.status == 1) {
                if (chessInfo.peaceRound >= 60 ||
                        chessInfo.attackNum_B == 0 && chessInfo.attackNum_R == 0 ||
                        infoSet.ZobristInfo.get(chessInfo.ZobristKeyCheck) >= 4)
                {
                    chessInfo.status = 2;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultDialog("Cờ hòa");
                        }
                    });
                }
            }
        } else {
            Move move = knowledgeBase.readBestMoves(chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, depth);
            if (move == null) {
                long t1 = System.currentTimeMillis();
                move = getBestMove(chessInfo.piece, false, depth, chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, infoSet.ZobristInfo);
                long t2 = System.currentTimeMillis();
                Log.i("Thông báo", "????" + String.valueOf(t2 - t1) + "ms");
                knowledgeBase.saveBestMove(chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, depth, move);
            }

            Pos fromPos = move.fromPos;
            Pos toPos = move.toPos;
            int tmp = chessInfo.piece[toPos.y][toPos.x];
            chessInfo.piece[toPos.y][toPos.x] = chessInfo.piece[fromPos.y][fromPos.x];
            chessInfo.piece[fromPos.y][fromPos.x] = 0;
            chessInfo.IsChecked = false;
            chessInfo.IsRedGo = true;
            chessInfo.Select = new int[]{-1, -1};
            chessInfo.ret.clear();
            chessInfo.prePos = new Pos(fromPos.x, fromPos.y);
            chessInfo.curPos = new Pos(toPos.x, toPos.y);

            chessInfo.updateAllInfo(chessInfo.prePos, chessInfo.curPos, chessInfo.piece[toPos.y][toPos.x], tmp);
            chessInfo.isMachine = false;

            try {
                infoSet.pushInfo(chessInfo);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            int key = 0;
            if (Rule.isKingDanger(chessInfo.piece, true)) {
                key = 1;
            }
            if (Rule.isDead(chessInfo.piece, true)) {
                key = 2;
            }
            if (key == 1) {
                Looper.prepare();
                Toast toast = Toast.makeText(PvMActivity.this, "Chiếu tướng", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Looper.loop();
            } else if (key == 2) {
                chessInfo.status = 2;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultDialog("Quân Đen Thắng");
                    }
                });
            }

            if (chessInfo.status == 1) {
                if (chessInfo.peaceRound >= 60 ||
                        chessInfo.attackNum_B == 0 && chessInfo.attackNum_R == 0 ||
                        infoSet.ZobristInfo.get(chessInfo.ZobristKeyCheck) >= 4)
                {
                    chessInfo.status = 2;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultDialog("Cờ hòa");
                        }
                    });
                }
            }
        }
    }

    public void AIFirstGo() {
        Move[] firstMoves = new Move[8];
        firstMoves[0] = new Move(new Pos(1, 9), new Pos(2, 7));
        firstMoves[1] = new Move(new Pos(7, 9), new Pos(6, 7));
        firstMoves[2] = new Move(new Pos(2, 9), new Pos(4, 7));
        firstMoves[3] = new Move(new Pos(6, 9), new Pos(4, 7));
        firstMoves[4] = new Move(new Pos(1, 7), new Pos(4, 7));
        firstMoves[5] = new Move(new Pos(7, 7), new Pos(4, 7));
        firstMoves[6] = new Move(new Pos(2, 6), new Pos(2, 5));
        firstMoves[7] = new Move(new Pos(6, 6), new Pos(6, 5));

        int num = (int) (Math.random() * firstMoves.length);
        Move firstMove = firstMoves[num];
        Pos fromPos = firstMove.fromPos;
        Pos toPos = firstMove.toPos;
        int tmp = chessInfo.piece[toPos.y][toPos.x];
        chessInfo.piece[toPos.y][toPos.x] = chessInfo.piece[fromPos.y][fromPos.x];
        chessInfo.piece[fromPos.y][fromPos.x] = 0;
        chessInfo.IsChecked = false;
        chessInfo.IsRedGo = false;
        chessInfo.Select = new int[]{-1, -1};
        chessInfo.ret.clear();
        chessInfo.prePos = new Pos(fromPos.x, fromPos.y);
        chessInfo.curPos = new Pos(toPos.x, toPos.y);

        chessInfo.updateAllInfo(chessInfo.prePos, chessInfo.curPos, chessInfo.piece[toPos.y][toPos.x], tmp);

        try {
            infoSet.pushInfo(chessInfo);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final CommonDialog backDialog = new CommonDialog(this, "Thoát", "Xác nhận rằng bạn muốn thoát?");
            backDialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {

                @Override
                public void onPositiveClick() {
                    backDialog.dismiss();
                    finish();
                }

                @Override
                public void onNegtiveClick() {
                    backDialog.dismiss();
                }
            });
            backDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        if (chessInfo.isMachine) {
            if (AIThread.isAlive() == false) {
                AIThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AIMove(chessInfo.IsRedGo, chessInfo.status);
                    }
                });
                AIThread.start();
            }
        }
        super.onStart();
    }

    public void resultDialog(String result){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PvMActivity.this);
        alertDialog.setTitle("Thong bao");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage(result);
        final RetryDialog retryDialog = new RetryDialog(this);
        alertDialog.setPositiveButton("NewGame", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 try {
                        chessInfo.setInfo(new ChessInfo());
                        infoSet.newInfo();
                        transformTable.transformInfos.clear();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (setting.isPlayerRed != retryDialog.isPlayerRed) {
                        setting.isPlayerRed = retryDialog.isPlayerRed;
                        editor.putBoolean("isPlayerRed", retryDialog.isPlayerRed);
                        editor.commit();
                    }
                    retryDialog.dismiss();
                    chessInfo.IsReverse = (setting.isPlayerRed) ? false : true;
                    if (chessInfo.IsReverse) {
                        try {
                            infoSet.curInfo.setInfo(chessInfo);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!setting.isPlayerRed) {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        AIFirstGo();
                    }
            }
        });
        alertDialog.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
}
