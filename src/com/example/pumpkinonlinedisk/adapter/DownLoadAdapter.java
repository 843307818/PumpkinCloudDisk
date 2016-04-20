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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pumpkinonlinedisk.Config;
import com.example.pumpkinonlinedisk.R;
import com.example.pumpkinonlinedisk.aty.MainActivity;
import com.example.pumpkinonlinedisk.bean.DownAndUpLoadInfo;
import com.example.pumpkinonlinedisk.fragment.StorageFrag;
import com.example.pumpkinonlinedisk.fragment.TransmissionFrag;
import com.example.pumpkinonlinedisk.service.DownAndUploadService;
import com.example.pumpkinonlinedisk.util.ServerUtil;
import com.example.pumpkinonlinedisk.view.CircleProgressView;

 public class DownLoadAdapter extends BaseAdapter {
	//����������Ϣ��list
	ArrayList<DownAndUpLoadInfo> list;
	//������
	Context context;
	//����ס�����λ��
	int posi;
	//��ʾ�ļ�ͼ��
	ImageView fileicon;
	TextView tv  ;
	//TextView fileprogress ;
	TextView filesize ;
	//������
	//ProgressBar downloadprogressbar;
	
	boolean progressbarisshow = false;
	
	
	CircleProgressView jinduview;
	
	////�Ƿ���ʾ������
	public boolean isProgressbarisshow() {
		return progressbarisshow;
	}

	//�Ƿ���ʾ�ļ�����
	public void isshowprogress(boolean show) {
		if (show) {
		 	jinduview.setVisibility(View.INVISIBLE);
		} else {
		 	jinduview.setVisibility(View.VISIBLE);
		}
	}


	public void setProgressbarisshow(boolean progressbarisshow) {
		this.progressbarisshow = progressbarisshow;
	}



	public DownLoadAdapter(Context context, ArrayList<DownAndUpLoadInfo> list, int posi) {
		this.context = context;
		this.list = list;
		this.posi = posi;
		//�жϵ�ǰ�ļ����Ƿ���ڣ��������������ļ���
		File file = new File(Config.DOWNLOAD_PATH);
		if (!file.exists()) {
			file.mkdir();
		}
	}
	
	
 
	
	public void setlist(ArrayList<DownAndUpLoadInfo> list) {
		this.list = list;
	}
	
	public int getPosi() {
		return posi;
	}

	public void setPosi(int posi) {
		this.posi = posi;
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
			//downloadprogressbar.setProgress(100);
		} else {
			//�������δ��ɣ���ʾ���صĽ���
			//�����totalΪ0
			int baifenbi = finished * 100 / (list.get(id).getTotal() / 1000);
			list.get(id).setFinished(baifenbi);
			
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final DownAndUpLoadInfo info = list.get(position);
		
		View view = LayoutInflater.from(context).inflate(R.layout.counddiskfrag_listitem,
				null);
	 
	//	Button open =(Button) view.findViewById(R.id.btOpen);  
		 Button start = (Button) view.findViewById(R.id.btStart);
		Button stop = (Button) view.findViewById(R.id.btStop);
		// fileprogress = (TextView) view.findViewById(R.id.progress);
		 filesize = (TextView) view.findViewById(R.id.size);
		 tv= (TextView) view.findViewById(R.id.tvFileName);
		// downloadprogressbar = (ProgressBar) view.findViewById(R.id.downloadprogressbar);
		 jinduview = (CircleProgressView) view.findViewById(R.id.jindu);
/*		if (progressbarisshow) {
			downloadprogressbar.setVisibility(View.VISIBLE);
		} else {
			downloadprogressbar.setVisibility(View.GONE);
		}*/
		
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MainActivity.currentTag == 0) {
					TransmissionFrag.getTransinstanceActivity();
					//���ж�transfrag���Ƿ��Ѿ������info
					if (StorageFrag.isdownloadinfoexist(info)) {
						Toast.makeText(context, "���ļ����ڱ����ļ���", Toast.LENGTH_SHORT).show();
					} else if (TransmissionFrag.isdownloadinfoexist(info)) {
						Toast.makeText(context, "���ļ����������б���", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "���������б��в鿴", Toast.LENGTH_SHORT).show();
						//����transfrag�ķ�����Ҫ���ص�info�͹�ȥ
						TransmissionFrag.adddownloadlist(info);
					}
				} else if (MainActivity.currentTag == 2) {
					//downloadprogressbar.setVisibility(View.VISIBLE);
				//�����ʼ����
				if (!list.get(position).isStart()) {

					
					Intent intent = new Intent(context, DownAndUploadService.class);
					intent.setAction(Config.ACTION_START);
					list.get(position).setId(position);
					list.get(position).setStart(true);
					intent.putExtra("value", list.get(position));
					context.startService(intent);
					jinduview.setVisibility(View.VISIBLE);
				}
				
				}
			}
		});

		//���ֹͣ����
		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list.get(position).isStart()) {
					Intent intent = new Intent(context, DownAndUploadService.class);
					intent.setAction(Config.ACTION_STOP);
					list.get(position).setId(position);
					list.get(position).setStart(false);
					intent.putExtra("value", list.get(position));
					context.startService(intent);
				}
			}
		});
		
	 
		
		/*//open��ť�ĵ���¼�
			open.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//������ļ�
					if (info.getType() == 0) {
						Toast.makeText(context, "��ʱû��д�ļ��鿴����", Toast.LENGTH_SHORT).show();
						//������ļ���
					}else if (info.getType() == 1) {
						 //���������������õ���Ӧ�ļ����µ��б�ArrayList<DownLoadInfo> list
					 	  new ServerUtil().getfolderList(list.get(position).getName(), context);
						 notifyDataSetChanged(); 
					}
				}
			});*/
		
 
		//��ʾ�ļ���
		tv.setText(info.getName());
		//�õ��ļ��Ĵ�С
		
		long size = info.getFilesize()/1024;
		DecimalFormat f = new DecimalFormat("####0.0");
		String str = f.format(size)+"KB";
	//	fileprogress.setText(info.getFinished() + "%");
		float num = ((float)info.getFinished())*360/100;
		
		String text = String.valueOf(info.getFinished()) + "%";
		jinduview.setshuju(num, text);
		
		
		filesize.setText(String.valueOf(str));
		//�ڽ���������ʾ��С
	//	downloadprogressbar.setMax(100);
	//	downloadprogressbar.setProgress(info.getFinished());
