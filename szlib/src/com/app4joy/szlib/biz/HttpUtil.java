package com.app4joy.szlib.biz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpUtil {


	/**
	 * 获取网络连接
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection getConnection(Context context,String path) throws Exception {
		HttpURLConnection conn = null;
		URL url = new URL(path);
		boolean isProxy = false;
		// 网络检测
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo nInfo = cm.getActiveNetworkInfo();
			if (nInfo != null) {
				if (!nInfo.getTypeName().equalsIgnoreCase("WIFI")) {
					isProxy = true;
				}
			}
		}
		if (isProxy) {// 设置代理
			String host = android.net.Proxy.getDefaultHost();
			int port = android.net.Proxy.getDefaultPort();
			SocketAddress sa = new InetSocketAddress(host, port);
			java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,
					sa);
			conn = (HttpURLConnection) url.openConnection(proxy);
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}
		return conn;
	}

	/**
	 * 获取网页字符串
	 * 
	 * @param path
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHtml(Context context,String path) throws Exception {
		String html = "";
		HttpURLConnection conn = getConnection(context,path);
		conn.setConnectTimeout(8000);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Cache-Control","no-cache");
		conn.setRequestProperty("Host", "szlib.gov.cn");
		conn.setRequestProperty("Accept-Encoding","gzip, deflate");
		conn.setUseCaches(false);
		if (conn.getResponseCode() == 200) {
			InputStream inputStream;
			String encoding = conn.getContentEncoding();
			if (-1!=encoding.indexOf("gzip")) {
				inputStream = new GZIPInputStream(conn.getInputStream());
			}else if (-1!=encoding.indexOf("deflate")) {
				inputStream = new InflaterInputStream(conn.getInputStream());
			}else{
				inputStream = conn.getInputStream();
			}
			html = new String(read(inputStream),"UTF-8");
		}
		conn.disconnect();
		return html;
	}
	
	/**
	 * 从流中读取二进制数据
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] read(InputStream inputStream) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		outputStream.close();
		inputStream.close();
		return outputStream.toByteArray();
	}
}
