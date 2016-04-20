package com.example.pumpkinonlinedisk.service;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import android.app.Service;
import android.content.Intent;
  
import android.os.IBinder;
import android.util.Log;

import com.example.pumpkinonlinedisk.Config;
import com.example.pumpkinonlinedisk.adapter.DownLoadAdapter;
import com.example.pumpkinonlinedisk.bean.DownAndUpLoadInfo;
import com.example.pumpkinonlinedisk.bean.Flag;
import com.example.pumpkinonlinedisk.downloaddb.DownLoadDAO;
import com.example.pumpkinonlinedisk.util.Util;

public class DownAndUploadService extends Service {

	// ÿ��Flag�������ػ���ͣ��־
	private LinkedList<Flag> flist = new LinkedList<Flag>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// ��ʼservice
	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		// ��ȡ�����Ķ���
		final DownAndUpLoadInfo info = (DownAndUpLoadInfo) intent
				.getSerializableExtra("value");

		// ��ת����
		Flag flagtemp;
		if ((flagtemp = Util.getStart(flist, info.getId())) == null) {
			// �����µ�flag������ӵ�����
			/*
			 *isDownload �Ƿ���ͣ
			 * id id��
			 * num �������еĵڼ���
			 */
			Flag flag = new Flag();
			flag.setNum(flist.size());
			flag.setId(info.getId());
			flag.setDownload(true);
			flagtemp = flag;
			flist.add(flag);
		}
		final Flag result = flagtemp;