/*		if (MainActivity.currentTag == 0) {
			//��ʾ��С
			tv1.setText(str);
		} else {
			//��ʾ���صİٷֱȺ������صĴ�С
			tv1.setText(String.valueOf(info.getFinished()) + "%" + "     "
					+ str);
		}*/
	 
		if (MainActivity.currentTag == 0) {
		//	fileprogress.setVisibility(View.INVISIBLE);
			//downloadprogressbar.setVisibility(View.GONE);
			jinduview.setVisibility(View.INVISIBLE);
		} else if (MainActivity.currentTag == 2) {
	//		fileprogress.setVisibility(View.VISIBLE);
			//downloadprogressbar.setVisibility(View.VISIBLE);
			jinduview.setVisibility(View.VISIBLE);
		}
		
		//��ʾ�ļ����ļ���ͼ��
	 
		fileicon = (ImageView) view.findViewById(R.id.cound_fileicon);
		if (MainActivity.currentTag == 1) {
			if (info.getURL() != null) {
				Log.d("��������������������", info.getURL());
				fileicon.setImageURI(Uri.fromFile(new File(info.getURL())));
			}
		} else {
	 
		 
	 	if (info.getType() == 0) {
			//MP4MP3��ͼƬ��ʽ�ļ���ʾ��ͼ��
			if (info.getName().endsWith(".mp3")) {
				fileicon.setBackgroundResource(R.drawable.mp3filepicture);
				
				//fileicon.setImageURI(Uri.fromFile(new File(info.getURL())));
			//	Log.d("����Ϊ��λ��λ��λ", info.getURL());
			} else if(info.getName().endsWith(".mp4")) {
				fileicon.setBackgroundResource(R.drawable.mp4filepicture);
			} else if((info.getName().endsWith(".jpg"))||(info.getName().endsWith(".png"))) {
				//Log.d("����Ϊ��λ��λ��λ", info.getURL());
				fileicon.setBackgroundResource(R.drawable.picturefilepicture);
				//fileicon.setImageURI(Uri.fromFile(new File(info.getURL())));
			}
			else {
				//�����ʽδ֪������Ϊһ���ļ�ͼ��
			fileicon.setBackgroundResource(R.drawable.file);
			}
		}else if (info.getType() == 1) {
			//fileprogress.setText("");
			fileicon.setBackgroundResource(R.drawable.folder);
		} 
		
		}
		
		//������ɫ�ı�
		for (int i = 0; i < list.size(); i++) {
			if (position == posi) {
			//	open.setVisibility(View.VISIBLE);
				start.setVisibility(View.VISIBLE);
				stop.setVisibility(View.VISIBLE);
				view.findViewById(R.id.counddisk_cell).setBackgroundColor(Color.parseColor("#F5DEB3"));
			} else {
			//	open.setVisibility(View.INVISIBLE);
				start.setVisibility(View.INVISIBLE);
				stop.setVisibility(View.INVISIBLE);
				view.findViewById(R.id.counddisk_cell).setBackgroundColor(Color.WHITE);
			}
		}
		
	 
		
		return view;
	}
	
 
}
