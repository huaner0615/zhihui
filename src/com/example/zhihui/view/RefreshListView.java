package com.example.zhihui.view;
import com.example.zhihui.domain.NewsDataDetail.News;
import java.security.PublicKey;
import com.example.zhihui.R;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;


/**
 * ����ˢ�µ�listview
 * 
 * @author Kevin
 * @date 2015-8-12
 */
public class RefreshListView extends ListView implements OnScrollListener,android.widget.AdapterView.OnItemClickListener{

	private static final int STATE_PULL_TO_REFRESH = 1;// ����ˢ��
	private static final int STATE_RELEASE_TO_REFRESH = 2;// �ɿ�ˢ��
	private static final int STATE_REFRESHING = 3;// ����ˢ��

	// ����ˢ��ͷ����
	private View mHeaderView;
	
	//����ˢ��β����
	private View mFooterView;
	
	//β�߶�
	private int mFooterViewHeight;
	// ͷ���ָ߶�
	private int mHeaderViewHeight;

	private int startY = -1;
	// ��ǰ����ˢ�µ�״̬
	private int mCurrentState = STATE_PULL_TO_REFRESH;// Ĭ��������ˢ��

	private TextView tvTitle;
	private ImageView ivArrow;
	private ProgressBar pbLoading;
	private TextView tvTime;

	private RotateAnimation animUp;// ��ͷ���϶���
	private RotateAnimation animDown;// ��ͷ���¶���
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private boolean isLoadingMore;//�Ƿ���ظ����

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		initView();
		initFooterView();
	}
	private void initFooterView(){
		mFooterView = View.inflate(getContext(), R.layout.list_refresh_footer, null);
		this.addFooterView(mFooterView);
		
		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		//����β����
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		tvFooterText = (TextView)mFooterView.findViewById(R.id.tv_title);
		pbFooterLoading = (ProgressBar)mFooterView.findViewById(R.id.pb_loading);
		this.setOnScrollListener(this);
	}

	private void initView() {
		mHeaderView = View.inflate(getContext(), R.layout.list_refresh_header,
				null);
		this.addHeaderView(mHeaderView);// ���ͷ����

		// ����ͷ����(1, ��ȡͷ���ָ߶�, 2.���ø�paddingTop,���־ͻ�������)
		// int height = mHeaderView.getHeight();//�˴��޷���ȡ�߶�,��Ϊ���ֻ�û�л������
		// ����֮ǰ��Ҫ��ȡ���ָ߶�
		mHeaderView.measure(0, 0);// �ֶ���������
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();// ����֮��ĸ߶�
		// ����ͷ����
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		pbLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);

		initAnim();
		setCurrentTime();// ���ó�ʼʱ��
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// ����û���סͷ���������»���, �ᵼ��listview�޷��õ�ACTION_DOWN,
								//��Ϊ��ʱ�¼��ᱻ�������ؼ�����
								// ��ʱҪ���»�ȡstartY
				startY = (int) ev.getY();
			}

			// �����ǰ����ˢ��, ʲô��������
			if (mCurrentState == STATE_REFRESHING) {
				break;
			}

			int endY = (int) ev.getY();
			int dy = endY - startY;

			if (dy > 0 && getFirstVisiblePosition() == 0) {// ���»���&��ǰ��ʾ���ǵ�һ��item,����������ˢ��
				int paddingTop = dy - mHeaderViewHeight;// ���㵱ǰ��paddingtopֵ

				// ����padding�л�״̬
				if (paddingTop >= 0
						&& mCurrentState != STATE_RELEASE_TO_REFRESH) {
					// �л����ɿ�ˢ��
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if (paddingTop < 0
						&& mCurrentState != STATE_PULL_TO_REFRESH) {
					// �л�������ˢ��
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();
				}

				mHeaderView.setPadding(0, paddingTop, 0, 0);// ��������ͷ����padding
				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// ��ʼ�������

			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				// �����ǰ���ɿ�ˢ��, ��Ҫ�л�Ϊ����ˢ��
				mCurrentState = STATE_REFRESHING;
				// ��ʾͷ����
				mHeaderView.setPadding(0, 0, 0, 0);

				refreshState();

				// ����ˢ�»ص�
				if (mListener != null) {
					mListener.onRefresh();
				}

			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// ����ͷ����
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}

			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * ��ʼ����ͷ����
	 */
	private void initAnim() {
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(500);
		animUp.setFillAfter(true);// ����״̬

		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(500);
		animDown.setFillAfter(true);// ����״̬
	}

	/**
	 * ���ݵ�ǰ״̬ˢ�½���
	 */
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("����ˢ��");
			// ��ͷ�����ƶ�
			ivArrow.startAnimation(animDown);
			// ���ؽ�����
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			break;
		case STATE_RELEASE_TO_REFRESH:
			tvTitle.setText("�ɿ�ˢ��");
			// ��ͷ�����ƶ�
			ivArrow.startAnimation(animUp);
			// ���ؽ�����
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("����ˢ��...");
			pbLoading.setVisibility(View.VISIBLE);
			ivArrow.clearAnimation();// �����������,�������ؿؼ�
			ivArrow.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
	}

	private OnRefreshListener mListener;

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	/**
	 * �����ϴ�ˢ��ʱ��
	 */
	private void setCurrentTime() {
		// 08:10 8:10 1
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH��ʾ24Сʱ��
		String time = format.format(new Date());
		tvTime.setText(time);
	}

	// ˢ�����
	public void onRefreshComplete(boolean success) {
		
		if(!isLoadingMore){
			// ����ͷ����
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			mCurrentState = STATE_PULL_TO_REFRESH;
			// ���ؽ�����
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			tvTitle.setText("����ˢ��");

			// ˢ��ʧ��,����Ҫ����ʱ��
			if (success) {
				setCurrentTime();
			}
		}else{
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
			isLoadingMore=false;
		}
		
		
	}

	public interface OnRefreshListener {
		// ����ˢ�µĻص�
		public void onRefresh();
		
		//���ظ���
		public void onLoadMore();
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState==SCROLL_STATE_IDLE){
			int lastVisiblePosition = getLastVisiblePosition();
			if(lastVisiblePosition>=getCount()-1 && !isLoadingMore){
				
				isLoadingMore=true;
				System.out.println("û����");
				mFooterView.setPadding(0, 0, 0, 0);
				setSelection(getCount()-1);
		
			if(mListener!=null){
				mListener.onLoadMore();
				}
			}
		}
	}
	private OnItemClickListener onItemClickListener;
	private void OnItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
		// TODO Auto-generated method stub
		onItemClickListener=listener;
		super.setOnItemClickListener( this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(onItemClickListener!=null){
			onItemClickListener.onItemClick(arg0, arg1, arg2-getHeaderViewsCount(), arg3);
		}
		
	}
}
