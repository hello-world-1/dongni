package com.hagk.dongni.utils;

import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.hagk.dongni.activity.RegistActivity;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpPostUtils {

	public static Map<String, Object> doPost(String BASE_URL,
			Map<String, Object> requestParamsMap) {
		HttpURLConnection connect = null;
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		Map<String, Object> retValue = new HashMap<String, Object>();

		try {
			URL url = new URL(BASE_URL);

			connect = (HttpURLConnection) url.openConnection();
			// 设置超时时间
			connect.setConnectTimeout(5000);
			connect.setReadTimeout(5000);

			// 设置提交参数
			StringBuffer params = new StringBuffer();

			// 组织请求参数
			Iterator it = requestParamsMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry element = (Map.Entry) it.next();
				params.append(URLEncoder.encode(element.getKey().toString(), "UTF-8"));
				params.append("=");
				if(element.getValue() == null){
					/*params.append(URLEncoder.encode("null", "UTF-8"));*/
				}else{
					params.append(URLEncoder.encode(element.getValue().toString(), "UTF-8"));
				}
				params.append("&");
			}
			if (params.length() > 0) {
				params.deleteCharAt(params.length() - 1);
			}

			// 设置通用的请求属性
			connect.setRequestProperty("accept", "*/*");
			connect.setRequestProperty("connection", "Keep-Alive");
			// connect.setRequestProperty("Content-type",
			// "text/plain;charset=UTF-8");
			 connect.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
			 connect  
             .setRequestProperty("User-Agent",  
                     "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");  
			connect.setRequestProperty("Content-Length",
					String.valueOf(params.length()));

			// 如果是POST请求如何设置数据
			connect.setDoOutput(true);// 是否输入参数
			connect.setDoInput(true);

			connect.setRequestMethod("POST");

			connect.connect();
			
			OutputStream os = connect.getOutputStream();  
            os.write(params.toString().getBytes());  
            os.flush();

//			printWriter = new PrintWriter(connect.getOutputStream());
//			printWriter.write(params.toString());
//			// flush输出流的缓冲
//			printWriter.flush();

			// 获取URLConnection对象对应的输出流
			// DataOutputStream out = new
			// DataOutputStream(connect.getOutputStream());
			// // 发送请求参数
			// out.write(params.toString().getBytes("UTF-8"));
			// // out.write(params.toString().getBytes());
			// // out.writeBytes(URLEncoder.encode(params.toString(), "utf-8"));
			// out.flush();

			// 获取响应数据
			int code = connect.getResponseCode();

			System.out.println("HttpPostUtils code :" + code);

			retValue.put("code", code);

			if (code == 200) {
				InputStream inputStream = connect.getInputStream();
				String html = parseStream(inputStream);
				System.out.println("HttpPostUtils result :" + code);
				retValue.put("value", html);

				System.out.println("HttpPostUtils retValue:" + html);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connect.disconnect();
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		return retValue;
	}

	private static String parseStream(InputStream inputStream) {

		String html = null;
		try {

			byte[] b = new byte[1024];
			int length = 0;

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((length = inputStream.read(b)) != -1) {
				baos.write(b, 0, length);
			}
			byte[] data = baos.toByteArray();

			html = new String(data, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return html;
	}

	public void myUtilsHttp(Map<String, Object> params, String url, final ResultHandler handler){
		MyHttpUtils.build()//构建myhttputils
				.url(url)//请求的url
				.addParams(params)
				.onExecuteByPost(new StringCallBack() {//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
					@Override
					public void onSucceed(String result) {//请求成功之后会调用这个方法----显示结果
						handler.processResult(result);
					}

					@Override
					public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
						handler.processResultError(throwable);
					}
				});
	}

}
