package com.didikee.onekeylockscreen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by didik on 2017/3/6.
 */

public class LockScreen extends Activity{
    public static final int MY_REQUEST_CODE = 0;

    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取设备管理服务
        devicePolicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);

        // AdminReceiver继承自DeviceAdminReceiver
        componentName = new ComponentName(this, AdminReceiver.class);

        mylock();

        // 锁屏之后立即kill掉我们的Activity，避免资源浪费
//        Process.killProcess(Process.myPid());
        finish();

//        setContentView(R.layout.main_activity);
    }

    // 执行锁屏逻辑
    private void mylock() {
        boolean isActive = devicePolicyManager.isAdminActive(componentName);
        if (isActive) {                    // 如何已经获取权限
            devicePolicyManager.lockNow(); // 立即锁屏
            finish();
        } else {              // 如果没有获取权限
            activeManager();  // 获取权限
        }
    }

    // 获取权限
    private void activeManager() {
        // 启动设备管理（隐式Intent），在AndroidManifest.xml中设定响应的过滤器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        // 权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);

        // 描述（additional explanation）
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "激活后才能使用锁屏功能");

        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取权限成功，立即锁屏并finish()，否则继续获取权限
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            devicePolicyManager.lockNow();
            finish();
        } else {
            activeManager();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
