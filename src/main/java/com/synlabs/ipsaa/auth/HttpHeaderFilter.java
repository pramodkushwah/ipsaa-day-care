package com.synlabs.ipsaa.auth;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpHeaderFilter extends GenericFilterBean
{
  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
  {
    HttpServletResponse httpResp = (HttpServletResponse) resp;
    httpResp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    httpResp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
    httpResp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    httpResp.setHeader("Access-Control-Allow-Origin", "*");
    chain.doFilter(req, resp);
  }
}
