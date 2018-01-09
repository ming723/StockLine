package com.abner.ming.stockline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * Created by AbnerMing on 2018/1/8.
 * 行情分时图，基金分时折线图
 */

public class StockLine extends View{

    /**
     * 长按时间
     * */
    private final long longTime=1000;
    /**
     * 控件默认宽高
     * */
    private  final float DEF_WIDTH = 650;
    private  final float DEF_HIGHT = 400;
    /**
     * 数据源
     * */
    private List<StockBean> stockList=new ArrayList<>();

    /**
     * 绘制折线时的路径
     * */
    private  Path mPath;

    /**
     * 画布
     * */
    private Canvas canvas;
    /**
     * 控件的宽高
     * */
    private int mWidth,mHeight;

    public StockLine(Context context) {
        super(context);
        initView(context);
    }

    public StockLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StockLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化一些信息
     * path
     * 背景等
     * */
    private void initView(Context context) {
        setBackgroundColor(Color.parseColor("#222222"));//设置背景为黑色
        mPath=new Path();
        initRectPaint();
        initRectPaintLoding();
        initRectPaintLodingText();
        initInnerXPaint();
        initBrokenPaint();

        initShiLine();

    }
    /**
     * 初始化十字光标
     * */
    private Paint mShiPaint;
    private void initShiLine() {
        mShiPaint = new Paint();
        mShiPaint.setColor(Color.parseColor("#d43c3c"));
        mShiPaint.setStyle(Paint.Style.STROKE);
        mShiPaint.setAntiAlias(true);
        mShiPaint.setStrokeWidth(2);
    }

    /**
     * 初始化折线图画笔
     * */
    private Paint mBrokenPaint;
    private void initBrokenPaint() {
        mBrokenPaint = new Paint();
        mBrokenPaint.setColor(Color.parseColor("#3785d9"));
        mBrokenPaint.setStyle(Paint.Style.STROKE);
        mBrokenPaint.setAntiAlias(true);
        mBrokenPaint.setStrokeWidth(2);
    }
    /**
     * 初始化边框画笔信息
     * */
    private Paint mRectPaint;
    private void initRectPaint(){
        mRectPaint=new Paint();
        mRectPaint.setColor(Color.parseColor("#ffffff"));
        mRectPaint.setAntiAlias(true);//设置抗锯齿
        mRectPaint.setStrokeWidth(2);//线条粗细
        mRectPaint.setStyle(Paint.Style.STROKE);//设置空心
    }

    /**
     * 初始化加载边框画笔信息
     * */
    private Paint mRectPaintLoding;
    private void initRectPaintLoding(){
        mRectPaintLoding=new Paint();
        mRectPaintLoding.setColor(Color.parseColor("#ffffff"));
        mRectPaintLoding.setAntiAlias(true);//设置抗锯齿
        mRectPaintLoding.setStrokeWidth(2);//线条粗细
        mRectPaintLoding.setStyle(Paint.Style.FILL);//设置实心
    }

    /**
     * 初始化文字信息
     * */
    private Paint mRectPaintLodingText;
    private void initRectPaintLodingText(){
        mRectPaintLodingText=new Paint();
        mRectPaintLodingText.setColor(Color.parseColor("#222222"));
        mRectPaintLodingText.setAntiAlias(true);//设置抗锯齿
        mRectPaintLodingText.setStrokeWidth(2);//线条粗细
        mRectPaintLodingText.setStyle(Paint.Style.STROKE);//设置空心
        mRectPaintLodingText.setTextSize(26);
    }

    /**
     * 初始化虚线边框画笔信息
     * */
    private Paint mInnerXPaint;
    private void initInnerXPaint(){
        mInnerXPaint=new Paint();
        mInnerXPaint.setColor(Color.parseColor("#ffffff"));
        mInnerXPaint.setAntiAlias(true);//设置抗锯齿
        mInnerXPaint.setStrokeWidth(2);//线条粗细
        mInnerXPaint.setStyle(Paint.Style.STROKE);//设置空心
        setLayerType(LAYER_TYPE_SOFTWARE, null);//禁用硬件加速
        PathEffect effects = new DashPathEffect(new float[] {15, 5}, 1);
        mInnerXPaint.setPathEffect(effects);
    }
    /**
     * 设置数据
     * */
    private boolean isData=false;//是否有数据

