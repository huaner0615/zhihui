package com.example.zhihui.view;

import android.content.Context;
import android.content.res.Configuration;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalScrollViewPager extends ViewPager {

	 int startX;
	 int startY;
	 int endX;
	 int endY;
	public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HorizontalScrollViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			getParent().requestDisallowInterceptTouchEvent(true);
			
			break;
		case MotionEvent.ACTION_MOVE:
			endX = (int) ev.getX();
			endY = (int) ev.getY();
			int dx = endX-startX;
			int dy = endY-startY;
			
			if(Math.abs(dx)>Math.abs(dy)){
				//左右滑
				if(dx>0){//向右滑
					if(this.getCurrentItem()==0){
						getParent().requestDisallowInterceptTouchEvent(false);
					}
					
				}else{//向左划
					if(getCurrentItem()==this.getAdapter().getCount()){
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			}else{
				//上下滑
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
