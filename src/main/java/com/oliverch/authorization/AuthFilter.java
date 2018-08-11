package com.oliverch.authorization;


import com.oliverch.common.result.Result;
import com.oliverch.common.result.ResultCode;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@WebFilter(urlPatterns = {"/test/*", "/staff/*", "/department/*", "/post/*", "/stuff/*"})
public class AuthFilter implements Filter {

    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter start");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String pathInfo = ((HttpServletRequest) servletRequest).getPathInfo();
        String jwt = getBearerToken(httpRequest);
        AuthService authService = new AuthService();
        if (authService.verifyJWT(jwt)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            servletResponse.getWriter().write(Result.failure(ResultCode.USER_NOT_LOGGED_IN).toString());
        }


    }

    @Override
    public void destroy() {
        System.out.println("Filter end");

    }

    private String getBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER_KEY);
        if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
            System.out.println(authHeader);
            return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
        }
        return null;
    }
}
