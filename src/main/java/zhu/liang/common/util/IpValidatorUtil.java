package zhu.liang.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpValidatorUtil {

	
	/*Java 验证Ip是否合法*/
	public static boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		if(null == ipaddr || ipaddr.isEmpty()){
			return flag;
		}
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
	}
	

	
}
