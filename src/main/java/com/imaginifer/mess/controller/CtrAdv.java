/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.controller;

import java.io.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author imaginifer
 */
@ControllerAdvice
public class CtrAdv {
    
    @ExceptionHandler(Exception.class)
    public String hiba(Exception ex, Model mod){
        
        ByteArrayOutputStream str = new ByteArrayOutputStream();
        PrintStream pr = new PrintStream(str);
        ex.printStackTrace(pr);
        mod.addAttribute("errortext", ex.getMessage()==null
                ?"Valaki, valahol elrontott valamit":ex.getMessage());
        mod.addAttribute("stacktrace", str.toString());
        return "hiba";
    }
}
