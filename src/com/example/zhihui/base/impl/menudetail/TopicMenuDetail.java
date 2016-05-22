package com.example.zhihui.base.impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhihui.base.BaseMenuDetailPager;

public class TopicMenuDetail extends BaseMenuDetailPager {

	public TopicMenuDetail(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		TextView view = new TextView(mActivity); 
		view.setText("ª∞Ã‚");
		view.setTextColor(Color.RED);
		view.setTextSize(20);
		view.setGravity(Gravity.CENTER);
		return view;
	}

	
}
