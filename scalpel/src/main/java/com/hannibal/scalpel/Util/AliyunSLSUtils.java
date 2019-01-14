package com.hannibal.scalpel.Util;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.aliyun.sls.android.sdk.ClientConfiguration;
import com.aliyun.sls.android.sdk.LOGClient;
import com.aliyun.sls.android.sdk.LogException;
import com.aliyun.sls.android.sdk.SLSLog;
import com.aliyun.sls.android.sdk.core.auth.PlainTextAKSKCredentialProvider;
import com.aliyun.sls.android.sdk.core.callback.CompletedCallback;
import com.aliyun.sls.android.sdk.model.Log;
import com.aliyun.sls.android.sdk.model.LogGroup;
import com.aliyun.sls.android.sdk.request.PostLogRequest;
import com.aliyun.sls.android.sdk.result.PostLogResult;
import com.aliyun.sls.android.sdk.utils.IPService;

/**
 * Created by ola_sk on 2019/1/7.
 * Email: magicbaby810@gmail.com
 *
 * @desc 举例使用 AliyunSLSUtils.asyncUploadLog(getApplicationContext(), "微信支付","回调成功");
 *       第一个参数传入context，
 *       第二个参数传入标识，例如标识为支付，action有拉取支付、支付失败、支付完成等等
 *       第三个参数传入相应action
 */
public class AliyunSLSUtils {

    public final static int HANDLER_MESSAGE_UPLOAD_FAILED = 00011;
    public final static int HANDLER_MESSAGE_UPLOAD_SUCCESS = 00012;

    /**
     * 填入必要的参数
     */
    private static String endpoint = "http://cn-beijing.log.aliyuncs.com";
    private static String project = "olayc-app-logs";
    private static String logStore = "olayc-android-passenger-logs";
    private static String source_ip = "";
    //client的生命周期和app保持一致
    private static LOGClient logClient;

    private static void checkLogClient(Context context) {
        if (null != logClient) return;

        setupSLSClient(context);
    }

    private static void setupSLSClient(Context context) {
        //        移动端是不安全环境，不建议直接使用阿里云主账号ak，sk的方式。建议使用STS方式。具体参见
//        https://help.aliyun.com/document_detail/62681.html
//        注意：SDK 提供的 PlainTextAKSKCredentialProvider 只建议在测试环境或者用户可以保证阿里云主账号AK，SK安全的前提下使用。
//		  具体使用如下

//        主账户使用方式

        String AK = "LTAIJYpjr94dyj7P";
        String SK = "j5yExa1753FBESixfvF3gDhWKy9lrT";
        PlainTextAKSKCredentialProvider credentialProvider =
                new PlainTextAKSKCredentialProvider(AK, SK);
//        STS使用方式
//        String STS_AK = "******";
//        String STS_SK = "******";
//        String STS_TOKEN = "******";
//        StsTokenCredentialProvider credentialProvider =
//                new StsTokenCredentialProvider(STS_AK, STS_SK, STS_TOKEN);


        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        conf.setCachable(true);
        conf.setConnectType(ClientConfiguration.NetworkPolicy.WWAN_OR_WIFI);
        SLSLog.enableLog(); // log打印在控制台

        logClient = new LOGClient(context, endpoint, credentialProvider, conf);
    }

    public static void asyncUploadLog(Context context, @Nullable String ip) {
        asyncUploadLog(context, ip, "");
    }

    /*
     *  推荐使用的方式，直接调用异步接口，通过callback 获取回调信息
     */
    public static void asyncUploadLog(Context context, @Nullable String ip, String content) {
        checkLogClient(context);

        /* 创建logGroup */
        LogGroup logGroup = new LogGroup("android_passenger", TextUtils.isEmpty(ip) ? " no ip " : ip);

        /* 存入一条log */
        Log log = new Log();
        log.PutContent("current time ", DateUtils.getFullSSDateText(System.currentTimeMillis()));
        log.PutContent("content", content);
        log.PutContent("device", Build.BRAND + ";" + Build.MODEL);

        logGroup.PutLog(log);

        try {
            PostLogRequest request = new PostLogRequest(project, logStore, logGroup);
            logClient.asyncPostLog(request, new CompletedCallback<PostLogRequest, PostLogResult>() {
                @Override
                public void onSuccess(PostLogRequest request, PostLogResult result) {
                    Message message = Message.obtain(handler);
                    message.what = HANDLER_MESSAGE_UPLOAD_SUCCESS;
                    message.sendToTarget();
                }

                @Override
                public void onFailure(PostLogRequest request, LogException exception) {
                    Message message = Message.obtain(handler);
                    message.what = HANDLER_MESSAGE_UPLOAD_FAILED;
                    message.obj = exception.getMessage();
                    message.sendToTarget();
                }
            });
        } catch (LogException e) {
            e.printStackTrace();
        }
    }

    static TextView logText;
    Button upload;
    Button gc;

    private static Handler handler = new Handler() {
        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IPService.HANDLER_MESSAGE_GETIP_CODE:
                    source_ip = (String) msg.obj;
                    logText.setText(source_ip);
                    return;
                case HANDLER_MESSAGE_UPLOAD_FAILED:
                    logText.setText((String) msg.obj);
                    return;
                case HANDLER_MESSAGE_UPLOAD_SUCCESS:
                    return;
            }
            super.handleMessage(msg);
        }
    };

}
