package com.example.pumpkinonlinedisk.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter{

	private List<View> views;
	private Context context;
	
	/**
	 * 构造方法，创建一个PagerView适配器
	 * @param views
	 * @param context
	 */
	public ViewPagerAdapter(List<View> views,Context context) {
		this.views = views;
		this.context = context;
	}
	
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(views.get(position));
		return views.get(position);
	}
	
	/**
	 * view的个数
	 */
	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

}
