package com.didikee.onekeylockscreen;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by didik on 2017/3/8.
 */

public class LockScreenWidget extends AppWidgetProvider {

    private static final String TAG = "LockScreenWidget";

    // 保存 widget 的id的HashSet，每新建一个 widget 都会为该 widget 分配一个 id。
    private static Set idsSet = new HashSet();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate(): appWidgetIds.length="+appWidgetIds.length);

        // 每次 widget 被创建时，对应的将widget的id添加到set中
        for (int appWidgetId : appWidgetIds) {
            idsSet.add(Integer.valueOf(appWidgetId));
        }
        updateAllAppWidgets(context,appWidgetManager,idsSet);
//        prtSet();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted(): appWidgetIds.length="+appWidgetIds.length);

        // 当 widget 被删除时，对应的删除set中保存的widget的id
        for (int appWidgetId : appWidgetIds) {
            idsSet.remove(Integer.valueOf(appWidgetId));
        }
//        prtSet();
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d(TAG, "OnReceive:Action: " + action);
        if (Constant.ACTION_UPDATE_ALL.equals(action)) {
            // “更新”广播
            updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet);
        } else {
            // “按钮点击”广播
            Toast.makeText(context, "应该息屏了", Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int
            appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
    // 更新所有的 widget
    private void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManager, Set set) {

        Log.d(TAG, "updateAllAppWidgets(): size="+set.size());

        // widget 的id
        int appID;
        // 迭代器，用于遍历所有保存的widget的id
        Iterator it = set.iterator();

        while (it.hasNext()) {
            appID = ((Integer)it.next()).intValue();
            // 获取 example_appwidget.xml 对应的RemoteViews
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.layout_lockscreen_widget);
            Intent intent = new Intent();
            intent.setClass(context, LockScreen.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteView.setOnClickPendingIntent(R.id.rootView,pendingIntent);

            // 更新 widget
            appWidgetManager.updateAppWidget(appID, remoteView);
        }
    }

//    // 调试用：遍历set
//    private void prtSet() {
//        if (DEBUG) {
//            int index = 0;
//            int size = idsSet.size();
//            Iterator it = idsSet.iterator();
//            Log.d(TAG, "total:"+size);
//            while (it.hasNext()) {
//                Log.d(TAG, index + " -- " + ((Integer)it.next()).intValue());
//            }
//        }
//    }
}