		//����ǿ�ʼ����
		if (intent.getAction().equals(Config.ACTION_START)) {
			//�����߳�
			Thread thread = new Thread(new Runnable() {
				File file = new File(Config.DOWNLOAD_PATH
						+ info.getName());
				@Override
				public void run() {
					DownLoadDAO dao = new DownLoadDAO(DownAndUploadService.this);
					//�������ݿ⣬��dao����б����Ƿ��������ݵ��ж�
					dao.insert(info);

					if (!file.exists())
						try {
							file.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					writeFile(file, info, result);
				}
			});
			thread.start();

		} else if (intent.getAction().equals(Config.ACTION_STOP)) {
			//������ͣ
			flist.get(flagtemp.getNum()).setDownload(false);
		} else if (intent.getAction().equals(Config.ACTION_DELETE)) {
		//�����ɾ����Ҫɾ���������ϵĸ��ļ������ݿ��ﱣ����ļ������д�ڳ�������������
			DownLoadDAO dao = new DownLoadDAO(DownAndUploadService.this);
			//�������ݿ⣬��dao��ɾ���������
			dao.delete(info);
		} else if (intent.getAction().equals(Config.ACTION_START_UPLOAD)) {
		//����ǿ�ʼ�ϴ�
			DownLoadDAO dao = new DownLoadDAO(DownAndUploadService.this);
			//�������ݿ⣬��dao��ɾ���������
			dao.delete(info);
			//�����߳�
			Thread thread = new Thread(new Runnable() {
				File file = new File(Config.DOWNLOAD_PATH
						+ info.getName());
				@Override
				public void run() {
					DownLoadDAO dao = new DownLoadDAO(DownAndUploadService.this);
					//�������ݿ⣬��dao����б����Ƿ��������ݵ��ж�
					dao.insert(info);
					uploadFile(file, info, result);
				}
			});
			thread.start();
			
		}else if (intent.getAction().equals(Config.ACTION_STOP_UPLOAD)) {
		//�����ֹͣ�ϴ�
			//������ͣ
			flist.get(flagtemp.getNum()).setDownload(false);
			}
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * �ϴ��ļ����������ķ���
	 * @param file
	 * @param info
	 * @param flag
	 */
	public void uploadFile(final File file, final DownAndUpLoadInfo info,
			final Flag flag) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				//���÷�������IP��ַ���Լ��˿�
				Socket socket;
				try {
					socket = new Socket(Config.SEVER_PATH, Config.SEVER_PORT);
					InputStream in = socket.getInputStream();
					OutputStream out = socket.getOutputStream();
					DataOutputStream dataout = new DataOutputStream(out);
					//���͸���������Ҫ���ص��ļ��Ͷϵ�
					dataout.writeUTF("4" +info.getName() + " " + info.getFinished());
					
					
					//ʵ�ֶϵ�������Ҫʹ�õ�File��
					RandomAccessFile raf = new RandomAccessFile(Config.DOWNLOAD_PATH + info.getName(),
							"rwd");
					//skipBytes�������Գ������������ n ���ֽ��Զ����������ֽڣ��Դﵽ�ϵ�������Ч��
					raf.skipBytes(info.getFinished());
					
					int length = 0;
					int total = info.getFinished();
					long time = System.currentTimeMillis();
					
					byte buff[] = new byte[8192];
					//����Flag�����ж��Ƿ��������
					while (flist.get(flag.getNum()).isDownload()) {
						if (raf != null) {
							length = raf.read(buff);
						}
						if (length == -1) {
							break;
						}
						//����Դ����
						out.write(buff, 0, length);
						
						total += length;
						info.setFinished(total);
						
						//�����һ��ʱ����ù㲥�������ݵĴ��ݣ��ù㲥����UI
						if ((System.currentTimeMillis() - time) > 1000) {
							Log.d("�ϴ������е�total", String.valueOf(info.getTotal()) );
							Log.d("�ϴ������е�finished", String.valueOf(info.getFinished()) );
							time = System.currentTimeMillis();
							Intent intent = new Intent(
									Config.ACTION_UPLOADUPDATE);
							intent.putExtra("id", info.getId());
							intent.putExtra("finished",
									info.getFinished() / 1000);
							sendBroadcast(intent);
						}
					}
					//
					out.flush();
					
					//�����������ϴ������������
					if (info.getTotal() == info.getFinished()) {
						Log.d("�ϴ���ϵ�total��finished",String.valueOf(info.getTotal()) +"�ָ�~��~#"+ String.valueOf(info.getFinished()) );
						Intent intent = new Intent(
								Config.ACTION_UPLOAD_FINISH);
						intent.putExtra("id", info.getId());
						intent.putExtra("finished", info.getFinished() / 1000);
						sendBroadcast(intent);
					}
					
					//��������Flag
					flist.get(flag.getNum()).setDownload(true);
					
					
					//������ͣ����������ɽ���Ϣ���������ݿ���
					DownLoadDAO dao = new DownLoadDAO(DownAndUploadService.this);
					dao.update(info);
					//�ر���Դ
					raf.close();
					Util.close(in, out, null, dataout, socket);
					
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		thread.start();
		
	}

	/*
	 * �ӷ�����������Դ�ķ���
	 * ��ʼ��ȡ�������ݣ�������������
	 */
	
	public void writeFile(final File file, final DownAndUpLoadInfo info,
			final Flag flag) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					//���÷�������IP��ַ���Լ��˿�
					Socket socket = new Socket(Config.SEVER_PATH, Config.SEVER_PORT);
					InputStream in = socket.getInputStream();
					OutputStream out = socket.getOutputStream();
					DataOutputStream dataout = new DataOutputStream(out);
					//���͸��������ļ����ֺ����صĶϵ�
					dataout.writeUTF("0" +info.getName() + " " + info.getFinished());
					
					//ʵ�ֶϵ�������Ҫʹ�õ�File��
					RandomAccessFile raf = new RandomAccessFile(file, "rwd");
					//ֱ�������ϵ�ĵط���ʼ����
					raf.skipBytes(info.getFinished());
					
					byte buff[] = new byte[8192];
					int length = 0;
					int total = info.getFinished();
					long time = System.currentTimeMillis();
					
					//����Flag�����ж��Ƿ��������
					while (flist.get(flag.getNum()).isDownload()) {
						/**
						 * length��¼�����صĳ��ȣ��������=-1������������
						 * ����һֱִ��raf��write����д������
						 */
						length = in.read(buff);
						if (length == -1)
							break;
						raf.write(buff, 0, length);
						
						
						total += length;
						info.setFinished(total);
						
						//�����һ��ʱ����ù㲥�������ݵĴ��ݣ��ù㲥����UI
						if ((System.currentTimeMillis() - time) > 100) {
							time = System.currentTimeMillis();
							Intent intent = new Intent(
									Config.ACTION_UPDATE);
							intent.putExtra("id", info.getId());
							intent.putExtra("finished",
									info.getFinished() / 1000);
							sendBroadcast(intent);
							Log.d("���ع����е�total", String.valueOf(info.getTotal()) );
							Log.d("���ع����е�finished", String.valueOf(info.getFinished()) );
						}

					}
					
					//�����������������
					if (info.getTotal() == info.getFinished()) {
						Log.d("������ϵ�total��finished",String.valueOf(info.getTotal()) +"�ָ�~��~#"+ String.valueOf(info.getFinished()) );
						Intent intent = new Intent(
								Config.ACTION_FINISH);
						intent.putExtra("id", info.getId());
						intent.putExtra("finished", info.getFinished() / 1000);
						sendBroadcast(intent);
					}

					//��������Flag
					flist.get(flag.getNum()).setDownload(true);
					
					//������ͣ����������ɽ���Ϣ���������ݿ���
					DownLoadDAO dao = new DownLoadDAO(DownAndUploadService.this);
					dao.update(info);
					//�ر���Դ
					raf.close();
					Util.close(in, out, null, dataout, socket);
				} catch (Exception e) {
				}
			}
		});
		thread.start();
	}
}
