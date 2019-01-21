package trinix.cd.cebomawigets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class HexagoneButton extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int backgroundColor = 0xffffffff;
    private int strokeColor = 0xff000000;
    private int notification = 5;
    private int notificationFillColor = 0xffff0000;
    private int notificationTextColor = 0xffffffff;
    private int paddingStroke = 5;
    private int widthStroke = 2;

    private Bitmap image;
    private Context mContext;

    public void setWidthStroke(int width){
        widthStroke = width;

        invalidate();
    }

    public void setPaddingStroke(int padding){
        paddingStroke = padding;

        invalidate();
    }

    public void setStrokeColor(int color){
        strokeColor = color;
    }

    public void setBackgroundImage(Bitmap bitmap){
        image = bitmap;

        invalidate();
    }

    public void setBackgroundImage(int res){
        if(mContext!=null)
            image = BitmapFactory.decodeResource(mContext.getResources(), res);

        invalidate();
    }

    private void setNotificationFillColor(int color){
        notificationFillColor = color;
        invalidate();
    }

    public void setNotification(int message){
        notification = message;
        invalidate();
    }

    public void setFillColor(int color){
        backgroundColor = color;
        invalidate();
    }

    public HexagoneButton(Context context) {
        super(context);

        init(null);
    }

    public HexagoneButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public HexagoneButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    @RequiresApi(21)
    public HexagoneButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(AttributeSet set){
        if(set==null)
            return;

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.ProfilButton);
        backgroundColor = ta.getColor(R.styleable.ProfilButton_fillColor, backgroundColor);
        try {
            Drawable drawable = ta.getDrawable(R.styleable.ProfilButton_backgroundImage);
            if(drawable instanceof BitmapDrawable){
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                if(bitmapDrawable.getBitmap()!=null)
                    image = bitmapDrawable.getBitmap();
            }else{
                image = null;
            }
        }catch (Exception e){
            if(BuildConfig.DEBUG)
                e.printStackTrace();

            image = null;
        }

        strokeColor = ta.getColor(R.styleable.ProfilButton_strokeColor, strokeColor);
        widthStroke = ta.getDimensionPixelSize(R.styleable.ProfilButton_stokeWidth, widthStroke);
        paddingStroke = ta.getDimensionPixelOffset(R.styleable.ProfilButton_strokePadding, paddingStroke);
        notificationFillColor = ta.getColor(R.styleable.ProfilButton_notificationBackgroundColor, notificationFillColor);
        notificationTextColor = ta.getColor(R.styleable.ProfilButton_notificationTextColor, notificationTextColor);

        ta.recycle();
    }

    @Override
    public void onDraw(Canvas canvas){

        float ratio = 0.90f;
        float height = getHeight() * ratio;
        float width = getWidth() * ratio;

        float marginHeight = getHeight() - height;
        float marginWidth = 0;

        float rayon = (height>width) ? width/2 : height/2;

        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        if(image!=null){
            image = Bitmap.createScaledBitmap(image, (int) width, (int)height, true);
            canvas.drawBitmap(image, marginWidth, getHeight()-height, paint);
        }

        float cy, cx;
        cy = rayon + marginHeight;
        cx = rayon + marginWidth;

        paint.setColor(backgroundColor);
        paint.setStrokeWidth(3f);

        Path path = new Path();
        path.setFillType(Path.FillType.INVERSE_WINDING);

        for(int i=0; i<=360; i+=60){

            float a = (float) (i*Math.PI/180);
            double x = (Math.cos(a) * rayon) + marginWidth + rayon ;
            double y = (Math.sin(a) * rayon) + marginHeight + rayon ;

            if(i==0){
                path.moveTo((float) x, (float) y);
            }else{
                path.lineTo((float) x, (float) y);
                if(i==60){
                    path.lineTo(getWidth(),0);
                    path.lineTo((float) x, (float) y);
                }

                if(i==180){
                    path.lineTo((float) x, getHeight());
                    path.lineTo((float) x, (float) y);
                }
            }
        }

        canvas.drawPath(path, paint);

        Path stroke = new Path();
        float rayon2 = rayon - (rayon * paddingStroke/100);

        System.out.println("rayon 2 : " + rayon2 + " -- rayon1 : " + rayon );
        for(int i=0; i<=360; i+=60){

            float a = (float) (i*Math.PI/180);
            double x = (Math.cos(a) * rayon2) + marginWidth + rayon ;
            double y = (Math.sin(a) * rayon2) + marginHeight + rayon ;

            if(i==0){
                stroke.moveTo((float) x, (float) y);
            }else{
                stroke.lineTo((float) x, (float) y);
            }
        }

        paint.setColor(strokeColor);
        paint.setStrokeWidth(widthStroke);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawPath(stroke, paint);


        if(notification>0){
            paint.setStrokeWidth(0);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(notificationFillColor);
            paint.setFakeBoldText(true);

            float radius = getHeight()*0.20f;
            cx = getWidth()-radius;
            cy = radius;

            canvas.drawCircle(cx, cy, radius, paint);

            paint.setColor(notificationTextColor);
            Rect rect = new Rect();

            String message = "" + notification;

            float textSize = radius*2*0.70f;
            paint.setTextSize(textSize);
            paint.getTextBounds(message, 0, message.length(), rect);

            float h = radius*2;

            canvas.drawText(message, getWidth()-radius-(rect.width()/2), h-rect.height() + (rect.height()/2), paint);
        }
    }

    private class Pt {
        float x;
        float y;

        public Pt(float x, float y){
            this.x = x;
            this.y = y;
        }

        public Pt(){

        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}
