package com.arsr.arsr;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.format.DateFormat;

import com.arsr.arsr.activity.MainActivity;
import com.arsr.arsr.util.DBUtil;
import com.arsr.arsr.util.DateUtil;
import com.arsr.arsr.util.LogUtil;

public class UpdateNextDayTasksService extends Service {
    public UpdateNextDayTasksService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("启动"+ DateUtil.getTimeStr(System.currentTimeMillis()));
        Intent i = new Intent(MyApplication.getContext(), MainActivity.class);
        MyApplication.getContext().startActivity(i);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }
}
