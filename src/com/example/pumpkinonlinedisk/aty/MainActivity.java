package com.example.pumpkinonlinedisk.aty;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pumpkinonlinedisk.Config;
import com.example.pumpkinonlinedisk.R;
import com.example.pumpkinonlinedisk.adapter.ViewPagerAdapter;
import com.example.pumpkinonlinedisk.fragment.CoundDiskFrag;
import com.example.pumpkinonlinedisk.fragment.StorageFrag;
import com.example.pumpkinonlinedisk.fragment.TransmissionFrag;
import com.example.pumpkinonlinedisk.fragment.UserFrag;

public class MainActivity extends FragmentActivity implements OnPageChangeListener {
	public static MainActivity instanceActivity = null;
	
	// ������������
	FragmentPagerAdapter fPagerAdapter;
	// ��Ƭÿ����ƬΪһ������
	private ArrayList<Fragment> fragments;
	// ���е�Tab
	private List<View> views;
	// ����ʽTab
	private ViewPager vp;
	// ����Tab������
	private ViewPagerAdapter vpAdapter;

	// imageview
	private ImageView[] ivViews;
	
	//TextView
	private TextView[] tvViews;
	
	
	private LinearLayout mian_caozuolan;
	// ��ǰ��ǩ
	public static int currentTag = 0;

	//����״̬��
	FrameLayout zhuangtaiLayout;
	
	/**
	 * �༭��ť״̬��ǩ
	 * 0Ϊ�༭
	 * 1Ϊ����
	 */
	static int editTag=0;
	// �༭������״̬��ǩ
	private boolean edit_save = true;

	private int[] ivIds = { R.id.ivMain1, R.id.ivMain2,
			R.id.ivMain3, R.id.ivMain4 };
	
	private int[] tvIds = { R.id.tvMain1, R.id.tvMain2,
			R.id.tvMain3, R.id.tvMain4 };
	//�ĸ�tab��ť
	private static int[] ivviews = {R.drawable.ivviews0, R.drawable.ivviews1,
			R.drawable.ivviews2,R.drawable.ivviews3};
 
	//�鿴�ļ�ʱ�ı�ķ��ذ�ť����ʾ������
	private ImageButton main_top_leftreturn;
	private TextView main_top_filename;
	//�鿴�ļ�ʱ�ı�Ŀؼ�
	private LinearLayout mian_top_gaibainqian;
	private LinearLayout mian_top_gaibainhou;
	
