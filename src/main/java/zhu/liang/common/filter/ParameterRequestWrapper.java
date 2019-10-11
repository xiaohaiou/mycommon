package zhu.liang.common.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import zhu.liang.common.util.DataUtil;

public class ParameterRequestWrapper extends HttpServletRequestWrapper {

	public ParameterRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	@Override
	public String[] getParameterValues(String parameter) { 
		String[] results = super.getParameterValues(parameter); 
		if (results == null) 
			return null; 
		int count = results.length; 
		String[] trimResults = new String[count]; 
		for (int i = 0; i < count; i++) { 
			//XSS验证 去除js 和html的注入
			trimResults[i] = DataUtil.stripXss(results[i].trim()); 
			//验证SQL的注入
			trimResults[i] = DataUtil.replaceSqlWord(results[i].trim());
		} 
		return trimResults; 
	} 
	@Override  
    public String getParameter(String name) { 
		String value = super.getParameter(name);
		if (value == null) 
			return null; 
		//XSS验证 去除js 和html的注入
		value = DataUtil.stripXss(value.trim()); 
		value = DataUtil.replaceSqlWord(value.trim()); 
		return value; 
	}
	
	

}
