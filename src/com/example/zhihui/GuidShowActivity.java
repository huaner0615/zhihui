package com.example.zhihui;

import java.util.ArrayList;

import com.example.zhihui.utils.DensityUtils;
import com.example.zhihui.utils.PrefUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuidShowActivity extends Activity implements OnClickListener{

	private ViewPager viewPager;
	private int[] pageId = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
	private ArrayList<ImageView> imageView;
	private LinearLayout lContainer;
	private ImageView pointView;
	private int pointWidth;
	private Button btnTry;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guid_show);
		
		viewPager = (ViewPager) findViewById(R.id.vp);
		lContainer = (LinearLayout) findViewById(R.id.ll_container);
		pointView = (ImageView) findViewById(R.id.iv_red_point);
		btnTry = (Button) findViewById(R.id.btn_try);
		btnTry.setOnClickListener(this);
		
		//初始化imageView
		imageView = new ArrayList<ImageView>();
		for(int i=0;i<pageId.length;i++){
			ImageView view = new ImageView(this);
			view.setBackgroundResource(pageId[i]);
			imageView.add(view);
			
			//初始化圆点
			ImageView pointView = new ImageView(this);
			pointView.setImageResource(R.drawable.shape_circle_default);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT
					);
			if(i>0){
				params.leftMargin=DensityUtils.dp2dx(8, this);
				
			}
			pointView.setLayoutParams(params);
			lContainer.addView(pointView);
		}
		viewPager.setAdapter(new MyAdapter());
	//绘图结束后，计算两个远点之间的距离
		//视图树
		pointView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
		
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				pointView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				
				pointWidth = lContainer.getChildAt(1).getLeft()-
						lContainer.getChildAt(0).getLeft();
			}
		});
		
		
				
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(position==pageId.length-1){
					btnTry.setVisibility(View.VISIBLE);
				}
				else{
					btnTry.setVisibility(View.GONE);
				}
			}
			//页面滑动过程的回调  arg0:当前位置 arg1:偏移百分比
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				//计算当前小红点位置
				int leftMargin = (int)(pointWidth*arg0+arg1*pointWidth);
				//修改小红点的左边距
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)pointView.getLayoutParams();
				params.leftMargin = leftMargin;
				pointView.setLayoutParams(params);
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
	}
	
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pageId.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			// TODO Auto-generated method stub
			
			return view==obj;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			ImageView view = imageView.get(position);
			container.addView(view);
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View)object);
		}
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id==R.id.btn_try){
			PrefUtils.putBoolean("is_guid_show", true, this);
			startActivity(new Intent(this,MainActivity.class));
			finish();
		}
		
	}
	

}
