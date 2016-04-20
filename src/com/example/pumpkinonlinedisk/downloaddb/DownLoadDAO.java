package com.example.pumpkinonlinedisk.downloaddb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pumpkinonlinedisk.bean.DownAndUpLoadInfo;

public class DownLoadDAO {
	DLDbHelper helper = null;

	public DownLoadDAO(Context context) {
		helper = DLDbHelper.getInstance(context);
	}

	
	//���жϵ�ǰDownloadInfo �Ƿ��Ѿ��������ݿ��У�û�������,����ȡ��.
	public synchronized DownAndUpLoadInfo insert(DownAndUpLoadInfo info) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from download_info where name=?",
				new String[] { info.getName() });
		if (!cursor.moveToFirst()) {
			synchronized (db) {
				db.execSQL(
						"insert into download_info(name,finished,total) values(?,?,?)",
						new Object[] { info.getName(), info.getFinished(),
								info.getTotal() });
			}
		} else {
			//������У��������ϴ����ص�finished��
			info.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
		}

		cursor.close();
		db.close();
		return info;
	}

	//��������
	public synchronized void update(DownAndUpLoadInfo info) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"update download_info set finished=?  where name=?",
				new Object[] { String.valueOf(info.getFinished()),
						info.getName() });
		db.close();
	}
	
	//ɾ������
	public synchronized void delete(DownAndUpLoadInfo info) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"delete from download_info  where name=?",
			//	"update download_info set finished=?  where name=?",
				new Object[] { info.getName() });
		db.close();
	}
}
