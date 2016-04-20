package com.example.pumpkinonlinedisk.util;

import java.util.ArrayList;

import com.example.pumpkinonlinedisk.bean.DownAndUpLoadInfo;

public class TextUtil {
	
	//����������StringתΪ��DownloadInfo������ downloadinfo������������xx.apk 12345
	public static ArrayList<DownAndUpLoadInfo> getList(String text) {
		ArrayList<DownAndUpLoadInfo> list = new ArrayList<DownAndUpLoadInfo>();
		String[] array = text.split(" ");
		for (int i = 0; i < array.length; i++) {
			DownAndUpLoadInfo info = new DownAndUpLoadInfo();
			info.setName(array[i]);
			 
			//�����ȡ���ļ�������
			/*fileΪ�ļ�
			 * fodeΪ�ļ���
			 * 
			 */
			String aString  = array[++i];
			String filetype = aString.substring(0,4);
			//000001 file0 00001 file0 Android�����Ŀ��Ҫ��2015.doc file0 
			int total = Integer.parseInt(aString.substring(4));
			info.setFilesize(Long.parseLong(aString.substring(4)));
			if (filetype.equals("file")) {
				info.setType(0);
			} else if (filetype.equals("fode")) {
				info.setType(1);
			}
			info.setTotal(total);
			list.add(info);
		}
		return list;
	}
}
