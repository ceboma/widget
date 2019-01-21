package trinix.cd.cebomawigets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.Thread.sleep;

public class ButtonH extends  android.support.v7.widget.AppCompatTextView {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ButtonH(Context context) {
        super(context);

        setEvent();

        init(null);
    }

    private void init(AttributeSet set){
        if(set==null)
            return;

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.ButtonH);
        backGroungColor = ta.getColor(R.styleable.ButtonH_lineColor, backGroungColor);
        fillColorPressed = ta.getColor(R.styleable.ButtonH_backrgroundPressedColor, fillColorPressed);
        fillColor = ta.getColor(R.styleable.ButtonH_backgroundColor, fillColor);
    }

    private int backGroungColor = 0xffe4bb05;

    public void setStrokeColor(int color){
        this.backGroungColor = color;
        invalidate();
    }

    public void setStrokeWidth(int x){
        this.stroke = x;
        invalidate();
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        fillColor = color;

        postInvalidate();
    }

    private int fillColor = 0xfff4d701;
    private int clicked = 0;


    private int fillColorPressed = 0xFFFFFFFF;

    public ButtonH(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setPadding(20, 0, 20, 0);

        setEvent();

        init(attrs);
    }

    private int color;
    public void setBackgroundPressedColor(int color){
        this.fillColorPressed = color;

        postInvalidate();
    }

    private void setEvent(){
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        color = fillColor;
                        fillColor = fillColorPressed;
                        break;
                    case MotionEvent.ACTION_UP:
                        fillColorPressed = fillColor;
                        fillColor = color;
                        break;
                }

                postInvalidate();
                return false;
            }
        });
    }

    private boolean isRunning = false;
    private int stroke = 2;

    private float padding = 0;

    public void onResume(){
        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning){
                    try {
                        sleep(900);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    postInvalidate();
                }
            }
        }).start();
    }

    public void onPause(){
        isRunning = false;
    }

    @Override
    public void onDraw(Canvas canvas){

        int length = 20;
        float leftPercent = length;//getWidth()*5/100;
        float rigthPercent = getWidth()-length;//getWidth()-(getWidth()*5/100);

        paint.setColor( backGroungColor );

        paint.setShader(null);
        paint.setStrokeWidth(6f);


        paint.setColor(backGroungColor);
        Path path1 = new Path();

        path1.moveTo(0, getHeight()/2);
        path1.lineTo(leftPercent, 0);
        path1.lineTo(rigthPercent, 0);
        path1.lineTo(getWidth(), getHeight()/2);
        path1.lineTo(rigthPercent, getHeight());
        path1.lineTo(leftPercent, getHeight());
        path1.lineTo(0, getHeight()/2);

        canvas.drawPath(path1, paint);

        Path fill = new Path();

        paint.setStrokeWidth(1);
        paint.setColor(fillColor);
        fill.moveTo(stroke, getHeight()/2 );
        fill.lineTo(leftPercent+stroke, stroke);
        fill.lineTo(rigthPercent- stroke, stroke);
        fill.lineTo(getWidth()- stroke, getHeight()/2);
        fill.lineTo(rigthPercent- stroke, getHeight()- stroke);
        fill.lineTo(leftPercent+ stroke, getHeight()- stroke);
        fill.lineTo(stroke, getHeight()/2);
        canvas.drawPath(fill, paint);

        super.onDraw(canvas);
    }
}
