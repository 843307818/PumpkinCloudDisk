package com.example.pumpkinonlinedisk.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pumpkinonlinedisk.Config;
import com.example.pumpkinonlinedisk.adapter.DownLoadAdapter;
import com.example.pumpkinonlinedisk.aty.MainActivity;
import com.example.pumpkinonlinedisk.bean.DownAndUpLoadInfo;
import com.example.pumpkinonlinedisk.fragment.CoundDiskFrag;


public class ServerUtil {
	 ArrayList<DownAndUpLoadInfo> list;
	 
	// ��appʱ���д˺���������ȡ����������Ϣ����Ҫ�������̷߳�ֹ���߳̿�ס��
	/*
	 * ArrayList<DownLoadInfo> list
	 */
		public void getfolderList(final String foldername , final Context context) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Socket socket = new Socket(Config.SEVER_PATH, 8081);
						//���ؾ���������10.0.2.2
						//�õ����������
						OutputStream out = socket.getOutputStream();
						InputStream in = socket.getInputStream();
						/*
						 * ��װ��
						 */
						DataOutputStream dataout = new DataOutputStream(out);
						DataInputStream datain = new DataInputStream(in);
						
						Config.SERVER_FILEPATH += "//" + foldername; 
						// ���������������������Ϣ
						dataout.writeUTF("3" + Config.SERVER_FILEPATH);

						//��÷��������ж���Ϣ�ж��Ƿ�Ϊ��
						int empty = datain.readInt();
						if (empty == 1) {
							// ���ص����̣߳�����UI
							((Activity) context).runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(context, "���ļ���Ϊ�գ����ܴ�", Toast.LENGTH_SHORT).show();
									Config.SERVER_FILEPATH = "F://pumpkin";
									}
							});
							
							
						} else {
							// ���ص����̣߳�����UI
							((Activity) context).runOnUiThread(new Runnable() {
								@Override
								public void run() {
									//�ı�mainactivity��ʾ
									MainActivity.instanceActivity.changgetoplayout(foldername);
									}
							});
							 
						// ��ȡ���������ص�������Ϣ
						String result = datain.readUTF();
						if (result == null) {
							Config.SERVER_FILEPATH = "F://pumpkin";
							// ���ص����̣߳�����UI
							((Activity) context).runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(context, "������������ʧ��", Toast.LENGTH_SHORT).show();
									}
							});
						}
						//�ر���Դ
						close(in, out, dataout, datain, socket);
						// ���������صĺ�����������Ϣ������ xx.apk 1234578 xxx.apk 234566
						// �� һ�� xx.apk 1234578��װ��һ��DownloadInfo
						// ����һ��ArrayList<DownLoadInfo>������
						
						Log.d("ServerUtil�õ���result", result);
						//000001 file0 00001 file0 Android�����Ŀ��Ҫ��2015.doc file0 
						list = TextUtil.getList(result);
						Message msg = new Message();
						msg.what=1;
						msg.obj = list;
						CoundDiskFrag.instance.hander.sendMessage(msg);
					
						Log.d("ServerUtil�õ���list", String.valueOf(list.size()) );
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			 
			});
			thread.start();
			 
			//Ϊʲô����õ���adapter�ǿյģ������
		}
		
		

		// �Զ���ر����͹ر�socket����
		public static void close(InputStream in, OutputStream out,
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
}
