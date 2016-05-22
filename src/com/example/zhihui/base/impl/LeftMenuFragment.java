package com.example.zhihui.base.impl;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhihui.MainActivity;
import com.example.zhihui.R;
import com.example.zhihui.R.id;
import com.example.zhihui.base.BaseFragment;
import com.example.zhihui.base.impl.ContentMenuFragment.MyAdapter;
import com.example.zhihui.domain.NewsMenuData.NewsData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;

public class LeftMenuFragment extends BaseFragment {
	private ListView lvList;
	private ArrayList<NewsData> myMenuList;
	private MenuAdapter myAdapter;
	private int mCurrentPosition;
	public View initView() {
		View view = View.inflate(mActivity, R.layout.left_menu_fragment, null);
		lvList = (ListView)view.findViewById(R.id.lv_list); 
		ViewUtils.inject(this,view);
		return view;
		
	}
	
	public void setData(ArrayList<NewsData> datas) {

		myMenuList =datas;
		myAdapter = new MenuAdapter();
		lvList.setAdapter(myAdapter);
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				mCurrentPosition = position;
				myAdapter.notifyDataSetChanged();
				//通知新闻页面，切换页面
				setCurrentMenuDetails(position);
			}
		});
		mCurrentPosition=0;
	}
	

	protected void setCurrentMenuDetails(int position) {
		// 获取新闻中心对象newsCurrentPager
		//1.先获取mainActivity
		//2.通过mainActivity获取contentFragment
		//3.通过contentFragment获取NewsCenterPager
		MainActivity mainUI = (MainActivity) mActivity;
		ContentMenuFragment contentFragment = mainUI.getContentMenuFragment();
		NewsPager newsPager = contentFragment.getNewsPager();
		
		//给新闻页面填充布局
		newsPager.setCurrentMenuDetailsPager(position);
		
		toggle();
		
	}

	protected void toggle() {
		// TODO Auto-generated method stub
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();
	}
	/**
	
	*设置网络，newPager掉用来传递数据
	*/
	
	class MenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return myMenuList.size();
		}

		@Override
		public NewsData getItem(int position) {
			return myMenuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			View view = View.inflate(mActivity, R.layout.list_item_menu, null);
			 
			TextView tvMenu = (TextView)view.findViewById(R.id.tv_menu);
			
			NewsData data = getItem(position);
			tvMenu.setText(data.title);
			
			if(mCurrentPosition==position){
				tvMenu.setEnabled(true);
			}else{
				tvMenu.setEnabled(false);
			}
			return view;
		}
		
	}
	
	

}
