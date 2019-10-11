package zhu.liang.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过Filter过滤器来防SQL注入攻击
 * 
 */
public class SQLFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(SQLFilter.class);
	
	/**
	 * 
	 */
	public void destroy() {

	}
	
	@SuppressWarnings("rawtypes")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		try{
	        HttpServletRequest req = (HttpServletRequest) request;
	        
	        req.setCharacterEncoding("UTF-8");
	        
	        if(req.getQueryString()!=null){
	        	URLDecoder.decode( req.getQueryString(), "UTF-8");
	        }
	        HttpServletResponse res = (HttpServletResponse) response;
	        //获得所有请求参数名
	        Enumeration params = req.getParameterNames();
	        String sql = "";
	        boolean b = false;
	        //logger.info("遍历参数列表：");
	        while (params.hasMoreElements()) {
	             //得到参数名
	             String name = params.nextElement().toString();
//	             logger.info("参数名称name="+name);
	             //得到参数对应值
	        	 String[] value = req.getParameterValues(name);
//	        	 logger.info("参数值value="+value);
	        	 if(value != null && value.length > 0)
	             for (int i = 0; i < value.length; i++) {
	            	 if(StringUtils.isNotBlank(value[i])){
	            		 sql = sql + value[i]+" "+name+" ";
	            	 }
	            	 if(b){
	            	     break;
	            	 }
	             }
	        }
	        //有sql关键字，跳转到error.html
	        if (b || (StringUtils.isNotBlank(sql)&&sqlValidate(sql))) {
	            //res.sendRedirect(req.getRequestURL()+"");
	            HttpServletResponse httpResponse = (HttpServletResponse) response;
	            httpResponse.setCharacterEncoding("UTF-8");
				httpResponse.reset();
				httpResponse.resetBuffer();
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter printWriter = httpResponse.getWriter();
				printWriter.println("<html>");
				printWriter.println("<head><title></title></head>");
				printWriter.println("<body>");
				printWriter.println("<SCRIPT LANGUAGE=\"JavaScript\">");
				printWriter.println("alert('查询字符串存在非法字符!');");
				printWriter.println("var currentWindow = window;");
				printWriter.println("if(currentWindow != null){");
				printWriter.println(" location= \"logoutAction.action\";");
				printWriter.println("}");
				printWriter.println("</SCRIPT>");
				printWriter.println("</body>");
				printWriter.println("</html>");
				printWriter.flush();
				printWriter.close();
	        } else {
	            chain.doFilter(req, res);
	        }
		}catch(Exception ex){
		    logger.error("SQLFilter doFilter error:"+ex.getMessage());
			ex.printStackTrace();
		}
    }


	//效验
    protected static boolean sqlValidate(String str) {
        try
        {
            str = str.toLowerCase();//统一转为小写
            //String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like";
            String badStr = "'|and|exec|execute|insert|drop|table |from|grant|group_concat|column_name|frame|iframe|script|" +
                    "information_schema.columns|table_schema|union|where|select|delete|update|" +
                    "chr|mid|master|truncate|declare|like|#|<|>";//|//|/|--|+|\\|$|!|?";//过滤掉的sql关键字，可以手动添加
            String[] badStrs = badStr.split("\\|");
            
            String[] arr={
                    "<\\s{0,}/\\s{0,}[iI]\\s{0,}[Ff]\\s{0,}[Rr]\\s{0,}[Aa]\\s{0,}[Mm]\\s{0,}[Ee]\\s{0,}>",
                    "<\\s{0,}\\s{0,}[iI]\\s{0,}[Ff]\\s{0,}[Rr]\\s{0,}[Aa]\\s{0,}[Mm]\\s{0,}[Ee]\\s{0,}>",
                    "<\\s{0,}[Ss]\\s{0,}[Cc]\\s{0,}[Rr]\\s{0,}[Ii]\\s{0,}[Pp]\\s{0,}[Tt]\\s{0,}>",
                    "<\\s{0,}/\\s{0,}[Ss]\\s{0,}[Cc]\\s{0,}[Rr]\\s{0,}[Ii]\\s{0,}[Pp]\\s{0,}[Tt]\\s{0,}>",
                    "\\s{0,}[Ee]\\s{0,}[Xx]\\s{0,}[Pp]\\s{0,}[Rr]\\s{0,}[Ee]\\s{0,}[Ss]\\s{0,}[Ss]\\s{0,}[Ii]\\s{0,}[Oo]\\s{0,}[Nn]"
            };
            for(String a:arr){
                Pattern pattern = Pattern.compile(a);
                Matcher matcher = pattern.matcher(str);
                boolean b= matcher.find();
                if(b){
                    logger.info(">>>>>>>>>>>>arr:"+a);
                    return true;
                }
            }
            for (int i = 0; i < badStrs.length; i++) {
                if (str.indexOf(badStrs[i]) !=-1 || str.indexOf("\\|")!=-1 ) {
                    logger.info(">>>>>>>>>>>>:"+badStrs[i]);
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("sqlValidate error:"+e.getMessage()+" >>>>param:"+str);
            e.printStackTrace();
        }
        return false;
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
 

	public void init(FilterConfig arg0) throws ServletException {
	}
}
