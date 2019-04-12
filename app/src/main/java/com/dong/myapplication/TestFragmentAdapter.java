package com.dong.myapplication;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author pd
 * time     2019/4/11 15:56
 */
public class TestFragmentAdapter extends FragmentStatePagerAdapter {
    List<String> titleList;
    List<TestFragment> fragmentList;

    public TestFragmentAdapter(FragmentManager fm,List<String> titleList,List<TestFragment> fragmentList) {
        super(fm);
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * 给TabLayout设置标题使用的，如果不写这个，会出现TabLayout为空白的现象
     * @param position 索引值
     * @return title
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
