package zhu.liang.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * <p>ClassName: JacksonUtil</p>
 * <p>Description: json、对象转换工具类</p>
 * <p>Author: zhangshengxiang</p>
 * <p>Date: 2018年1月29日</p>
 */
public class JacksonUtil {
	private static ObjectMapper objectMapper =null;
	private static Log logger = LogFactory.getLog(JacksonUtil.class);
	static{
		objectMapper = new ObjectMapper();
		//格式化json
//		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
		//对象属性不存在的时候跳过该属性
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		//jackson null是否参与序列化
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		//Include.Include.ALWAYS 默认 
		//Include.NON_DEFAULT 属性为默认值不序列化 
		//Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化 
		//Include.NON_NULL 属性为NULL 不序列化 
	}
	private JacksonUtil() {}
	
	/**
	 * 
	* @Title: beanToJson 
	* @Description: TODO(Bean & List 转  JSON) 
	* @param @param obj
	* @param @return  参数说明 
	* @return String    返回类型 
	* @throws
	 */
	public static String beanToJson(Object obj){
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error("beanToJson error:",e);;
		};
		return null;
	}
	
	/**
	 * 
	* @Title: jsonToBean 
	* @Description: TODO(JSON 转 Bean) 
	* @param @param str
	* @param @param clazz
	* @param @return  参数说明 
	* @return T    返回类型 
	* @throws
	 */
	public static <T> T jsonToBean(String str, Class<T> clazz){
		try {
			return objectMapper.readValue(str, clazz);
		} catch (IOException e) {
			logger.error("jsonToBean error:",e);
		};
		return null;
	}
	
	public static <T> T jsonToBean(JsonNode jsonNode,Class<T> clazz){
		try {
			if(jsonNode != null && clazz != null)
				return objectMapper.readValue(jsonNode.toString(), clazz);
		} catch (IOException e) {
			logger.error("jsonToBean error:",e);
		};
		return null;
	}
	
	public static <T> T jsonToCollections(String str,Class<?> collectionClass, Class<?>... elementClasses){
		try {
			
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
		    return objectMapper.readValue(str, javaType);
		} catch (IOException e) {
			logger.error("jsonToBean error:",e);
		};
		return null;
	}
	
	public static JsonNode getJsonNode(String plain) {
		JsonNode root = null;
		try {
			root = objectMapper.readTree(plain);
		} catch (Exception e) {
			logger.error("getJsonNode error:",e);
		} 
		return root; 
	}
	
	public static ObjectNode createObjectNode() {
		return objectMapper.createObjectNode();
	}
	
	public static Long getLong(JsonNode jsonNode, String nodeName) {
		if(jsonNode.has(nodeName)) {
			jsonNode = jsonNode.get(nodeName);
			if(!jsonNode.isNull()) {
				return jsonNode.asLong();
			}
		}
		return null;
	}
	
	public static Integer getInt(JsonNode jsonNode, String nodeName) {
		if(jsonNode.has(nodeName)) {
			jsonNode = jsonNode.get(nodeName);
			if(!jsonNode.isNull()) {
				return jsonNode.asInt();
			}
		}
		return null;
	}
	
	public static String getString(JsonNode jsonNode, String nodeName) {
		if(jsonNode.has(nodeName)) {
			jsonNode = jsonNode.get(nodeName);
			if(!jsonNode.isNull()) {
				return jsonNode.asText();
			}
		}
		return null;
	}
	
	public static Boolean getBoolean(JsonNode jsonNode, String nodeName) {
		if(jsonNode.has(nodeName)) {
			jsonNode = jsonNode.get(nodeName);
			if(!jsonNode.isNull()) {
				return jsonNode.asBoolean();
			}
		}
		return null;
	}
	
}
