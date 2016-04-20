package com.example.pumpkinonlinedisk.fragment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pumpkinonlinedisk.Config;
import com.example.pumpkinonlinedisk.R;
import com.example.pumpkinonlinedisk.adapter.DownLoadAdapter;
import com.example.pumpkinonlinedisk.aty.MainActivity;
import com.example.pumpkinonlinedisk.bean.DownAndUpLoadInfo;
import com.example.pumpkinonlinedisk.util.TextUtil;

public class CoundDiskFrag extends android.support.v4.app.Fragment implements
		android.view.View.OnClickListener {
	
	
	public static CoundDiskFrag coundDiskFrag = null;
	// ������
	Context context;
 

	// �ӹ��췽������context
	public CoundDiskFrag(Context context) {
		this.context = context;
		instance =this;
	}

	// �����������е�������Ϣ
	private static ArrayList<DownAndUpLoadInfo> list;
	// ��ʾ��listview
	ListView listView;
	// ����������
	static DownLoadAdapter adapter;
	// �ĸ���������ť
	ImageButton frag1_sort;
	ImageButton frag1_newfolder;
	ImageButton frag1_cloud_upload;
	ImageView frag1_refresh;

	// ͼƬ
	ImageView img;
	// �½��ļ��в������ļ�������
	EditText inputfoldername;

	// �����������ť�ĵ��������

	public static CoundDiskFrag instance;

	// ���������Ϣ��hander
	public  Handler hander = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//				adapter. msg.obj
				adapter.setlist((ArrayList<DownAndUpLoadInfo>) msg.obj);
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);
				Toast.makeText(context, "�ļ��д򿪳ɹ�", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.main_activity_tab1, container, false);

		// �õ�������ṩ���ļ��б�
		getFileList();

		// ��ʼ������
		initView(v);
/* 
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Config.ACTION_UPDATE);
		intentFilter.addAction(Config.ACTION_FINISH);
		// ע��һ��Receiver
		((Activity) context).registerReceiver(receiver, intentFilter); */
		coundDiskFrag = this;
		return v;
	}

	private void initView(View v) {
		// ͼƬ
		img = new ImageView(context);
		// ������
		frag1_sort = (ImageButton) v.findViewById(R.id.frag1_sort);
		frag1_newfolder = (ImageButton) v.findViewById(R.id.frag1_newfolder);
		frag1_cloud_upload = (ImageButton) v
				.findViewById(R.id.frag1_cloud_upload);
		frag1_refresh = (ImageView) v.findViewById(R.id.frag1_refresh);
		// ���ü�����
		frag1_sort.setOnClickListener(this);
		frag1_newfolder.setOnClickListener(this);
		frag1_cloud_upload.setOnClickListener(this);
		frag1_refresh.setOnClickListener(this);
		// listview
		listView = (ListView) v.findViewById(R.id.main_lv1);
		// ������ɫ�ı��ѡ����ֵļ�����
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ���ñ�ѡ�������ɫ
				adapter.setPosi(position);
				adapter.notifyDataSetChanged();
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int posi = position;
				AlertDialog.Builder builder = new Builder(context);
				builder.setMessage("ȷ��ɾ������ļ���");
				builder.setTitle("��ʾ");
				builder.setPositiveButton("ȷ��", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						deleteFile(list.get(posi).getName(), posi);
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

	// �õ��������ļ��б�
	// ��appʱ���д˺���������ȡ����������Ϣ����Ҫ�������̷߳�ֹ���߳̿�ס��
	public void getFileList() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Socket socket = new Socket(Config.SEVER_PATH, Config.SEVER_PORT);
					// ���ؾ���������10.0.2.2
					// �õ����������
					OutputStream out = socket.getOutputStream();
					InputStream in = socket.getInputStream();
					/*
					 * ��װ��
					 */
					DataOutputStream dataout = new DataOutputStream(out);
					DataInputStream datain = new DataInputStream(in);

					// ���������������������Ϣ
					dataout.writeUTF("file");

					// ��ȡ���������ص�������Ϣ
					String result = datain.readUTF();
					
					// �ر���Դ
					close(in, out, dataout, datain, socket);
					// ���������صĺ�����������Ϣ������ xx.apk 1234578 xxx.apk 234566
					/**
					 * xx.apk file123456
					 */
					// �� һ�� xx.apk 1234578��װ��һ��DownloadInfo
					// ����һ��ArrayList<DownLoadInfo>������
					Log.d("��������ļ��б�õ���result", result);
					if (result == null) {
						((Activity) context).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, "��ȡʧ��", Toast.LENGTH_SHORT);
							}
						});
					}
					list = TextUtil.getList(result);
					adapter = new DownLoadAdapter(context, list, 0);
					// ���ص����̣߳�����UI
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							listView.setAdapter(adapter);
						}
					});
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	// ɾ��������ĳ�ļ�
	public void deleteFile(final String filename, final int position) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Socket socket = new Socket(Config.SEVER_PATH, Config.SEVER_PORT);
					// ���ؾ���������10.0.2.2
					// �õ����������
					OutputStream out = socket.getOutputStream();
					InputStream in = socket.getInputStream();
					/*
					 * ��װ��
					 */
					DataOutputStream dataout = new DataOutputStream(out);
					DataInputStream datain = new DataInputStream(in);

					// �����������ɾ����Ϣ
					dataout.writeUTF("1" + filename);

					// ��ȡ���������ص�result
					final int result = datain.readInt();
					// �ر���Դ
					close(in, out, dataout, datain, socket);
					/*
					 * �õ����������ص�result ���Ϊ1��ɾ���ɹ���Ϊ0��ɾ��ʧ��
					 */

					// ���ص����̣߳�����UI
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (result == 1) {
								Toast.makeText(context, "ɾ���ɹ�",
										Toast.LENGTH_SHORT).show();
								list.remove(position);
								adapter.notifyDataSetChanged();
								listView.setAdapter(adapter);
							} else {
								Toast.makeText(context, "ɾ��ʧ��",
										Toast.LENGTH_SHORT).show();
							}
						}
					});
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	// �ڷ��������½��ļ���
	public void newfloder(final String flodername) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Socket socket = new Socket(Config.SEVER_PATH, Config.SEVER_PORT);
					// ���ؾ���������10.0.2.2
					// �õ����������
					OutputStream out = socket.getOutputStream();
					InputStream in = socket.getInputStream();
					/*
					 * ��װ��
					 */
					DataOutputStream dataout = new DataOutputStream(out);
					DataInputStream datain = new DataInputStream(in);

					// �����������ɾ����Ϣ
					dataout.writeUTF("2" + flodername);

					// ��ȡ���������ص�result
					final int result = datain.readInt();
					// �ر���Դ
					close(in, out, dataout, datain, socket);
					/*
					 * �õ����������ص�result ���Ϊ1���½��ɹ���Ϊ0���½�ʧ��
					 */

					// ���ص����̣߳�����UI
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (result == 1) {
								Toast.makeText(context, "�½��ɹ�",
										Toast.LENGTH_SHORT).show();
								adapter.notifyDataSetChanged();
								listView.setAdapter(adapter);
							} else {
								Toast.makeText(context, "�½�ʧ��",
										Toast.LENGTH_SHORT).show();
							}
						}
					});
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

