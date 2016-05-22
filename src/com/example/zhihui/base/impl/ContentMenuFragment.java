package com.example.zhihui.base.impl;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RemoteViews.RemoteView;

import com.example.zhihui.MainActivity;
import com.example.zhihui.R;
import com.example.zhihui.base.BaseFragment;
import com.example.zhihui.base.BasePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.annotation.Check;
import com.lidroid.xutils.view.annotation.ViewInject;




public class ContentMenuFragment extends BaseFragment {
	
	//private ViewPager mViewPager;
	private ArrayList<BasePager> mPager;
	private RadioGroup rGroup;
	@ViewInject(R.id.vp_content)
	private ViewPager mViewPager;
	public View initView() {
		View view = View.inflate(mActivity, R.layout.content_menu_fragment, null);
		ViewUtils.inject(this, view);
		//mViewPager = (ViewPager) view.findViewById(R.id.vp_content);
		rGroup = (RadioGroup)view.findViewById(R.id.rg_btn);
		return view;
		
		
	}
	@Override
	public void initDate() {
		mPager = new ArrayList<BasePager>();
		mPager.add(new HomePager(mActivity));
		mPager.add(new NewsPager(mActivity)); 
		mPager.add(new SmartPager(mActivity));
		mPager.add(new GovPager(mActivity));
		mPager.add(new SettingPager(mActivity));
		
		mViewPager.setAdapter(new MyAdapter());
		
		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_home:
					mViewPager.setCurrentItem(0, false);
					mPager.get(0).initDate();
					setSlidingMenuEnable(false);
					break;
				case R.id.rb_news:
					mViewPager.setCurrentItem(1, false);
					mPager.get(1).initDate();
					setSlidingMenuEnable(true);
					break;
				case R.id.rb_smart:
					mViewPager.setCurrentItem(2, false);
					mPager.get(2).initDate();
					setSlidingMenuEnable(true);
					break;
				case R.id.rb_gov:
					mViewPager.setCurrentItem(3, false);
					mPager.get(3).initDate();
					setSlidingMenuEnable(true);
					break;
				case R.id.rb_setting:
					mViewPager.setCurrentItem(4, false);
					mPager.get(4).initDate();
					setSlidingMenuEnable(false);
					break;

				default:
					break;
				}
			}
		});
		mPager.get(0).initDate();//初始化首页
		setSlidingMenuEnable(false);//默认侧边栏禁用

	}
	
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPager.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view==object;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPager.get(position);
			container.addView(pager.mRootView);
			//pager.initDate();
			return pager.mRootView;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View)object);
		}
	}
	/***
	 * 
	 * 设置侧边栏可不可用
	 * */
	private void setSlidingMenuEnable(boolean enable) {
		MainActivity main= (MainActivity) mActivity;
		SlidingMenu slidingMenu = main.getSlidingMenu();
		if(enable){
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	/**
	 * 获取新闻中心的页面
	 * */
	public NewsPager getNewsPager(){
		return (NewsPager) mPager.get(1);
	}

}
