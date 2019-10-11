package zhu.liang.common.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisPropertiesConstants {
	
	private Logger logger = LoggerFactory.getLogger(RedisPropertiesConstants.class);
	
	@Value("${server.context-path:${server.servlet.context-path:}}")
    private String serverContextPath;
	
	public static String moduleName;
	public static String appId;
	public static String preWord;
	
	/*@Override
	public void run(String... args) throws Exception {
		try {
			//读取配置文件
			InputStream inputStream = RedisPropertiesConstants.class.getResourceAsStream("/cacheClientConfig.properties");
			//配置文件不存在时不做处理
			if(inputStream != null) {
				Properties properties = new Properties();
				properties.load(new InputStreamReader(inputStream, "UTF-8"));
				appId = properties.getProperty("appId");
				preWord = properties.getProperty("preword");
				moduleName = serverContextPath;
				//保存配置
//				loadAllowedRedisModule();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}*/
	
//	//保存配置
//	public void loadAllowedRedisModule() throws Exception {
//		moduleName = moduleName.replaceFirst("/", "");
//		logger.info("应用名=" + moduleName + ",appid=" + appId + ",preword=" + preWord);
//		if(!"basecfgmgr".equals(moduleName)) {
//			String url = "http://localhost:14040/basecfgmgr/api/caches/saveRedisModule";
//			if("beta".equals(CommonConstants.env)) {
//				url = "http://wholesale-service-basecfgmgr.springcloud.beta.uledns.com/basecfgmgr/api/caches/saveRedisModule";
//			}else if("prd".equals(CommonConstants.env)) {
//				url = "http://wholesale-service-basecfgmgr.springcloud.prd.uledns.com/basecfgmgr/api/caches/saveRedisModule";
//			}
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("moduleName", moduleName);
//			params.put("appId", appId);
//			params.put("propName", preWord);
//			String result = OkhttpUtil.post(url, params, null);
//			logger.info(result);
//		}
//	}
	
//	public static void main(String[] args) {
//		try {
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("moduleName", "marketingmgr");
//			params.put("appId", "10104");
//			params.put("propName", "msgservice_");
//			String result = OkhttpUtil.post("http://localhost:14040/basecfgmgr/api/caches/saveRedisModule", params, null);
//			System.out.println(result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
