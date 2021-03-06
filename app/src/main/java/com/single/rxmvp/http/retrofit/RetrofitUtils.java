package com.single.rxmvp.http.retrofit;

import com.single.rxmvp.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author li
 *         Create on 2018/1/10.
 * @Description
 *          RetrofitUtils工具类
 */

public class RetrofitUtils {
    /**
     * 接口地址
     */
    public static final String BASE_API = "http://apicloud.mob.com/";
    /**
     * 连接超时时长
     */
    public static final int CONNECT_TIME_OUT = 30;
    /**
     * 读取数据超时时长
     */
    public static final int READ_TIME_OUT = 30;
    /**
     * 写数据超时时长
     */
    public static final int WRITE_TIME_OUT = 30;

    private static RetrofitUtils mInstance = null;

    private RetrofitUtils(){

    }

    public static RetrofitUtils get(){
        if (mInstance == null) {
            synchronized (RetrofitUtils.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置okHttp
     *
     * @return
     */
    private static OkHttpClient okHttpClient(){
        //开启log
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.e("okHttp--->" + message);
            }
        });

        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT,TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT,TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        return client;
    }

    public Retrofit retrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient())
                .baseUrl(BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }
}
