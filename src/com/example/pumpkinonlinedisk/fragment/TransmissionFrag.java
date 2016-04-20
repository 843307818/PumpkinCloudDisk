	package com.example.pumpkinonlinedisk.fragment;
	
	import java.util.ArrayList;
	
	import com.example.pumpkinonlinedisk.Config;
	import com.example.pumpkinonlinedisk.R;
	import com.example.pumpkinonlinedisk.adapter.DownLoadAdapter;
	import com.example.pumpkinonlinedisk.adapter.UploadAdapter;
	import com.example.pumpkinonlinedisk.aty.MainActivity;
	import com.example.pumpkinonlinedisk.bean.DownAndUpLoadInfo;
	
	import android.app.Activity;
	import android.content.BroadcastReceiver;
	import android.content.Context;
	import android.content.Intent;
	import android.content.IntentFilter;
	import android.os.Bundle;
	import android.support.annotation.Nullable;
	import android.util.Log;
	import android.view.LayoutInflater;
	import android.view.View;
	import android.view.View.OnClickListener;
	import android.view.ViewGroup;
	import android.widget.AdapterView;
	import android.widget.Button;
	import android.widget.ListView;
	import android.widget.Toast;
	import android.widget.AdapterView.OnItemClickListener;
	
	public class TransmissionFrag extends android.support.v4.app.Fragment{
		
		static Context context;
		private static TransmissionFrag transinstanceActivity = null;
		//�������
		static ListView  trans_lv;
		
		// �����������е�������Ϣ
		private static ArrayList<DownAndUpLoadInfo> downloadlist = new ArrayList<DownAndUpLoadInfo>();
		
		private static DownLoadAdapter downloadAdapter  = new DownLoadAdapter(context, downloadlist, 0);
			
		// �����������е��ς���Ϣ
		private static ArrayList<DownAndUpLoadInfo> uploadlist= new ArrayList<DownAndUpLoadInfo>();
		
		private static UploadAdapter uploadAdapter  = new UploadAdapter(context, uploadlist, 0);
		
		//���尴ť���
		Button trans_downloadbtn;
		Button trans_uploadbtn;
		Button trans_clean;
			
		public TransmissionFrag(Context context) {
			this.context = context;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater,
				@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			View v = inflater
					.inflate(R.layout.main_activity_tab3, container, false);
			trans_lv = (ListView) v.findViewById(R.id.trans_lv);
			setTransinstanceActivity(this);
			initView(v);
	
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(Config.ACTION_UPDATE);
			intentFilter.addAction(Config.ACTION_FINISH);
			intentFilter.addAction(Config.ACTION_UPLOAD_FINISH);
			intentFilter.addAction(Config.ACTION_UPLOADUPDATE);
			// ע��һ��Receiver
			((Activity) context).registerReceiver(receiver, intentFilter);
					return v;
		}
		
		private void initView(View v ) {
			trans_downloadbtn = (Button) v.findViewById(R.id.trans_downloadbtn);
			trans_downloadbtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					downloadAdapter.notifyDataSetChanged();
					trans_lv.setAdapter(downloadAdapter);
				}
			});
			trans_uploadbtn = (Button) v.findViewById(R.id.trans_uploadbtn);
			trans_uploadbtn.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					uploadAdapter.notifyDataSetChanged();
					trans_lv.setAdapter(uploadAdapter);
			 
				}
			});
			
			// ������ɫ�ı��ѡ����ֵļ�����
			trans_lv.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// ���ñ�ѡ�������ɫ
					downloadAdapter.setPosi(position);
					downloadAdapter.notifyDataSetChanged();
					
					 
					uploadAdapter.setPosi(position);
					uploadAdapter.notifyDataSetChanged();
				}
			});
			
			trans_clean = (Button) v.findViewById(R.id.trans_clean);
			trans_clean.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					downloadlist.clear();
					uploadlist.clear();
					downloadAdapter.setlist(downloadlist);
					downloadAdapter.notifyDataSetChanged();
					uploadAdapter.setlist(uploadlist);
					uploadAdapter.notifyDataSetChanged();
					Toast.makeText(context, "����б�ɹ�", Toast.LENGTH_SHORT).show();
					
				}
			});
		}
		
		//��downloadlist��������ݵķ���
		public static void adddownloadlist(DownAndUpLoadInfo info) {
			 
			downloadlist.add(info);
			downloadAdapter  = new DownLoadAdapter(context, downloadlist, 0);
			downloadAdapter.notifyDataSetChanged();
			trans_lv.setAdapter(downloadAdapter);
		}
		
		//��uploadlist��������ݵķ���
		public static void adduploadlist(DownAndUpLoadInfo info) {
			uploadlist.add(info);
			uploadAdapter  = new UploadAdapter(context, uploadlist, 0);
			uploadAdapter.notifyDataSetChanged();
			trans_lv.setAdapter(uploadAdapter);
		}
		
		//�ж��͹�����downloadinfo�Ƿ��Ѿ����ڵķ���
		public static boolean isdownloadinfoexist(DownAndUpLoadInfo info) {
			boolean result = false;
			if (downloadlist.size() != 0) {
			for (int i = 0; i < downloadlist.size(); i++) {
				if (info == downloadlist.get(i)) {
					result = true;
				}
			}
			}
			return result;
		}
		
		//�ж��͹�����uploadinfo�Ƿ��Ѿ����ڵķ���
		public static boolean isuploadinfoexist(DownAndUpLoadInfo info) {
			boolean result = false;
			if (uploadlist.size() != 0) {
			for (int i = 0; i < uploadlist.size(); i++) {
				if (info == uploadlist.get(i)) {
					result = true;
				}
			}
			}
			return result;
		}
	
		// ����һ��Receiver�������سɹ��Ĺ㲥
		BroadcastReceiver receiver = new BroadcastReceiver() {
	
			@Override
			public void onReceive(Context context, Intent intent) {
				// �����ACTION_UPDATE���ʾ���ؽ��ȸ���UI��ACTION_FINISH���ʾ������ɣ�ͬʱҲ����UI
				if (Config.ACTION_UPDATE.equals(intent.getAction())) {
					int finished = intent.getIntExtra("finished", 0);
					int id = intent.getIntExtra("id", -1);
					// ����adapter����UI����
					downloadAdapter.update(id, finished);
					downloadAdapter.notifyDataSetChanged();
				} else if (Config.ACTION_UPLOADUPDATE.equals(intent.getAction())) {
					int finished = intent.getIntExtra("finished", 0);
					int id = intent.getIntExtra("id", -1);
					// ����adapter����UI����
					uploadAdapter.update(id, finished);
					uploadAdapter.notifyDataSetChanged();
				} else if (Config.ACTION_UPLOAD_FINISH.equals(intent.getAction())) {
					int id = intent.getIntExtra("id", -1);
					// ������-1Ϊ�������
					// ����adapter����UI����
					uploadAdapter.update(id, -1);
					uploadAdapter.notifyDataSetChanged();
					CoundDiskFrag.instance.coundrefresh();
					Toast.makeText(context, "�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
				} else if (Config.ACTION_FINISH.equals(intent.getAction())){
					int id = intent.getIntExtra("id", -1);
					// ������-1Ϊ�������
					// ����adapter����UI����
					downloadAdapter.update(id, -1);
					downloadAdapter.notifyDataSetChanged();
					StorageFrag.storageFrag.refresh();
					Toast.makeText(context, "���سɹ�", Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		//�Զ����ɵķ���
		public static TransmissionFrag getTransinstanceActivity() {
			return transinstanceActivity;
		}
		//�Զ����ɵķ���
		public static void setTransinstanceActivity(TransmissionFrag transinstanceActivity) {
			TransmissionFrag.transinstanceActivity = transinstanceActivity;
		}
		
		@Override
		public void onDestroy() {
			((Activity) context).unregisterReceiver(receiver);
			super.onDestroy();
		}
		
		//������ʾ�ļ����ȵķ���
		public  static void changgeprogress() {
			downloadAdapter.isshowprogress(true);
			downloadAdapter.notifyDataSetChanged();
		}
		
	
	
	}
