package com.abner.ming.stockline;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private  final int DELAYEDTIME=1000;
    private StockLine mStockLine;

    private List<StockBean> mList=new ArrayList<>();
    /**
     * 模拟时间数据
     * */
    private String[] times={"2018/1/1","2018/1/2","2018/1/3","2018/1/4","2018/1/5","2018/1/6","2018/1/7","2018/1/8","2018/1/9","2018/1/10","2018/1/11","2018/1/12","2018/1/13","2018/1/14","2018/1/15"
            ,"2018/1/16","2018/1/17","2018/1/18","2018/1/19","2018/1/20","2018/1/21","2018/1/22","2018/1/23","2018/1/24","2018/1/25","2018/1/26","2018/1/27","2018/1/28","2018/1/29","2018/1/30"};
    /**
     * 模拟价格数据
     * */
    private int[] prices={121,125,128,122,150,168,133,166,120,166,188,200,220,215,210,190,180,148,136,158,168,198,120,135,168,133,200,230,200,188};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStockLine=(StockLine)findViewById(R.id.stockline);
        createData();
    }

    private void createData() {
        for (int a=0;a<times.length;a++){
            StockBean bean=new StockBean();
            bean.setTime(times[a]);
            bean.setPrice(prices[a]);
            mList.add(bean);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(DELAYEDTIME,2000);
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DELAYEDTIME:
                    mStockLine.setStockData(mList);
                    break;
            }
        }
    };
}
