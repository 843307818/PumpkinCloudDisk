package com.example.pumpkinonlinedisk.fragment;

import java.io.File;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
 
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pumpkinonlinedisk.Config;
import com.example.pumpkinonlinedisk.R;
import com.example.pumpkinonlinedisk.adapter.UploadAdapter;
import com.example.pumpkinonlinedisk.bean.DownAndUpLoadInfo;
import com.example.pumpkinonlinedisk.service.DownAndUploadService;

public class StorageFrag extends android.support.v4.app.Fragment implements
		android.view.View.OnClickListener {
	public static StorageFrag storageFrag = null;

	//������
	Context context;
	//�������
	ListView listView;
	static //����洢�����ļ���Ϣ������
	ArrayList<DownAndUpLoadInfo> list= new ArrayList<DownAndUpLoadInfo>();;
	//����ɾ���õĴ洢�����ļ���Ϣ������
	ArrayList<DownAndUpLoadInfo> aaaaaalist;
	//�����ļ���Ϣ������
	UploadAdapter adapter;
	//������ˢ�°�ť
	ImageView frag2_refresh;
	
	public StorageFrag(Context context) {
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_activity_tab2, container, false);
		initView(v);
/*		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Config.ACTION_DELETE);
		// ע��һ��Receiver
		((Activity) context).registerReceiver(receiver, intentFilter);*/
		storageFrag = this;
		return v;
	}
	static int posi;
	/**
	 * 
	 * @param v
	 */
	private void initView(View v) {
		frag2_refresh = (ImageView) v.findViewById(R.id.frag2_refresh);
		frag2_refresh.setOnClickListener(this);
		listView = (ListView) v.findViewById(R.id.main_lv2);
		getlocalfile();
		//������ɫ�ı��ѡ����ֵļ�����
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//���ñ�ѡ�������ɫ
				
				posi = position;
				adapter.setPosi(position);
				adapter.notifyDataSetChanged();
			}
		});
		
		//���ó���ɾ��
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			AlertDialog.Builder builder = new Builder(context);
			builder.setMessage("ȷ��ɾ������ļ���");
			builder.setTitle("��ʾ");
			builder.setPositiveButton("ȷ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					//���͹㲥ɾ�������ļ���SQLITE����
					Intent intent = new Intent(context, DownAndUploadService.class);
					intent.setAction(Config.ACTION_DELETE);
					intent.putExtra("value", list.get(position));
					context.startService(intent);
					
					//ɾ�������ļ�deleteFile(list.get(posi).getName(), posi);
					// ���Ŀ¼
					list.remove(posi);
					File file = new File(Config.DOWNLOAD_PATH + aaaaaalist.get(position).getName());
					Log.d("ѡ��Ҫɾ�����ļ�����", file.getName());
					Log.d("��Ӧlist���ļ�����", aaaaaalist.get(position).getName() );
					//ɾ�����ļ�
					file.delete();
					 getlocalfile();
					adapter.notifyDataSetChanged();
					refresh();
					listView.setAdapter(adapter);
						Toast.makeText(context, "ɾ�������ļ��ɹ�", Toast.LENGTH_SHORT).show();
				}
			});

			builder.setNegativeButton("ȡ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
			return true;
		}
		});
	}
	
	//�õ������ļ�
	public   void getlocalfile() {
		// ���Ŀ¼
		File file = new File(Config.DOWNLOAD_PATH);
		// ����ļ���
		String[]   filename = file.list();
		File file2;
		aaaaaalist = new ArrayList<DownAndUpLoadInfo>();
		if (filename != null) {
		
 
		for (int i = 0; i < filename.length; i++) {
			Log.d("�õ����ļ���", filename[i]);
			//�õ�ý�峤��
			  file2 = new File(Config.DOWNLOAD_PATH, filename[i]);
			  long a = file2.length();
			  String url = Config.DOWNLOAD_PATH + filename[i];
			  DownAndUpLoadInfo fileinfo = new DownAndUpLoadInfo(filename[i],file2.length(), (int)file2.length(), url ,1);
			  
			  Log.d("����Ϊ��λ��λ��λ", fileinfo.getURL());
			  
			list.add(fileinfo);
			aaaaaalist.add(fileinfo);
			Log.d("list�е��ļ����Ӧ��λ��", list.get(i).getName() + "�ڶ���λ��" + i);
			 
		}
		}
		adapter = new UploadAdapter(context, list, 0);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.frag2_refresh:
			list.clear();
			 getlocalfile() ;
			 adapter.notifyDataSetChanged();
			 Toast.makeText(context, "ˢ�±����ļ��ɹ�", Toast.LENGTH_SHORT).show();
			 
			RotateAnimation ra = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
			ra.setDuration(500);
			frag2_refresh.startAnimation(ra);
			break;

		default:
			break;
		}
		
	}
	
	//ˢ��
	public void refresh() {
		list.clear();
		 getlocalfile() ;
		 adapter.notifyDataSetChanged();
		 listView.setAdapter(adapter);
	}
	
/*	// ����һ��Receiver�����ϴ��ɹ��Ĺ㲥
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// �����ACTION_UPDATE���ʾ���ؽ��ȸ���UI��ACTION_FINISH���ʾ������ɣ�ͬʱҲ����UI
			if (Config.ACTION_UPLOAD_FINISH.equals(intent.getAction())) {
				int finished = intent.getIntExtra("finished", 0);
				int id = intent.getIntExtra("id", -1);
				Log.d("finished", String.valueOf(finished) + "  " + list.size());
				// ����adapter����UI����
				adapter.update(id, finished);
				adapter.setProgressbarisshow(false);
				adapter.notifyDataSetChanged();
				Toast.makeText(context, "�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
			} else {
			 
				int finished = intent.getIntExtra("finished", 0);
				int id = intent.getIntExtra("id", -1);
				// ������-1Ϊ�������
				// ����adapter����UI����
				adapter.update(id, -1);
			}
		}
	};*/
	

	//�ж��͹�����downloadinfo�Ƿ��Ѿ����ڵķ���
	public static boolean isdownloadinfoexist(DownAndUpLoadInfo info) {
 
		boolean result = false;
		if (list.size() != 0) {
		for (int i = 0; i < list.size(); i++) {
			if (info.getName().equals(list.get(i).getName())) {
				result = true;
			}
		}
		}
		return result;
	}
	
	// ע��Receiver
	public void onDestroy() {
	//	((Activity) context).unregisterReceiver(receiver);
		list.clear();
		super.onDestroy();
	};
	
	
}