package com.example.zhihui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;

public abstract class BaseFragment extends Fragment{
	public Activity mActivity;
	@Override
	//开始创建fragment
	public void onCreate(Bundle savedInstanceState) {
		mActivity = getActivity();
		super.onCreate(savedInstanceState);
	}
	//初始化布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView();
		return view;
	}
	//创建完成
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		initDate();
	}
	//初始化布局，子类必须实现
	public abstract View initView();
	//可以不实现
	public void initDate() {
		
		
	}
	
}
