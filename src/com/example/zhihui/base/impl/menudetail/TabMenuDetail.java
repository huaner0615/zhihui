package com.example.zhihui.base.impl.menudetail;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;

import javax.security.auth.PrivateCredentialPermission;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhihui.NewsDetailActivity;
import com.example.zhihui.R;
import com.example.zhihui.base.BaseMenuDetailPager;
import com.example.zhihui.domain.NewsDataDetail;
import com.example.zhihui.domain.NewsDataDetail.News;
import com.example.zhihui.domain.NewsDataDetail.TopNews;
import com.example.zhihui.domain.NewsMenuData.NewsTabData;
import com.example.zhihui.global.Constants;
import com.example.zhihui.utils.CacheUtils;
import com.example.zhihui.utils.PrefUtils;
import com.example.zhihui.view.HorizontalScrollViewPager;
import com.example.zhihui.view.RefreshListView;
import com.example.zhihui.view.RefreshListView.OnRefreshListener;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.viewpagerindicator.CirclePageIndicator;

public class TabMenuDetail extends BaseMenuDetailPager{
	public NewsTabData mTabData;//页签分类的网络信息
	private String mUrl;//加载新闻列表的URL
	private NewsDataDetail newsTabData;//网络返回的新闻列表数据
	private ArrayList<TopNews> newsDataList;//头条新闻的网络数据
	private TopNewsAdapter mNewsAdapter;
	private ArrayList<News> mNewsList;
	
	@ViewInject(R.id.vp_tab_detail)
	private HorizontalScrollViewPager mTabPager;
	
	@ViewInject(R.id.lv_tab_detail)
	private RefreshListView mTabListView;
	
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;
	
	@ViewInject(R.id.tv_title)
	private TextView tvTopNewsTitle;
	private String mMoreUrl;
	private NewsAdapter mAdapter;
	
	private Handler handler = null;
	
	public TabMenuDetail(Activity activity, NewsTabData tabData) {
		super(activity);
		mTabData=tabData;
		mUrl = Constants.SERVER_URL+mTabData.url;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);
		
		View header = View.inflate(mActivity, R.layout.list_header_topnews, null);
		ViewUtils.inject(this,header);
		
