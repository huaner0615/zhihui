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
	//��ʼ����fragment
	public void onCreate(Bundle savedInstanceState) {
		mActivity = getActivity();
		super.onCreate(savedInstanceState);
	}
	//��ʼ������
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView();
		return view;
	}
	//�������
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		initDate();
	}
	//��ʼ�����֣��������ʵ��
	public abstract View initView();
	//���Բ�ʵ��
	public void initDate() {
		
		
	}
	
}
