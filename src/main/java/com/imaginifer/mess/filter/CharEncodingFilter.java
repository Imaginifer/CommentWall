/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.filter;

import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author imaginifer
 */
@Component
public class CharEncodingFilter implements Filter{
    
    private final Logger logger = LoggerFactory.getLogger(CharEncodingFilter.class);

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) 
            throws IOException, ServletException {
        //sr.setCharacterEncoding("UTF-8");
        sr.getParameterMap().forEach((k, v) -> logger.info("{} - {}", k, v));
        
        //sr1.setContentType("text/html; charset=UTF-8");
        fc.doFilter(sr, sr1);
    }
    
    
    
}
