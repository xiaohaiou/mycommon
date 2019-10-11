package zhu.liang.common.util;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class DataUtil {

	public  static String parseString(HttpServletRequest request,String key){
		Object str = request.getParameter(key);
		if( str != null && !"".equals(str.toString())){
			return str.toString().trim();
		}
		else{
			return "";
		}
	}
	
	public  static Long parseLong(HttpServletRequest request,String key){
		Object str = request.getParameter(key);
		if( str != null && !"".equals(str.toString())){
			return Long.parseLong(str.toString());
		}
		else{
			return null;
		}
	}
	
	public  static Double parseDouble(HttpServletRequest request,String key){
		Object str = request.getParameter(key);
		if( str != null && !"".equals(str.toString())){
			return Double.parseDouble(str.toString());
		}
		else{
			return null;
		}
	}
	
	public  static Integer parseInt(HttpServletRequest request,String key){
		Object str = request.getParameter(key);
		if( str != null && !"".equals(str.toString())){
			return Integer.parseInt(str.toString());
		}
		else{
			return null;
		}
	}
	
	public static Short parseShort(HttpServletRequest request,String key){
		Object str = request.getParameter(key);
		if( str != null && !"".equals(str.toString())){
			return Short.parseShort(str.toString());
		}else{
			return null;
		}
	}
	
	public  static Integer parseInt(Object str){
		if(!isNumber(str.toString()))
			return null;
		if( str != null && !"".equals(str.toString())){
			return Integer.parseInt(str.toString());
		}
		else{
			return null;
		}
	}
	
	public static boolean isNumber(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches(); 
	}
	
	/**
	 * 汉字转换成拼音
	 * @param input
	 * @return
	 */
	public static String toPinyin(String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c <= 255) {
				sb.append(c);
			} else {
				String pinyin = null;
				try {
					String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, PINYIN_FORMAT);
					pinyin = pinyinArray[0];
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				if (pinyin != null) {
					sb.append(pinyin);
				}
			}
		}
		return sb.toString();
	}
	
	private static HanyuPinyinOutputFormat PINYIN_FORMAT = new HanyuPinyinOutputFormat();
	
	static {
		PINYIN_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		PINYIN_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);	
	}
	
	 public static String ToDBC(String input)
     {
         char[] c=input.toCharArray();
         for (int i = 0; i < c.length; i++)
         {
             if (c[i]==12288)
             {
                 c[i]= (char)32;
                 continue;
             }
             if (c[i]>65280 && c[i]<65375)
                 c[i]=(char)(c[i]-65248);
         }
         return new String(c);
     }
	 /**
	  * 获取主机真实IP
	  * @return
	  * @throws IOException
	  */
	 public static String getRealIp() throws IOException{
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()){
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()){
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address){
//						System.out.println("本机的IP = " + ip.getHostAddress());
						return ip.getHostAddress();
					} 
				}
			}
			return null;
		}
	 /**
	  * 合并数组
	  * @param a1
	  * @param a2
	  * @return
	  */
	 public static String[] mergeArray(String[] a1,String[] a2){
		String[] c= new String[a1.length+a2.length];
		System.arraycopy(a1, 0, c, 0, a1.length);  
		System.arraycopy(a2, 0, c, a1.length, a2.length);
		return c;
	 }
	 
	//效验 替换参数中的sql关键字
	    public static String replaceSqlWord(String str) {
	            str = str.toLowerCase();//统一转为小写
	            String badStr = "'|and|exec|execute|insert|drop|table|from|grant|group_concat|column_name|frame|iframe|script|" +
	                    "information_schema.columns|table_schema|union|where|select|delete|update|" +
	                    "chr|mid|master|truncate|declare|like|#|<|>";//|//|/|--|+|\\|$|!|?";//过滤掉的sql关键字，可以手动添加
	            
	            Pattern pattern = Pattern.compile("\\s{1,}"+badStr+"\\s{1,}");
	            Matcher matcher = pattern.matcher(str);
	            if(matcher.find()){
	            	return matcher.replaceAll("");
	            }
	            return str;
	    }
	    private static List<Pattern> patterns = null;

	    private static List<Object[]> getXssPatternList() {
	        List<Object[]> ret = new ArrayList<Object[]>();
	        ret.add(new Object[]{"<(no)?script[^>]*>.*?</(no)?script>", Pattern.CASE_INSENSITIVE});
	        ret.add(new Object[]{"eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
	        ret.add(new Object[]{"expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
	        ret.add(new Object[]{"(javascript:|vbscript:|view-source:)+", Pattern.CASE_INSENSITIVE});
	        ret.add(new Object[]{"<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
	        ret.add(new Object[]{"(window\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
	        ret.add(new Object[]{"<+\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror=|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)+\\s*=+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
	        return ret;
	    }
	    private static List<Pattern> getPatterns() {
	        if (patterns == null) {
	            List<Pattern> list = new ArrayList<Pattern>();
	            String regex = null;
	            Integer flag = null;
	            int arrLength = 0;
	            for(Object[] arr : getXssPatternList()) {
	                arrLength = arr.length;
	                for(int i = 0; i < arrLength; i++) {
	                    regex = (String)arr[0];
	                    flag = (Integer)arr[1];
	                    list.add(Pattern.compile(regex, flag));
	                }
	            }
	            patterns = list;
	        }
	        return patterns;
	    }
	    public static String stripXss(String value) {
	        if(StringUtils.isNotBlank(value)) {
	            Matcher matcher = null;
	            for(Pattern pattern : getPatterns()) {
	                matcher = pattern.matcher(value);
	                // 匹配
	                if(matcher.find()) {
	                    // 删除相关字符串
	                    value = matcher.replaceAll("");
	                }
	            }
	            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	        }
	        return value;
	    }
	    
	    public static boolean stripXssCheck(String value) {
	        if(StringUtils.isNotBlank(value)) {

	            Matcher matcher = null;

	            for(Pattern pattern : getPatterns()) {
	                matcher = pattern.matcher(value);
	                // 匹配
	                if(matcher.find()) {
	                    return true;
	                }
	            }
	        }
	        return false;
	    }

	/**
	 * get ip address of client
	 * @param request
	 * @return
	 */
	public static String getClientIpAddress(HttpServletRequest request) {
		String xForwardedForHeader = request.getHeader("X-Forwarded-For");
		if (xForwardedForHeader == null) {
			return request.getRemoteAddr();
		} else {
			// As of https://en.wikipedia.org/wiki/X-Forwarded-For
			// The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
			// we only want the client
			return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
		}
	}
}
