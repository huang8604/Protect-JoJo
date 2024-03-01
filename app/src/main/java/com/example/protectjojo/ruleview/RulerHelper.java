package com.example.protectjojo.ruleview;

import android.animation.ArgbEvaluator;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;


public class RulerHelper {

    private int currentLine = -1;
    private float offSet = 0.1f;
    private int lineNumbers;
    private List<Integer> mPoints;
    private int mCenterPointX;
    private ScrollChange scrollChange;


    public  static  final int normalColor = Color.parseColor("#8BC846");
    public static final int warmColor = Color.parseColor("#FF7A32");
    public static final int feverColor = Color.parseColor("#FF3A3A");
    private String currentText;

    public static final float normalLevel = 37.5f;
    public static final float warmlLevel = 38.5f;

    public static final float avaLevel = (normalLevel + warmlLevel)/2;
    public static int getBodyTempColor(float temperature) {
        if(temperature < normalLevel){
            return normalColor;
        }else if (temperature > warmlLevel){
            return feverColor;
        }else{
           if( temperature >= normalLevel &&  temperature < avaLevel){
               return getIntermediateColor(  (temperature - normalLevel)/(avaLevel- normalLevel),normalColor,warmColor );
           }else{
               return getIntermediateColor(  (temperature - avaLevel)/(warmlLevel -avaLevel),warmColor,feverColor );
           }
        }
    }

    public static int getIntermediateColor(float fraction, int startColor, int endColor) {
        ArgbEvaluator evaluator = new ArgbEvaluator();
        return (int) evaluator.evaluate(fraction, startColor, endColor);
    }

    private List<String> texts;

    public RulerHelper(ScrollChange scrollChange) {
        texts = new ArrayList<>();
        mPoints = new ArrayList<>();
        this.scrollChange = scrollChange;
    }

    public int getCounts() {
        return lineNumbers;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCenterPoint(int mCenterPoint) {
        this.mCenterPointX = mCenterPoint;
    }

    public boolean isLongLine(int index) {
        int lineIndex = index / 10;
        if (currentLine != lineIndex) {
            currentLine = lineIndex;
            return true;
        }
        return false;
    }

    public void setScope(int start, int count,float offSet) {
        if(offSet != 0) {
            this.offSet = offSet;
        }
        lineNumbers = (int)((count - start)*1000) /( (int)((this.offSet *1000) / 10));
        for (int i = 0; i <= lineNumbers; i ++) {
            texts.add(String.valueOf(offSet*i/10+start));
        }
    }


    public String getTextByIndex(int index) {
        if (index < 0 || index >= texts.size()) {
            return "";
        }
        return texts.get(index);
    }

    public int getCenterPointX() {
        return mCenterPointX;
    }

    public void setCurrentItem(String text) {
        setCurrentText(text);
    }

    public void setCurrentText(int index) {
        if (index >= 0 && index < texts.size()) {
            this.currentText = texts.get(index);
        }
    }


    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }

    public int getScrollDistance(int x) {
        //Log.d("wentao","");
        for (int i = 0; i < mPoints.size(); i++) {
            int pointX = mPoints.get(i);
            if (0 == i && x < pointX) {
                //当前的x比第一个位置的x坐标都小 也就是需要往右移动到第一个长线的位置.
                setCurrentText(0);
                return x - pointX;
            } else if (i == mPoints.size() - 1 && x > pointX) {
                //当前的x比最后一个左边的x都大,也就是需要往左移动到最后一个长线位置.
                setCurrentText(texts.size() - 1);
                return x - pointX;
            } else {
                if (i + 1 < mPoints.size()) {
                    int nextX = mPoints.get(i + 1);
                    if (x > pointX && x <= nextX) {
                        int distance = (nextX - pointX) / 2;
                        int dis = x - pointX;
                        if (dis > distance) {
                            //说明往下一个移动
                            setCurrentText(i + 1);
                            return x - nextX;
                        } else {
                            setCurrentText(i);
                            //往前一个移动
                            return x - pointX;
                        }
                    }
                }
            }
        }
        return 0;
    }

    public void addPoint(int x) {
        mPoints.add(x);
        if (mPoints.size() == texts.size() && null != scrollChange) {
            int index = texts.indexOf(currentText);
            if (index < 0) {
                index = texts.indexOf("37.0");
            }
            if (index < 0) {
                return;
            }
            int currentX = mPoints.get(index);
            if (currentX < 0) {
                return;
            }
            scrollChange.startScroll(mCenterPointX - currentX);
        }
    }

    public boolean isFull() {
        return texts.size() == mPoints.size();
    }

    public void destroy(){
        mPoints.clear();
        texts.clear();
        mPoints = null;
        texts = null;
        scrollChange = null;
    }
}