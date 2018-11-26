package com.hannibal.scalpel.http;


import android.util.Log;

import com.hannibal.scalpel.BuildConfig;
import com.hannibal.scalpel.Constant;
import com.hannibal.scalpel.Util.Base64;
import com.hannibal.scalpel.Util.CommonUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class HttpManager {

    private final static int CONNECT_TIMEOUT = 20;
    private final static int READ_TIMEOUT = 20;
    private final static int WRITE_TIMEOUT = 20;

    public static boolean post(HashMap<String, String> paramsMap) {

        try {
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key,  URLEncoder.encode(paramsMap.get(key),"UTF-8")));
                pos++;
            }

            String params = tempParams.toString();
            //params = Base64.encode(toJson(paramsMap).getBytes());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("str", paramsMap);
            params = jsonObject.toString();

            // 请求的参数转换为byte数组
            byte[] postData = params.getBytes();
            // 新建一个URL对象
            URL url = new URL(Constant.BASE_URL);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(CONNECT_TIMEOUT * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(READ_TIMEOUT * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());

            dos.write(postData);
//            //构建输出流对象，以实现输出序列化的对象
//            ObjectOutputStream objOut = new ObjectOutputStream(dos);
//            //向对象输出流写出数据，这些数据将存到内存缓冲区中
//            objOut.writeChars(params);
//            //刷新对象输出流，将字节全部写入输出流中
//            objOut.flush();
//            //关闭流对象
//            objOut.close();

            dos.flush();
            dos.close();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                CommonUtils.printDevLog("Post方式请求成功，result--->" + result);

                // 关闭连接
                urlConn.disconnect();
                return true;
            } else {
                CommonUtils.printDevLog("Post方式请求失败");

                try {
                    urlConn.getInputStream().close();
                } catch (Exception e) {
                    //CommonUtils.printDevLog(e.toString());
                }
                // 关闭连接
                urlConn.disconnect();
                return false;
            }

        } catch (Exception e) {
            CommonUtils.printDevLog(e.toString());
        }
        return false;
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    private static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            CommonUtils.printDevLog(e.toString());
            return null;
        }
    }

}
