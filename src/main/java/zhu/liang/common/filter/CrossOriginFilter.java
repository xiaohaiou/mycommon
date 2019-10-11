package zhu.liang.common.filter;

import java.awt.geom.IllegalPathStateException;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解决本地代码跨域配置，只有在localhost 和127.0.0.1的时候有效
 * 引入该累后无需在接口上加CrossOrigin注解
 * @author zhengmingzhi
 *
 */
public class CrossOriginFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(CrossOriginFilter.class);
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // 得到请求的uri和url
//        String reqUri = req.getRequestURI();
        String reqUrl = req.getRequestURL().toString();
        log.info(" reqUrl:{} ",  reqUrl);

        // 跨域处理
        this.crossDomain(req, resp,reqUrl);
        filterChain.doFilter(req, resp);
    }
    
//    String getIp = "((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)(\\.((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)){3}";
//    Pattern patternIp = Pattern.compile(getIp);
//    // ^(?=^.{3,255}$)(http(s)?:\/\/)?(www\.)?[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:\d+)*(\/\w+\.\w+)*$
//    String getDomain = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:\\d+)*(\\/\\w+\\.\\w+)*";

    /**
     * 处理跨域问题,并验证如果不是ule.com过来的请求不响应，先用于beta测试
     */
    private void crossDomain(HttpServletRequest request, HttpServletResponse response,String reqUrl) {
        String referer = request.getHeader("REFERER");
        log.info("REFERER:{}", referer);
//        String reqRefererDomain = "";
//        Matcher matcherIp = patternIp.matcher(url);
//        if(matcherIp.matches())
        if(referer == null || referer.length() <7 ) {
        	return ;
        }
        if((referer.indexOf("localhost") >= 0 || referer.indexOf("127.0.0.1") >= 0) && (reqUrl.indexOf("localhost") >= 0 || reqUrl.indexOf("127.0.0.1") >=0)){
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin") );

            response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers",
                    "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, Cookie");
        }else if (referer.indexOf(".com")>0 && referer.indexOf("beta.") >= 0 && referer.length() > referer.indexOf(".com") + 4 ) {
//            reqRefererDomain = referer.substring(0, referer.indexOf(".com") + 4);
//            String origin = request.getHeader("Origin");
//            log.info("Origin:{}", origin);
            if(referer.indexOf(".ule.com") ==-1){
                throw new IllegalPathStateException("访问来源非法");
            }
        }

        
    }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
