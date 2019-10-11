package zhu.liang.common.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.alibaba.fastjson.JSONObject;

public class OkhttpUtil {
	
	private final static int CONNECT_TIMEOUT = 60;
	private final static int READ_TIMEOUT = 120;
	private final static int WRITE_TIMEOUT = 60;
    private static OkHttpClient client = new OkHttpClient.Builder()
										    .readTimeout(READ_TIMEOUT,TimeUnit.SECONDS)//设置读取超时时间  
										    .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)//设置写的超时时间  
										    .connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS)//设置连接超时时间  
										    .build();
    
	public static String get(String url,Map<String,String> headersMap) throws Exception {  
		if(headersMap == null)
			headersMap = new HashMap<String, String>();
		headersMap.put("nonce", UUID.randomUUID().toString());
		headersMap.put("timestamp",System.currentTimeMillis()+"");
		Headers headers = Headers.of(headersMap);
        Request request = new Request.Builder().url(url).headers(headers).build();  
        Response response = client.newCall(request).execute();  
        return response.body().string();  
    }
	
	public static String post(String url,Map<String,String> params,Map<String,String> headersMap) throws Exception { 
		RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), JSONObject.toJSONString(params));
		okhttp3.Request.Builder b = new Request.Builder().url(url).post(body);
		if(headersMap != null && headersMap.size() > 0){
			headersMap.forEach((k,v)->b.addHeader(k, v));
		}else {
			headersMap = new HashMap<>();
		}
		headersMap.put("nonce", UUID.randomUUID().toString());
		headersMap.put("timestamp",System.currentTimeMillis()+"");
		Request request = b.build();
        Response response = client.newCall(request).execute();  
        if(response.isSuccessful()){
            String string = response.body().string();
//            System.out.println(string);
            return string;
        }
        return null;  
    }
	public static String postJsonParams(String url,String params,Map<String,String> headersMap) throws Exception {
		RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), params);
		Request.Builder b = new Request.Builder().url(url).post(body);
		if(headersMap != null && headersMap.size() > 0){
			headersMap.forEach((k,v)->b.addHeader(k, v));
		}else {
			headersMap = new HashMap<>();
		}
		headersMap.put("nonce", UUID.randomUUID().toString());
		headersMap.put("timestamp",System.currentTimeMillis()+"");
		Request request = b.build();
		Response response = client.newCall(request).execute();
		if(response.isSuccessful()){
			String string = response.body().string();
//			System.out.println(string);
			return string;
		}
		return null;
	}
	
	public static String postForm(String url,Map<String,String> params,Map<String,String> headersMap) throws Exception { 
		if(headersMap == null)
			headersMap = new HashMap<String, String>();
		FormBody.Builder bulider = new FormBody.Builder();
		if(params != null){
			params.forEach((k,v)->{bulider.add(k, v);});
		}else {
			headersMap = new HashMap<>();
		}
		headersMap.put("nonce", UUID.randomUUID().toString());
		headersMap.put("timestamp",System.currentTimeMillis()+"");
		RequestBody body = bulider.build();
		Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();  
        if(response.isSuccessful()){
            String string = response.body().string();
//            System.out.println(string);
            return string;
        }
        return null;  
    }
	
	public static String uploadFile(String url,File file) throws IOException{
		RequestBody body = RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"),file);
		Request request =  new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();  
        if(response.isSuccessful()){
            String string = response.body().string();
//            System.out.println(string);
            return string;
        }
        return null;  
	}
//	public final static String CACHE_SERVUCE_URL_PREFIC="http://localhost:8082/cache-service";
	public static String getCacheInterface(String url,String propName,String keyPrefix,String value) throws Exception {
//		String url= CACHE_SERVUCE_URL_PREFIC+"/caches/call/"+ propName+"/"+ keyPrefix+"/"+value;
		Map<String,String> headersMap = null;
		if(headersMap == null)
			headersMap = new HashMap<>();
		headersMap.put("nonce", UUID.randomUUID().toString());
		headersMap.put("timestamp",System.currentTimeMillis()+"");

		Headers headers = Headers.of(headersMap);
        Request request = new Request.Builder().url(url).headers(headers).build();  
        Response response = client.newCall(request).execute();
		String rst = response.body().string();
		try {
			JSONObject json = JSONObject.parseObject(rst);
			if("200".equals(json.getString("status" ))){
				return  json.getString("data");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
    }
	
//	public static void main(String[] args) {
//		Map<String,String> params = new HashMap<String, String>();
//		try {
//			params.put("recommendId", "76");
//			params.put("floorId", "159");
//			params.put("isIncrement", "false");
//			params.put("endDate", "20170125000000");
//			OkhttpUtil.postForm("http://localhost:3030/marketingmgr/api/activity/pushActiviData",params,null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
