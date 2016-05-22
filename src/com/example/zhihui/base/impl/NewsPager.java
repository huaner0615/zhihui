package com.example.zhihui.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhihui.MainActivity;
import com.example.zhihui.base.BaseMenuDetailPager;
import com.example.zhihui.base.BasePager;
import com.example.zhihui.base.impl.menudetail.InteractMenuDetail;
import com.example.zhihui.base.impl.menudetail.NewsMenuDetail;
import com.example.zhihui.base.impl.menudetail.PhotoMenuDetail;
import com.example.zhihui.base.impl.menudetail.TopicMenuDetail;
import com.example.zhihui.domain.NewsMenuData;
import com.example.zhihui.global.Constants;
import com.example.zhihui.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsPager extends BasePager {

	private NewsMenuData newsMenuData;
	public ArrayList<BaseMenuDetailPager> baseMenuDetailPagers;
	public NewsPager(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initDate() {
		// TODO Auto-generated method stub
		tvTitle.setText("新闻中心");
		String cache = CacheUtils.getCache(Constants.CATEGORIES_URL, mActivity);
		if(!TextUtils.isEmpty(cache)){
			processResult(cache);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		// TODO Auto-generated method stub
		HttpUtils utils  = new HttpUtils();
		utils.send(HttpMethod.GET, Constants.CATEGORIES_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String result = responseInfo.result;//获取json字符串
				System.out.println(result);
				processResult(result);
				CacheUtils.setCache(Constants.CATEGORIES_URL, result, mActivity);
				
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				
			}

			
		});
	} 

	protected void processResult(String result) {
		// TODO Auto-generated method stub
		System.out.println("***"+result);
		Gson gson = new Gson();
		newsMenuData = gson.fromJson(result, NewsMenuData.class);
//		newsMenuData = gson.fromJson(result, NewsMenuData.class);
		

		//获取侧边栏对象
		MainActivity mainUI = (MainActivity) mActivity;
		LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
		leftMenuFragment.setData(newsMenuData.data);
		
		//初始化4个菜单栏信息
		baseMenuDetailPagers= new ArrayList<BaseMenuDetailPager>();
		baseMenuDetailPagers.add(new NewsMenuDetail(mActivity,newsMenuData.data.get(0).children));
		baseMenuDetailPagers.add(new InteractMenuDetail(mActivity));
		baseMenuDetailPagers.add(new PhotoMenuDetail(mActivity,btnDisplay));
		baseMenuDetailPagers.add(new TopicMenuDetail(mActivity));
	
		setCurrentMenuDetailsPager(0);
	}
	//给新闻中心的FrameLayout填充布局
		protected void setCurrentMenuDetailsPager(int position) {
			BaseMenuDetailPager pager = baseMenuDetailPagers.get(position);
		
			//填充之前先移除，清屏
		flContent.removeAllViews();
		
		flContent.addView(pager.myRootView);
		pager.initData();
		tvTitle.setText(newsMenuData.data.get(position).title);
		
		
		if(pager instanceof PhotoMenuDetail){
			btnDisplay.setVisibility(View.VISIBLE);
		}else{
			btnDisplay.setVisibility(View.INVISIBLE);
		}
	}

}
