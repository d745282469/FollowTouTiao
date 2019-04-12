package com.dong.myapplication;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        //打开这个注释，头条的效果
//        int count = 15;
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        //打开这个注释，选项比较少的效果
        int count = 5;
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        //模拟数据源
        List<String> titleList = new ArrayList<>();
        List<TestFragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < count; i++){
            tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab));//给TabLayout生成Tab项，并使用自定义布局文件
            fragmentList.add(new TestFragment());//要切换的Fragment

            //给自定义tab中的TextView设置文字
            TextView textView = tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab_normal);
            textView.setText("tab_" + i);
            textView = tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab_act_to_left);
            textView.setText("tab_" + i);
            textView = tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab_act_to_right);
            textView.setText("tab_" + i);
        }

        //因为我们没调用TabLayout的setupWithViewPager()方法,所以我们需要自己设置监听器，让自定义布局生效
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener(tabLayout,MainActivity.this));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        //给ViewPager设置适配器
        viewPager.setAdapter(new TestFragmentAdapter(getSupportFragmentManager(),titleList,fragmentList));
        //将TabLayout和ViewPager关联起来
//        tabLayout.setupWithViewPager(viewPager);

    }
}
