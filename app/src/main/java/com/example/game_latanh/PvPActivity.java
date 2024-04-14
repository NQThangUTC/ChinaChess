package com.example.game_latanh;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.game_latanh.ChessMove.Rule;
import com.example.game_latanh.CustomDialog.CommonDialog;
import com.example.game_latanh.CustomView.ChessView;
import com.example.game_latanh.CustomView.RoundView;
import com.example.game_latanh.Info.ChessInfo;
import com.example.game_latanh.Info.InfoSet;
import com.example.game_latanh.Info.Pos;

import static com.example.game_latanh.HomeActivity.MIN_CLICK_DELAY_TIME;
import static com.example.game_latanh.HomeActivity.curClickTime;
import static com.example.game_latanh.HomeActivity.lastClickTime;



public class PvPActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private RelativeLayout relativeLayout;
    private ChessInfo chessInfo;
    private InfoSet infoSet;
    private ChessView chessView;
    private RoundView roundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        chessInfo = new ChessInfo();
        infoSet = new InfoSet();

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
        RelativeLayout buttonGroup = (RelativeLayout) inflater.inflate(R.layout.button_grouppvp, relativeLayout, false);
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
                    int i = chessInfo.Select[0], j = chessInfo.Select[1];
                    if (i >= 0 && i <= 8 && j >= 0 && j <= 9) {
                        if (chessInfo.IsRedGo == true) {
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
                                        Toast toast = Toast.makeText(PvPActivity.this, "Tướng đang bị chiếu", Toast.LENGTH_SHORT);
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
                                            Toast toast = Toast.makeText(PvPActivity.this, "Chiếu Tướng", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        } else if (key == 2) {
                                            chessInfo.status = 2;
                                            resultDialog("Đỏ Thắng");
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
                                    }
                                }
                            }
                        } else {
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
                                        Toast toast = Toast.makeText(PvPActivity.this, "Tướng đang bị chiếu", Toast.LENGTH_SHORT);
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
                                            Toast toast = Toast.makeText(PvPActivity.this, "Chiếu Tướng", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        } else if (key == 2) {
                                            chessInfo.status = 2;
                                            resultDialog("Đen thắng");
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
        if (viewId == R.id.btn_retry) {
            final CommonDialog retryDialog = new CommonDialog(this, "Trò chơi mới", "Bắt đầu game mới?\n");
            retryDialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    try {
                        chessInfo.setInfo(new ChessInfo());
                        infoSet.newInfo();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    retryDialog.dismiss();
                }

                @Override
                public void onNegtiveClick() {
                    retryDialog.dismiss();
                }
            });
            retryDialog.show();
        } else if (viewId == R.id.btn_recall) {
            if (!infoSet.preInfo.empty()) {
                ChessInfo tmp = infoSet.preInfo.pop();
                try {
                    infoSet.recallZobristInfo(chessInfo.ZobristKeyCheck);
                    chessInfo.setInfo(tmp);
                    infoSet.curInfo.setInfo(tmp);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        lastClickTime = System.currentTimeMillis();
        if (lastClickTime - curClickTime < MIN_CLICK_DELAY_TIME) {
            return true;
        }
        curClickTime = lastClickTime;

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
        super.onStart();
    }
    public void resultDialog(String result){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thong bao");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage(result);
        alertDialog.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    chessInfo.setInfo(new ChessInfo());
                    infoSet.newInfo();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
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