		mTabListView.addHeaderView(header);
		mTabListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getDataFromServer();
			}
			
			@Override
			public void onLoadMore() {
				// 加载更多数据
				if(mMoreUrl!=null){
					getMoreDateFromServer();
					System.out.println("加载更多数据");
				}else{
					Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		mTabListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				News news = mNewsList.get(arg2);
				TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
				tvTitle.setTextColor(Color.GRAY);
				
				String readId = PrefUtils.getString("read_ids", "", mActivity);
				if(!readId.contains(news.id)){
					//以前没有添加数据
				readId = readId +news.id+",";
				PrefUtils.putString("read_ids", "", mActivity);
				}
				Intent intent = new Intent(mActivity,NewsDetailActivity.class);;
				intent.putExtra("url", news.url);
				mActivity.startActivity(intent);

				
				
			}
		});
		return view;
	}
	@Override
	public void initData() {
		//myTextView.setText(mTabData.title);
		String cache = CacheUtils.getCache(mUrl, mActivity);
		if(!TextUtils.isEmpty(cache)){
			processResult(cache,false);
		}
		getDataFromServer();
		
	}
	private void getMoreDateFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String result = responseInfo.result;
				processResult(result,true);
				mTabListView.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				mTabListView.onRefreshComplete(false);
			}
		});
		
	}

	private void getDataFromServer() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processResult(result,false);
				System.out.println("访问网络成功");
				CacheUtils.setCache(mUrl, result, mActivity);
				//刷新完成，收起刷新
				mTabListView.onRefreshComplete(true);
			}


			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				mTabListView.onRefreshComplete(false);
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void processResult(String result,boolean isMore) {
		Gson gson = new Gson();
		newsTabData = gson.fromJson(result, NewsDataDetail.class);
		//System.out.println("锟斤拷锟斤拷锟斤拷锟�+newsTabData);
		
		if(!isMore){

			if(TextUtils.isEmpty(newsTabData.data.more)){
				mMoreUrl = Constants.SERVER_URL+newsTabData.data.more;
			}else{
				////没有下一页了
				mMoreUrl = null;
			}
			
			newsDataList = newsTabData.data.topnews;
			System.out.println(newsDataList.get(0).topimage);
			if(newsDataList!=null){
				mTabPager.setAdapter(new TopNewsAdapter());
				mIndicator.setViewPager(mTabPager);
				mIndicator.setSnap(true);//快照模式
				
				mIndicator.onPageSelected(0);//将小圆点位置归0，销毁上次记录的bug
				tvTopNewsTitle.setText(newsDataList.get(0).title);//初始化第一个页面标题
			}
			mNewsList = newsTabData.data.news;
			if(mNewsList!=null){
				mAdapter = new NewsAdapter();
				mTabListView.setAdapter(mAdapter);
			}
			if(handler==null){
				handler=new Handler(){
					public void handleMessage(android.os.Message msg) {
						int mcurrentItem = mTabPager.getCurrentItem();
						if(mcurrentItem<newsDataList.size()-1){
							mcurrentItem++;
						}else{
							mcurrentItem=0;
						}
						mTabPager.setCurrentItem(mcurrentItem);
						handler.sendEmptyMessageDelayed(0, 2000);
					};
				};
				//延迟2秒切换广告
				handler.sendEmptyMessageDelayed(0, 2000);
				mTabPager.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						switch (arg1.getAction()) {
						case MotionEvent.ACTION_DOWN:
							handler.removeCallbacksAndMessages(null);
							break;
						case MotionEvent.ACTION_CANCEL://事件取消，当时时间按下或者移动刷新，当抬起后无法响应ACTION_UP，
													//也需要继续播放广告
						case MotionEvent.ACTION_UP:
							handler.sendEmptyMessageDelayed(0, 2000);
						default:
							break;
						}
						return false;
					}
				});
			}
			
		}else{
			ArrayList<News> getMoreDate = newsTabData.data.news;
			getMoreDate.addAll(getMoreDate);
			mAdapter.notifyDataSetChanged();
			
		}
		
		
	}
	class TopNewsAdapter extends PagerAdapter{
		BitmapUtils bitmapUtils;
		public TopNewsAdapter(){
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newsDataList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(mActivity);
			bitmapUtils.display(view, newsDataList.get(position).topimage);
			
			container.addView(view);
			
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View)object);
		}
	}
	class NewsAdapter extends BaseAdapter{
		BitmapUtils bitmapUtils;
		public NewsAdapter(){
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mNewsList.size();
		}

		@Override
		public News getItem(int position) {
			// TODO Auto-generated method stub
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View converView, ViewGroup parent) {
			ViewHolder viewHolder ;
			if(converView==null){
				viewHolder = new ViewHolder();
				converView = converView.inflate(mActivity, R.layout.list_item_news, null);
				
				viewHolder.tvTitle=(TextView) converView.findViewById(R.id.tv_title);
				viewHolder.tvDate=(TextView) converView.findViewById(R.id.tv_date);
				viewHolder.ivIcon=(ImageView) converView.findViewById(R.id.iv_icon);
				converView.setTag(viewHolder);
				
			}else{
				viewHolder = (ViewHolder) converView.getTag();
			}
			News news = getItem(position);
			viewHolder.tvTitle.setText(news.title);
			viewHolder.tvDate.setText(news.pubdate);
			bitmapUtils.display(viewHolder.ivIcon, news.listimage);
			
			//标记已读和未读
			String readId = PrefUtils.getString("read_ids", "", mActivity);
			if(readId.contains(news.id)){
				//已读
				viewHolder.tvTitle.setTextColor(Color.GRAY);
			}else{
				viewHolder.tvTitle.setTextColor(Color.BLACK);
			}
			
			return converView;
		}
		
	}
	static class ViewHolder{
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivIcon;
		
	}

}
