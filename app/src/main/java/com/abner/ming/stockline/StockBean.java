package com.abner.ming.stockline;

/**
 * Created by AbnerMing on 2018/1/8.
 */

public class StockBean {
    /**
     * 记录x坐标
     * */
    private float stockX;
    /**
     * 记录y坐标
     * */
    private float stockY;
    /**
     * 记录x轴的时间
     * */
    private String time;
    /**
     * 记录y的价格
     * */
    private float price;

    public float getStockX() {
        return stockX;
    }

    public void setStockX(float stockX) {
        this.stockX = stockX;
    }

    public float getStockY() {
        return stockY;
    }

    public void setStockY(float stockY) {
        this.stockY = stockY;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
