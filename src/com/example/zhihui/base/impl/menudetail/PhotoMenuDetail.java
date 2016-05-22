package com.example.zhihui.base.impl.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhihui.R;
import com.example.zhihui.base.BaseMenuDetailPager;
import com.example.zhihui.domain.PhotoBean;
import com.example.zhihui.domain.PhotoBean.PhotoNewsData;
import com.example.zhihui.global.Constants;
import com.example.zhihui.utils.CacheUtils;
import com.example.zhihui.utils.bitmap.MyBitmapUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;


public class PhotoMenuDetail extends BaseMenuDetailPager implements OnClickListener {
	@ViewInject(R.id.lv_list)
	private ListView lvList;
	@ViewInject(R.id.gv_list)
	private GridView gvList;
	private ArrayList<PhotoNewsData> mPhotoList;
	private ImageButton btnDisplay;
	
	private boolean isList = true;
	public PhotoMenuDetail(Activity activity, ImageButton btnDisplay) {
		super(activity);
		this.btnDisplay = btnDisplay;
		//btnDisplay.setVisibility(View.VISIBLE);
		btnDisplay.setOnClickListener(this);
	}

	
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.page_menu_detail_photo, null);
		ViewUtils.inject(this, view);
		return view;
	}
	@Override
	public void initData() {
		String cache = CacheUtils.getCache(Constants.PHOTOS_URL, mActivity);
		if(!TextUtils.isEmpty(cache)){
			processResult(cache);
		}
		getDateFromServer();
	}

	private void getDateFromServer() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, Constants.PHOTOS_URL,new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				processResult(responseInfo.result);
				CacheUtils.setCache(Constants.PHOTOS_URL, responseInfo.result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		} );
	}

	protected void processResult(String result) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		PhotoBean photoBean = gson.fromJson(result, PhotoBean.class);
		mPhotoList = photoBean.data.news;
	//	System.out.println("aaaaaaaaaa"+mPhotoList);
		lvList.setAdapter(new PhotoAdapter());
		gvList.setAdapter(new PhotoAdapter());
		
	}
	class PhotoAdapter extends BaseAdapter{

		private BitmapUtils bitmap;
		//private MyBitmapUtils myBitmapUtils;

		public PhotoAdapter(){
			bitmap = new BitmapUtils(mActivity);
			bitmap.configDefaultLoadingImage(R.drawable.pic_item_list_default);
			//myBitmapUtils = new MyBitmapUtils();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPhotoList.size();
		}

		@Override
		public PhotoNewsData getItem(int position) {
			// TODO Auto-generated method stub
			return mPhotoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder= null;
			if(convertView==null){
				convertView= View.inflate(mActivity, R.layout.item_list_photo, null);
				holder = new ViewHolder();
				holder.ivCorn = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			PhotoNewsData item = getItem(position);
			holder.tvTitle.setText(item.title);
			
			bitmap.display(holder.ivCorn, item.listimage);
			return convertView;
		}
		
	}
	static class ViewHolder{
			private ImageView ivCorn;
			private TextView tvTitle;
		}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_display:
			////如果是列表就显示格子
			if(isList){
				isList = false;
				lvList.setVisibility(View.GONE);
				gvList.setVisibility(View.VISIBLE);
				btnDisplay.setImageResource(R.drawable.icon_pic_list_type);
			}else{
				isList=true;
				lvList.setVisibility(View.VISIBLE);
				gvList.setVisibility(View.GONE);
				btnDisplay.setImageResource(R.drawable.icon_pic_grid_type);
			}
			break;

		default:
			break;
		}
		
	}
	
}
