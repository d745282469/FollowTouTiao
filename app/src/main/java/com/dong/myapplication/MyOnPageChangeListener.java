package com.dong.myapplication;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pd
 * time     2019/4/11 16:18
 */
public class MyOnPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {
    private static final String TAG = "MyOnPageChangeListener";
    private Context context;
    private List<Integer> tabWidthList;//Tab宽度集合
    private List<TextView> textViewToRightList;//染色层TextView集合
    private List<TextView> textViewToLeftList;
    private int currentScrollState;//当前滑动状态
    private int lastScrollStare;//上一次的滑动状态
    private int lastPositionOffsetPix;//上一次手指滑动的位置
    private int times;//已间隔次数
    private final int RECORD_TIMES = 8;//两次记录滑动位置的间隔

    public MyOnPageChangeListener(TabLayout tabLayout, Context context) {
        super(tabLayout);
        this.context = context;

        tabWidthList = new ArrayList<>();
        textViewToRightList = new ArrayList<>();
        textViewToLeftList = new ArrayList<>();

        //循环获取每一个Tab的宽度，因为每一个Tab宽度可能是不一样的
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            final View view = tabLayout.getTabAt(i).getCustomView();
            //由于存在view还未添加进容器中，所以可能出现getWidth为0的情况
            //为了保证getWidth不为0，使用post(runnable)的方式获取。
            view.post(new Runnable() {
                @Override
                public void run() {
                    tabWidthList.add(view.getWidth());//每个Tab的宽度

                    TextView textViewToRight = view.findViewById(R.id.tab_act_to_right);
                    //将所有的染色层宽度设置为0
                    ViewGroup.LayoutParams layoutParams = textViewToRight.getLayoutParams();
                    layoutParams.width = 0;
                    textViewToRight.setLayoutParams(layoutParams);
                    textViewToRightList.add(textViewToRight);

                    TextView textViewToLeft = view.findViewById(R.id.tab_act_to_left);
                    layoutParams = textViewToLeft.getLayoutParams();
                    layoutParams.width = 0;
                    textViewToLeft.setLayoutParams(layoutParams);
                    textViewToLeftList.add(textViewToLeft);
                    onPageSelected(1);//默认选中第一项
                }
            });
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);

        /**
         * 滑动状态，0：静止，1：手指滑动，2：自动滑动
         */
        //我们只在手指滑动和手指滑动过后的自动滑动做染色效果
        //由于没有点击监听，所以经过测试发现，点击的时候，滑动状态是才从0→2→0这样一个过程
        if (currentScrollState != 2 || lastScrollStare != 0) {
            int currentPosition, nextPosition;

            //计算，得出是向哪个方向移动
            int orientation = positionOffsetPixels - lastPositionOffsetPix;
            if (orientation > 0) {
                //向右

                currentPosition = position;
                nextPosition = currentPosition + 1;

                //操作下一个目标向右染色
                textViewToLeftList.get(nextPosition).setVisibility(View.GONE);
                textViewToRightList.get(nextPosition).setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = textViewToRightList.get(nextPosition).getLayoutParams();

                //设置宽度从0开始递增
                layoutParams.width = 0;
                textViewToRightList.get(nextPosition).setLayoutParams(layoutParams);

                float step = Float.valueOf(tabWidthList.get(nextPosition)) / context.getResources().getDisplayMetrics().widthPixels;
                layoutParams.width = (int) (step * positionOffsetPixels);
                textViewToRightList.get(nextPosition).setLayoutParams(layoutParams);


                //操作当前目标从左到右取消染色
                textViewToRightList.get(currentPosition).setVisibility(View.GONE);
                textViewToLeftList.get(currentPosition).setVisibility(View.VISIBLE);
                layoutParams = textViewToLeftList.get(currentPosition).getLayoutParams();

                //设置宽度从满开始递减
                layoutParams.width = tabWidthList.get(currentPosition);
                textViewToLeftList.get(currentPosition).setLayoutParams(layoutParams);

                step = Float.valueOf(tabWidthList.get(currentPosition)) / context.getResources().getDisplayMetrics().widthPixels;
                layoutParams.width = (int) (tabWidthList.get(currentPosition) - (step * positionOffsetPixels));
                textViewToLeftList.get(currentPosition).setLayoutParams(layoutParams);
            } else if (orientation < 0 && position < tabWidthList.size() -1) {
                //向左

                currentPosition = position + 1;
                nextPosition = position;

                //操作下一个目标从右向左染色
                textViewToRightList.get(nextPosition).setVisibility(View.GONE);
                textViewToLeftList.get(nextPosition).setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = textViewToLeftList.get(nextPosition).getLayoutParams();

                //设置宽度从0开始递增
                layoutParams.width = 0;
                textViewToLeftList.get(nextPosition).setLayoutParams(layoutParams);

                float step = Float.valueOf(tabWidthList.get(nextPosition)) / context.getResources().getDisplayMetrics().widthPixels;
                layoutParams.width = (int) (tabWidthList.get(nextPosition) - (step * positionOffsetPixels));
                textViewToLeftList.get(nextPosition).setLayoutParams(layoutParams);


                //操作当前目标从右向左取消染色
                textViewToLeftList.get(currentPosition).setVisibility(View.GONE);
                textViewToRightList.get(currentPosition).setVisibility(View.VISIBLE);
                layoutParams = textViewToRightList.get(currentPosition).getLayoutParams();

                //设置宽度从满开始递减
                layoutParams.width = tabWidthList.get(currentPosition);
                textViewToRightList.get(currentPosition).setLayoutParams(layoutParams);

                step = Float.valueOf(tabWidthList.get(nextPosition)) / context.getResources().getDisplayMetrics().widthPixels;
                layoutParams.width = (int) (step * positionOffsetPixels);
                textViewToRightList.get(currentPosition).setLayoutParams(layoutParams);
            }else {
                //等于0不动
                return;
            }

            //记录滑动位置，两次记录之间间隔要稍微拉开一点，否则可能出现两次滑动位置在同一个地方
            if (times >= RECORD_TIMES) {
                lastPositionOffsetPix = positionOffsetPixels;
                times = 0;
            }
            times++;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);
        //更新滑动状态
        lastScrollStare = currentScrollState;
        currentScrollState = state;
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        //将除了选中项之外的所有项的染色层的宽度都设置为0
        for (int i = 0; i < textViewToRightList.size(); i++){
            ViewGroup.LayoutParams layoutParamsToRight = textViewToRightList.get(i).getLayoutParams();
            ViewGroup.LayoutParams layoutParamsToLeft = textViewToLeftList.get(i).getLayoutParams();
            layoutParamsToLeft.width = 0;
            layoutParamsToRight.width = 0;

            if (i == position){
                //选中项染色层宽度设置为Tab的宽度
                layoutParamsToLeft.width = tabWidthList.get(i);
                layoutParamsToRight.width = tabWidthList.get(i);
            }
            textViewToRightList.get(i).setLayoutParams(layoutParamsToRight);
            textViewToLeftList.get(i).setLayoutParams(layoutParamsToLeft);
        }
    }
}
