/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.annot;

import java.lang.reflect.Parameter;
import java.util.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author imaginifer
 */
@Aspect
@Service
public class LoggableAspect {
    
    //private final Logger logger = LoggerFactory.getLogger(LoggableAspect.class);
    
    /*@Before("execution(* com.progmatic.msg.service.*.*(..))")
    public void test(JoinPoint jp){
        System.out.println(jp.getSignature().getName() + " starts");
    }*/
    
    @Around(value = "@annotation(LogAnnot) || @within(LogAnnot)")
    public Object logAll(ProceedingJoinPoint pjp) throws Throwable{
        String cl = MethodSignature.class.cast(pjp.getSignature())
                .getDeclaringTypeName();
        String met = MethodSignature.class.cast(pjp.getSignature())
                .getMethod().getName();
        List<Parameter> params = new ArrayList<>(Arrays.asList(MethodSignature
                .class.cast(pjp.getSignature())
                .getMethod().getParameters()));
        System.out.println("Osztály: "+cl);
        System.out.println("Metódus: "+met);
        params.forEach(pr -> System.out.println(pr.getName() + " " + pr.toString()));
        //logger.trace(pjp.getSignature().getName());
        return pjp.proceed();
    }
}
