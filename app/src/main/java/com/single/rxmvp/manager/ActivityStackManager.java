package com.single.rxmvp.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;

import com.single.rxmvp.utils.LogUtils;

import java.util.Stack;

/**
 * @author li
 *         Create on 2018/1/10.
 * @Description
 *          Activity栈管理
 */

public class ActivityStackManager {

    private static ActivityStackManager instance = null;
    //栈
    private static Stack<Activity> activityStack;

    /**
     * 私有构造方法
     */
    private ActivityStackManager(){
        activityStack = new Stack<>();
    }

    /**
     * 单利模式
     * @return
     */
    public static ActivityStackManager getManager(){
        if(instance == null){
            synchronized (ActivityStackManager.class){
                if(instance == null){
                    instance = new ActivityStackManager();
                }
            }
        }

        return instance;
    }

    /**
     * 压栈
     * @param activity
     */
    public void push(Activity activity){
        activityStack.push(activity);
    }

    /**
     * 出栈
     * @return
     */
    public Activity pop(){
        if(!activityStack.isEmpty()){
            return activityStack.pop();
        }

        return null;
    }

    /**
     * 栈顶
     * @return
     */
    public Activity peek(){
        if(activityStack.isEmpty()){
            return null;
        }

        return activityStack.peek();
    }

    /**
     * 用于异地登录或者退出清除activity
     */
    public void clearActivity(){
        while (!activityStack.isEmpty()){
            Activity activity = activityStack.pop();
            /*if(activity instanceof LoginActivity){

            }else {
                activity.finish();
            }*/
        }
    }

    /**
     * 移除
     * @param activity
     */
    public void remove(Activity activity){
        if(activityStack.size() > 0 && activity == activityStack.peek()){
            activityStack.pop();
        }else {
            activityStack.remove(activity);
        }
    }

    /**
     * 是否存在栈
     *
     * @param activity
     * @return
     */
    public boolean contains(Activity activity) {
        return activityStack.contains(activity);
    }

    /**
     * 结束所有
     */
    public void finishAllActivity(){
        while (!activityStack.isEmpty()){
            activityStack.pop().finish();
        }
    }

    /**
     * 退出应用程序
     *
     * @param context
     */
    public void exitApp(Context context){
        try {
            finishAllActivity();
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            manager.restartPackage(context.getPackageName());
            //清除通知栏
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            android.os.Process.killProcess(android.os.Process.myPid());
        }catch (Exception e){
            LogUtils.e(e.getMessage());
        }
    }
}
