package com.eip.common.xss.filter;


import com.eip.common.xss.warpper.XssHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS过滤
 */
public class XssFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		XssHttpServletRequestWrapper requestWrapper = new XssHttpServletRequestWrapper((HttpServletRequest) request);
		chain.doFilter(requestWrapper, response);
	}
}