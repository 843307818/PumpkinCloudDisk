package com.example.pumpkinonlinedisk;

import android.os.Environment;

public class Config {

	
	public static String username ;
	// ��ʼ����
	public static final String ACTION_START = "ACTION_START";
	// ֹͣ����
	public static final String ACTION_STOP = "ACTION_STOP";
	// ��ʼ�ϴ�
	public static final String ACTION_START_UPLOAD = "ACTION_START_UPLOAD";
	// ֹͣ�ϴ�
	public static final String ACTION_STOP_UPLOAD = "ACTION_STOP_UPLOAD";
	// ���ظ���UI
	public static final String ACTION_UPDATE = "ACTION_UPDATE";
	//�ϴ�����UI
	public static final String ACTION_UPLOADUPDATE = "ACTION_UPLOAD_UPDATE";
	// �������
	public static final String ACTION_FINISH = "ACTION_FINISH";
	// �ϴ����
	public static final String ACTION_UPLOAD_FINISH = "ACTION_UPLOAD_FINISH";
	// ɾ���ļ�
	public static final String ACTION_DELETE = "ACTION_DELETE";
	
	//����֮��洢���ֻ��ϵĵ�ַ
	public static final String DOWNLOAD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/downloadtask/";
	//��������IP��ַ
	public static final String SEVER_PATH = "192.168.191.1";
	//172.16.51.57    
	//������ʹ�õĶ˿�
	public static final int SEVER_PORT = 8081;
	
	//��ǰ��������ʾ������
	public static String SHOW_FILE_OR_FOLDER_NAME= "��Ŀ¼";
	
	//��ǰ�������ļ�·�� 
	public static String SERVER_FILEPATH = "F://pumpkin";
}
