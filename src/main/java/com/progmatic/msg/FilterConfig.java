/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;

/**
 *
 * @author imaginifer
 */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean filterReg1(){
        FilterRegistrationBean fb = new FilterRegistrationBean();
        //fb.addUrlPatterns("/messages/*");
        fb.setFilter(new CharEncodingFilter());
        return fb;
    }
}
