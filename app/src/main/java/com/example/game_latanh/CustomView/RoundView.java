package com.example.game_latanh.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.game_latanh.Info.ChessInfo;

public class RoundView extends SurfaceView implements SurfaceHolder.Callback {
    public TutorialThread thread;

    public ChessInfo chessInfo;

    public Paint paint1;
    public Paint paint2;
    public Paint paint3;
    public RectF rectF;
    public int Round_width = 0, Round_height = 0;

    public RoundView(Context context, ChessInfo chessInfo) {
        super(context);
        this.chessInfo = chessInfo;
        getHolder().addCallback(this);
    }

    void init() {
        paint1 = new Paint();
        paint1.setStyle(Paint.Style.FILL);
        paint1.setColor(Color.rgb(222, 184, 135));

        paint2 = new Paint();
        paint2.setTextSize(Round_height / 2);
        paint2.setStrokeWidth(1);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.RED);

        paint3 = new Paint();
        paint3.setTextSize(Round_height / 2);
        paint3.setStrokeWidth(1);
        paint3.setAntiAlias(true);
        paint3.setColor(Color.BLACK);

        rectF = new RectF(0, 0, Round_width, Round_height);
    }

    public void Draw(Canvas canvas) {
        canvas.drawRect(rectF, paint1);
        if (chessInfo.IsRedGo == true) {
            canvas.drawText("Lượt đỏ", (Round_width - Round_height * 2) / 2, Round_height * 7 / 10, paint2);
        } else {
            canvas.drawText("Lượt đen", (Round_width - Round_height * 2) / 2, Round_height * 7 / 10, paint3);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (Round_width == 0) {
            Round_width = MeasureSpec.getSize(widthMeasureSpec) * 4 / 9;
            Round_height = Round_width / 3;
        }
        init();
        setMeasuredDimension(Round_width, Round_height);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.thread = new TutorialThread(getHolder());
        this.thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    class TutorialThread extends Thread {
        public int span = 100;
        public SurfaceHolder surfaceHolder;

        public TutorialThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void run() {
            Canvas c;
            while (true) {
                c = this.surfaceHolder.lockCanvas();
                try {
                    Draw(c);
                } catch (Exception e) {
                }
                if (c != null) this.surfaceHolder.unlockCanvasAndPost(c);
                try {
                    Thread.sleep(span);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