/*	//����һ��Receiver�������سɹ��Ĺ㲥
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// �����ACTION_UPDATE���ʾ���ؽ��ȸ���UI��ACTION_FINISH���ʾ������ɣ�ͬʱҲ����UI
			if (Config.ACTION_UPDATE.equals(intent.getAction())) {
				int finished = intent.getIntExtra("finished", 0);
				int id = intent.getIntExtra("id", -1);
				Log.d("finished", String.valueOf(finished) + "  " + list.size());
				// ����adapter����UI����
				adapter.update(id, finished);
			} else {
				//int finished = intent.getIntExtra("finished", 0);
				int id = intent.getIntExtra("id", -1);
				// ������-1Ϊ�������
				// ����adapter����UI����
				adapter.update(id, -1);
				adapter.setProgressbarisshow(false);
				adapter.notifyDataSetChanged();
				Toast.makeText(context, "���سɹ�", Toast.LENGTH_SHORT).show();
			}
		}
	};*/

	// ע��Receiver
	public void onDestroy() {
	//	((Activity) context).unregisterReceiver(receiver);
		super.onDestroy();
	};

	// �Զ���ر����͹ر�socket����
	public void close(InputStream in, OutputStream out,
			DataOutputStream dataout, DataInputStream datain, Socket socket) {
		try {
			in.close();
			out.close();
			dataout.close();
			datain.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.frag1_sort:
			Toast.makeText(context, "�˹�����δ����", Toast.LENGTH_SHORT).show();
			break;
		case R.id.frag1_newfolder:
			// �����½��ļ��еĶԻ���
			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle("�½��ļ���");
			builder.setIcon(com.example.pumpkinonlinedisk.R.drawable.folder);
			inputfoldername = new EditText(context);
			builder.setView(inputfoldername);
			builder.setPositiveButton("ȷ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					String getfoldername = inputfoldername.getText().toString();
					newfloder(getfoldername);
					getFileList();
					// ˢ��listview
					adapter.notifyDataSetChanged();
					listView.setAdapter(adapter);
					Toast.makeText(context, "�½��ɹ�", Toast.LENGTH_SHORT).show();
				}
			});

			builder.setNegativeButton("ȡ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();

			break;
		case R.id.frag1_cloud_upload:
			Toast.makeText(context, "�뵽�ϴ�ҳ������ϴ�����", Toast.LENGTH_SHORT).show();
			break;
		case R.id.frag1_refresh:
			getFileList();
			// ˢ��listview
			adapter.notifyDataSetChanged();
			listView.setAdapter(adapter);
			Toast.makeText(context, "ˢ�³ɹ�", Toast.LENGTH_SHORT).show();
			RotateAnimation ra = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
			ra.setDuration(500);
			frag1_refresh.startAnimation(ra);
			break;
		}
	}
	
	//ˢ��
	public void coundrefresh() {
		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);
	}
	
	
	//���ò���ʾ�ļ����ȵķ���
	public  static void changgeprogress() {
		adapter.isshowprogress(false);
		adapter.notifyDataSetChanged();
	}
	
	//�ж��͹�����downloadinfo�Ƿ��Ѿ����ڵķ���
	public static boolean isuploadinfoexist(DownAndUpLoadInfo info) {
 
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
	
	//ˢ�����ݵķ���
	public void refresh() {
		Config.SERVER_FILEPATH = "F://pumpkin";
		getFileList();
		// ˢ��listview
		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);
	}

}
