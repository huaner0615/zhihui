package com.example.zhihui.base.impl.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.zhihui.MainActivity;
import com.example.zhihui.R;
import com.example.zhihui.base.BaseMenuDetailPager;
import com.example.zhihui.domain.NewsMenuData.NewsTabData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

public class NewsMenuDetail extends BaseMenuDetailPager implements OnPageChangeListener{
	private ArrayList<NewsTabData> mTabList;
	private ArrayList<TabMenuDetail> tabMenuDetails;
	@ViewInject(R.id.vp_detail_new)
	private ViewPager mViewPager;
	@ViewInject(R.id.indicator)
	private TabPageIndicator  mIndicator;
	//public ImageView imgViewNext;
	public NewsMenuDetail(Activity activity, ArrayList<NewsTabData> children) {
		super(activity);
		mTabList = children;
	}



	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.page_menu_details_news, null);
		ViewUtils.inject(this, view);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_detail_new);
		mIndicator = (TabPageIndicator)view.findViewById(R.id.indicator);
		return view;
	}
	@Override
	public void initData() {
		tabMenuDetails= new ArrayList<TabMenuDetail>();
		for (NewsTabData tabData : mTabList) {
			//初始化对象
			TabMenuDetail pager = new TabMenuDetail(mActivity,tabData);
			tabMenuDetails.add(pager);
			
		}
		mViewPager.setAdapter(new MenuDetailAdapter());
		//mViewPager.setOnPageChangeListener(this);
		//将页面指针与ViewPager关联起来
		mIndicator.setViewPager(mViewPager);
		//当页面指针与VIewPager绑定时，需要将页面监听设置给指针
		mIndicator.setOnPageChangeListener(this);
	}
	class MenuDetailAdapter extends PagerAdapter{
		@Override
		public CharSequence getPageTitle(int position) {
			// 返回页面指示器的标题
			return mTabList.get(position).title;
		}
		@Override
		public int getCount() {
			return tabMenuDetails.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabMenuDetail pager = tabMenuDetails.get(position);
			container.addView(pager.myRootView);
			pager.initData();
			return pager.myRootView;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
	
		
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}



	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}



	@Override
	public void onPageSelected(int position) {
		if(position==0){
			//第一个页签允许侧边栏出现,开启侧边栏
			setSlidingMenuEnable(true);
		}else{
			//关闭侧边栏
			setSlidingMenuEnable(false);
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
	@OnClick(R.id.iv_next_pager)
	public void nextPage(View view){
		int currentItem = mViewPager.getCurrentItem();
		currentItem++;
		mViewPager.setCurrentItem(currentItem);
	}

	
}