	private TextView yonghuming;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.main_activity_layout);
			// ������Ƭ����
			fragments = new ArrayList<Fragment>();
			initView();
			instanceActivity = this;
	}
	
	private void initView() {
		//�鿴�ļ�ʱ�ı�Ŀؼ�
		main_top_leftreturn = (ImageButton) findViewById(R.id.main_top_leftreturn);
		main_top_leftreturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Config.SERVER_FILEPATH = "F://pumpkin";
				//�ı䶥�˲��ֻ�ȥ
				changgetoplayoutback(null);
				//ˢ��һ�λص���Ŀ¼
				CoundDiskFrag.coundDiskFrag.refresh();
			}
		});
		main_top_filename = (TextView) findViewById(R.id.main_top_filename);
		mian_top_gaibainqian = (LinearLayout) findViewById(R.id.mian_top_gaibainqian);
		yonghuming = (TextView) findViewById(R.id.yonghuming);
		yonghuming.setText(Config.username);
		mian_top_gaibainhou = (LinearLayout) findViewById(R.id.mian_top_gaibainhou);
		//�������
		zhuangtaiLayout = (FrameLayout) findViewById(R.id.ll1);
		mian_caozuolan = (LinearLayout) findViewById(R.id.mian_caozuolana);
		//��ͼƬ
		ivViews = new ImageView[ivIds.length];
		for (int i = 0; i < ivIds.length; i++) {//
			final int j = i;
			ivViews[i] = (ImageView) this.findViewById(ivIds[i]);
			ivViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					changeTagView(j);       
				}
			});
		}
		//������
		
		tvViews = new TextView[tvIds.length];
		for (int i = 0; i < tvIds.length; i++) {
			final int j = i;
			tvViews[i] = (TextView) this.findViewById(tvIds[i]);
			tvViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					changeTagView(j);
				}
			});
		}
		
		LayoutInflater inflater = LayoutInflater.from(this);

		// ��ӻ���
		views = new ArrayList<View>();
		views.add(inflater.inflate(R.layout.main_activity_tab1, null));
		views.add(inflater.inflate(R.layout.main_activity_tab2, null));
		views.add(inflater.inflate(R.layout.main_activity_tab3, null));
		views.add(inflater.inflate(R.layout.main_activity_tab4, null));
		vpAdapter = new ViewPagerAdapter(views, this);
		vp = (ViewPager) findViewById(R.id.main_viewPager);
		
		fPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO �Զ����ɵķ������
				return fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO �Զ����ɵķ������
				return fragments.get(arg0);
			}
		};
		
		// ��������Tab��ʵ��
		CoundDiskFrag coundDiskFrag = new CoundDiskFrag(this);
		StorageFrag storageFrag = new StorageFrag(this);
		TransmissionFrag transmissionFrag = new TransmissionFrag(this);
		UserFrag userFrag = new UserFrag(this);
		// ��tabs���뵽��Ƭ����
		fragments.add(coundDiskFrag);
		fragments.add(storageFrag);
		fragments.add(transmissionFrag);
		fragments.add(userFrag);
		vp.setAdapter(fPagerAdapter);

		vp.setOnPageChangeListener(this);
		
		//ע�⣬����Page ������ҳ��ĸ���������Сʱ�����fragment�ظ����ص�����
		vp.setOffscreenPageLimit(4);
		ivViews[0].setBackgroundResource(R.drawable.ivviews0);
		tvViews[0].setTextColor(Color.parseColor("#EE4000"));
	}
	
	// ������ǩ
	private void changeTagView(int change) {
		vp.setCurrentItem(change, false);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		currentTag = arg0;
		switch (arg0) { 
		case 0:
			tvViews[0].setTextColor(Color.parseColor("#1A94E6"));
			tvViews[1].setTextColor(Color.parseColor("#000000"));
			tvViews[2].setTextColor(Color.parseColor("#000000"));
			tvViews[3].setTextColor(Color.parseColor("#000000"));
			 
			//ͼƬ
			ivViews[0].setBackgroundResource(R.drawable.ivviews0);
			ivViews[1].setBackgroundResource(R.drawable.cunchu);
			ivViews[2].setBackgroundResource(R.drawable.trans);
			ivViews[3].setBackgroundResource(R.drawable.user);
			
			break;
		case 1:
			//����
			tvViews[0].setTextColor(Color.parseColor("#000000"));
			tvViews[1].setTextColor(Color.parseColor("#1A94E6"));
			tvViews[2].setTextColor(Color.parseColor("#000000"));
			tvViews[3].setTextColor(Color.parseColor("#000000"));
			 
			//ͼƬ
			ivViews[0].setBackgroundResource(R.drawable.clouddisk);
			ivViews[1].setBackgroundResource(R.drawable.ivviews1);
			ivViews[2].setBackgroundResource(R.drawable.trans);
			ivViews[3].setBackgroundResource(R.drawable.user);
			break;
		case 2:
			//����
			tvViews[0].setTextColor(Color.parseColor("#000000"));
			tvViews[1].setTextColor(Color.parseColor("#000000"));
			tvViews[2].setTextColor(Color.parseColor("#1A94E6"));
			tvViews[3].setTextColor(Color.parseColor("#000000"));
			 
			//ͼƬ
			ivViews[0].setBackgroundResource(R.drawable.clouddisk);
			ivViews[1].setBackgroundResource(R.drawable.cunchu);
			ivViews[2].setBackgroundResource(R.drawable.ivviews2);
			ivViews[3].setBackgroundResource(R.drawable.user);
			break;
		case 3:
			//����
			tvViews[0].setTextColor(Color.parseColor("#000000"));
			tvViews[1].setTextColor(Color.parseColor("#000000"));
			tvViews[2].setTextColor(Color.parseColor("#000000"));
			tvViews[3].setTextColor(Color.parseColor("#1A94E6"));
			 
			//ͼƬ
			ivViews[0].setBackgroundResource(R.drawable.clouddisk);
			ivViews[1].setBackgroundResource(R.drawable.cunchu);
			ivViews[2].setBackgroundResource(R.drawable.trans);
			ivViews[3].setBackgroundResource(R.drawable.ivviews3);
			
			break;
		}
	}

	//�鿴�ļ� �ı䶥�˲���
	public void changgetoplayout(String filename) {
		mian_top_gaibainqian.setVisibility(View.INVISIBLE);
		mian_top_gaibainhou.setVisibility(View.VISIBLE);
		main_top_filename.setText(filename);
		
	}

	//�鿴�ļ� �Ļ������˲���
	public void changgetoplayoutback(String filename) {
		mian_top_gaibainhou.setVisibility(View.INVISIBLE);
		mian_top_gaibainqian.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onBackPressed() {
		Config.SERVER_FILEPATH = "F://pumpkin";
		if (mian_top_gaibainqian.getVisibility() == View.VISIBLE) {
			if (currentTag != 0) {
				changeTagView(0);
				return;
			}
			super.onBackPressed();
		} else if (mian_top_gaibainqian.getVisibility() == View.INVISIBLE) {
			changgetoplayoutback(null);
			CoundDiskFrag.coundDiskFrag.refresh();
		}
	}
}