    /**
     * 获取价格中的最大和最小值
     * */
    private float minStock,maxStock;
    private String[] priceLeft=new String[5];
    private String[] bootomTime=new String[3];
    public void setStockData(List<StockBean> list){
        if(!list.isEmpty()){
            isData=true;
            stockList=list;
            mRectPaintLodingText.setColor(0x00000000);//隐藏加载
            mRectPaintLoding.setColor(0x00000000);//隐藏加载
            float minPrice=stockList.get(0).getPrice();
            float maxPrice=stockList.get(0).getPrice();
            for (int a=0;a<stockList.size();a++){
                float p=stockList.get(a).getPrice();
                if(p<minPrice){
                    minPrice=p;
                }
                if(p>maxPrice){
                    maxPrice=p;
                }
            }

            bootomTime[0]=stockList.get(0).getTime();
            bootomTime[1]=stockList.get(stockList.size()/2-1).getTime();
            bootomTime[2]=stockList.get(stockList.size()-1).getTime();
            minStock=minPrice;
            maxStock=maxPrice;

            float p=(maxStock-minStock)/4;
            priceLeft[4]=minStock+"";
            priceLeft[3]=(minStock+p)+"";
            priceLeft[2]=(minStock+2*p)+"";
            priceLeft[1]=(minStock+3*p)+"";
            priceLeft[0]=maxStock+"";
            invalidate();

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == AT_MOST && heightSpecMode == AT_MOST) {
            setMeasuredDimension((int) DEF_WIDTH, (int) DEF_HIGHT);
        } else if (widthSpecMode == AT_MOST) {
            setMeasuredDimension((int) DEF_WIDTH, heightSpecSize);
        } else if (heightSpecMode == AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) DEF_HIGHT);
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas=canvas;
        onDrawRectLoding();
        //有数据就进行绘制
        if(isData){
            onDrawRect();
            onDrawInnerLine();

            onDrawLeftPrice();

            onDrawBootmTime();

            onDrawZheLine();

            onDrawShiLine();
        }
    }
    /**
     * 绘制十字光标
     * */
    private void onDrawShiLine() {
        if(isShowShiLine){
            StockBean bean = stockList.get(0);
            float maxNum = Integer.MAX_VALUE;
            for (int a=0;a<stockList.size();a++){
                StockBean b = stockList.get(a);
                float x = Math.abs(moveX - b.getStockX());
                if(x<maxNum){
                    bean=b;
                    maxNum=x;
                }
            }

            Log.i("onDrawShiLine",moveX+"----"+bean.getStockY()+"----"+bean.getStockX());
            //绘制横线
            canvas.drawLine(rectLeft,bean.getStockY(),mWidth-rectRight,bean.getStockY(),mShiPaint);
            //绘制纵线
            canvas.drawLine(bean.getStockX(),rectTop,bean.getStockX(),mHeight-rectBottom,mShiPaint);

            //绘制右边价格边框
            Rect rectR=new Rect();
            rectR.top=(int)bean.getStockY()-15;
            rectR.bottom=(int)bean.getStockY()+15;
            rectR.left=mWidth-46;
            rectR.right=mWidth-5;
            canvas.drawRect(rectR,mShiPaint);

            //绘制右边价格
            canvas.drawText(bean.getPrice()+"",mWidth-42,bean.getStockY()+5,mRectPaint);

            //绘制底部时间边框
            Rect rectB=new Rect();
            rectB.top=mHeight-46;
            rectB.bottom=mHeight-10;
            rectB.left=(int) bean.getStockX()-35;
            rectB.right=(int) bean.getStockX()+40;
            canvas.drawRect(rectB,mShiPaint);

            //绘制底部时间
            canvas.drawText(bean.getTime()+"",bean.getStockX()-25,mHeight-25,mRectPaint);

        }
    }

    /**
     * 绘制折线图
     * */
    private void onDrawZheLine() {
       float timeX= ( mWidth-rectLeft-rectRight)/stockList.size();//一份时间所占的宽度
        float priceY= ( mHeight-rectTop-rectBottom)/(maxStock-minStock);//一份价格所占的高度
        for (int a=0;a<stockList.size();a++){
            StockBean b = stockList.get(a);
            float x=timeX*a;
            float y=b.getPrice()-minStock;
            if(a==0){
                mPath.moveTo(rectLeft,mHeight-rectBottom);
            }
            Log.i("onDrawZheLine",priceY+"----"+timeX+"-----"+x+"-------"+y);
            float xLine=x+rectLeft;
            float yLine=mHeight-(rectBottom+y*priceY);
            b.setStockX(xLine);
            b.setStockY(yLine);
            mPath.lineTo(xLine,yLine);
        }
        canvas.drawPath(mPath,mBrokenPaint);
    }

    /**
     * 绘制底部时间，我这里只显示了三个
     * */
    private void onDrawBootmTime() {
        float widthTime=(mWidth-rectLeft-rectRight)/2;
        for (int a=0;a<3;a++){
            float time=widthTime*a;
            canvas.drawText(bootomTime[a],time,mHeight-20,mRectPaint);
        }
    }

    /**
     * 绘制边框左边的价格
     *返回的数据源，我们这里分成5个区间，根据返回的价格最大减去最小来确定区间的大小
     * 一般价格都是从下到上增大
     *
     * */
    private void onDrawLeftPrice() {
        int heightInner=(mHeight-rectBottom)/4;
        for (int a=0;a<priceLeft.length;a++){
            float leftPrice=heightInner*a;
            if (a==0){
                leftPrice=rectTop+5;
            }
            canvas.drawText(priceLeft[a],rectLeft-35,leftPrice,mRectPaint);
        }

    }

    /**
     * 绘制虚线，三条
     * 由于只绘制中间，a=1,减少开始和结束时的重复绘制
     * */
    private void onDrawInnerLine() {
        int heightInner=(mHeight-rectTop-rectBottom)/4;
        for (int a=1;a<4;a++){
            canvas.drawLine(rectLeft,heightInner*a,mWidth-rectRight,heightInner*a,mInnerXPaint);
        }
    }

    /**
     * 绘制边框 上下两条实线
     * */
    private int rectLeft=50, rectTop=10,rectRight=50,rectBottom=50;
    private void onDrawRect() {
        canvas.drawLine(rectLeft,rectTop,mWidth-rectRight,rectTop,mRectPaint);
        canvas.drawLine(rectLeft,mHeight-rectBottom,mWidth-rectRight,mHeight-rectBottom,mRectPaint);
    }

    /**
     * 绘制加载框
     * */
    private String rectLoding="正在加载……";
    private void onDrawRectLoding() {
        Rect rect=new Rect(0,0,mWidth,mHeight);
        canvas.drawRect(rect,mRectPaintLoding);
        canvas.drawText(rectLoding,mWidth/2-mRectPaintLodingText.measureText(rectLoding) / 2,mHeight/2,mRectPaintLodingText);
    }


    private long downTime;//按下时间

    private boolean isShowShiLine=false;//是否显示十字光标

    private float moveX,moveY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downTime = event.getDownTime();
                break;
            case MotionEvent.ACTION_UP:
                hiddenLongPressView();
                break;
            case MotionEvent.ACTION_MOVE:
                long eventTime = event.getEventTime();
                if((eventTime-downTime)>longTime){//长按
                    moveX=event.getX();
                    moveY=event.getY();
                    isShowShiLine=true;
                    invalidate();
                }
                break;
        }
        return true;
    }

    /**
     * 抬起后過一秒后隐藏十字光标
     * */
    private void hiddenLongPressView() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isShowShiLine = false;
                invalidate();
            }
        }, 1000);
    }
}
