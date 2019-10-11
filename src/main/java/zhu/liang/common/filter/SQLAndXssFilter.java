package zhu.liang.common.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 通过Filter过滤器来防SQL注入攻击
 * 
 */
public class SQLAndXssFilter implements Filter {
//	private static Logger logger = LoggerFactory.getLogger(SQLAndXssFilter.class);
	public void destroy() {

	}
	
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//    	logger.info("ParameterRequestWrapper xss 过滤");
	        HttpServletRequest req = (HttpServletRequest) request;
	        
	        req.setCharacterEncoding("UTF-8");
	        
	        if(req.getQueryString()!=null){
	        	URLDecoder.decode( req.getQueryString(), "UTF-8");
	        }
	        ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper(req);
	        chain.doFilter(requestWrapper, response);
	       
    }

	public void init(FilterConfig arg0) throws ServletException {
	}
}
