package com.example.pumpkinonlinedisk.adapter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pumpkinonlinedisk.Config;
import com.example.pumpkinonlinedisk.R;
import com.example.pumpkinonlinedisk.aty.AtyPhotoViewer;
import com.example.pumpkinonlinedisk.aty.MainActivity;
import com.example.pumpkinonlinedisk.bean.DownAndUpLoadInfo;
import com.example.pumpkinonlinedisk.fragment.CoundDiskFrag;
import com.example.pumpkinonlinedisk.fragment.TransmissionFrag;
import com.example.pumpkinonlinedisk.service.DownAndUploadService;

public class UploadAdapter extends BaseAdapter{
	//����������Ϣ��list
	ArrayList<DownAndUpLoadInfo> list;
	//������
	Context context;
	//��������λ��
	int posi;
	TextView sf_fileprogress;
	TextView sf_filesize;
	//ProgressBar uploadprogressbar;
	
	Button dakaiButton;
	boolean progressbarisshow = false;
	
	//�Ƿ���ʾ������
	public boolean isProgressbarisshow() {
		return progressbarisshow;
	}
	
	//�Ƿ���ʾ�ļ�����
	public void isshowprogress(boolean show) {
		if (show) {
			sf_fileprogress.setVisibility(View.INVISIBLE);
		} else {
			sf_fileprogress.setVisibility(View.VISIBLE);
		}
	}




	public void setProgressbarisshow(boolean progressbarisshow) {
		this.progressbarisshow = progressbarisshow;
	}
	
	public void setlist(ArrayList<DownAndUpLoadInfo> list) {
		this.list = list;
	}
	
	private ImageView localfileicon;
	
	public int getPosi() {
		return posi;
	}

	public void setPosi(int posi) {
		this.posi = posi;
	}

	public UploadAdapter(Context context,	ArrayList<DownAndUpLoadInfo> list, int posi) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	//����UI����
	public void update(int id, int finished) {
		if (finished == -1) {
			//����������
			list.get(id).setFinished(100);
		} else {
			//�������δ��ɣ���ʾ���صĽ���
			list.get(id).setFinished(
					finished * 100 / (list.get(id).getTotal() / 1000));
	 
		}
		notifyDataSetChanged();
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.storagefrag_listitem,
				null);
		 Button start = (Button) view.findViewById(R.id.sf_btStart);
		Button stop = (Button) view.findViewById(R.id.sf_btStop);
		localfileicon = (ImageView) view.findViewById(R.id.localfileicon);
		sf_fileprogress = (TextView) view.findViewById(R.id.sf_fileprogress);
		dakaiButton = (Button) view.findViewById(R.id.dakai);
		 
		sf_filesize = (TextView) view.findViewById(R.id.sf_filesize);
		//uploadprogressbar = (ProgressBar) view.findViewById(R.id.uploadprogressbar);
		sf_fileprogress.setVisibility(View.INVISIBLE);
		if (progressbarisshow) {
		//	uploadprogressbar.setVisibility(View.VISIBLE);
		} else {
		//	uploadprogressbar.setVisibility(View.GONE);
		}
		final DownAndUpLoadInfo info = list.get(position);
		
			dakaiButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i  = new Intent(context, AtyPhotoViewer.class);
				i.putExtra(AtyPhotoViewer.EXTRA_PATH, info.getURL());
				context.startActivity(i);
			}
		});
		
		
		//�����ʼ�ϴ�
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final DownAndUpLoadInfo infoa = list.get(position);
				if (MainActivity.currentTag == 1) {
					TransmissionFrag.getTransinstanceActivity();
					//���ж�transfrag���Ƿ��Ѿ������info
					if (CoundDiskFrag.instance.isuploadinfoexist(infoa)) {
						Toast.makeText(context, "���ļ�����������", Toast.LENGTH_SHORT).show();
					} else if (TransmissionFrag.isuploadinfoexist(infoa)) {
						Toast.makeText(context, "���ļ������ϴ��б���", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "�����ϴ��б��в鿴", Toast.LENGTH_SHORT).show();
						//����transfrag�ķ�����Ҫ���ص�info�͹�ȥ
						TransmissionFrag.adduploadlist(infoa);
					}
				} else if (MainActivity.currentTag == 2) { 
				//	uploadprogressbar.setVisibility(View.VISIBLE);
					
					Log.d("�鿴��ʼ�ϴ�ʱ��total", String.valueOf(infoa.getTotal()) );
					
					
				Intent intent = new Intent(context, DownAndUploadService.class);
				intent.setAction(Config.ACTION_START_UPLOAD);
				list.get(position).setId(position);
				list.get(position).setStart(true);
				intent.putExtra("value", list.get(position));
				context.startService(intent);
				}
			}
		});

		//���ֹͣ�ϴ�
		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, DownAndUploadService.class);
				intent.setAction(Config.ACTION_STOP_UPLOAD);
				list.get(position).setId(position);
				list.get(position).setStart(true);
				intent.putExtra("value", list.get(position));
				context.startService(intent);
			}
		});
		 
		TextView tv = (TextView) view.findViewById(R.id.sf_tvFileName);
		//��ʾ�ļ���
		tv.setText(info.getName());
		//�õ��ļ��Ĵ�С
		long size = info.getFilesize()/1024/1024;
		DecimalFormat f = new DecimalFormat("####0.0");
		String str = f.format(size)+"MB";
		sf_fileprogress.setText(String.valueOf(info.getFinished()) + "%");
		sf_filesize.setText(str);
	//	uploadprogressbar.setMax(100);
	//	uploadprogressbar.setProgress(info.getFinished());
		
	/*	if (MainActivity.currentTag == 1) {
			//��ʾ��С
			
			tv1.setText(str);
		} else {
			//��ʾ���صİٷֱȺ������صĴ�С
			tv1.setText(String.valueOf(info.getFinished()) + "%" + "     "
					+ str);
		}*/
		
		//������ɫ�ı�
		for (int i = 0; i < list.size(); i++) {
			if (position == posi) {
				start.setVisibility(View.VISIBLE);
				stop.setVisibility(View.VISIBLE);
				view.findViewById(R.id.storage_cell).setBackgroundColor(Color.parseColor("#F5DEB3"));
			} else {
				start.setVisibility(View.INVISIBLE);
				stop.setVisibility(View.INVISIBLE);
				view.findViewById(R.id.storage_cell).setBackgroundColor(Color.WHITE);
			}
		}
		
		if (MainActivity.currentTag == 1) {
			sf_fileprogress.setVisibility(View.INVISIBLE);
		//	uploadprogressbar.setVisibility(View.GONE);
		} else if (MainActivity.currentTag == 2) {
			sf_fileprogress.setVisibility(View.VISIBLE);
		//	uploadprogressbar.setVisibility(View.VISIBLE);
		}
		
		if (info.getURL() != null) {
			Log.d("��������������������", String.valueOf(info.getAa()) );
			Log.d("��������������������", Config.DOWNLOAD_PATH + info.getName());
			localfileicon.setImageURI(Uri.fromFile(new File( Config.DOWNLOAD_PATH + info.getName())));
		}
		
		
		
		return view;
		
		
	}
}