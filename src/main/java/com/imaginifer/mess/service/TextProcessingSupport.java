/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import java.util.*;
import java.util.regex.*;

/**
 *
 * @author imaginifer
 */
public class TextProcessingSupport {
    
    public static final String[] REPLY_MARKERS = {"@", "[", "]"};
    public static final String REPLY_LINK = "<a class=\"cimzett\" data-th-text = ${%s} data-th-href=\"@{'/messaging/msg/'+${%s}}\"></a>";
    
    public static boolean containsPattern(String toFind, String toLookAt){
        Pattern p = Pattern.compile(toFind);
        Matcher m = p.matcher(toLookAt);
        return m.find();
    }
    
    public static boolean containsPattern(String[] toFind, String toLookAt){
        boolean res = true;
        for (int i = 0; i < toFind.length; i++) {
            res = containsPattern(toFind[i], toLookAt);
            if(!res){
                return false;
            }
        }
        return res;
    }
    
    public static List<String> splitText(String text, String splitter){
        List<String> res = new ArrayList<>();
        String s = text.trim();
        if(!s.isEmpty() && containsPattern(splitter, s)){
            res = Arrays.asList(s.split(splitter));
        }
        return res;
    }
    
    public static List<String> findAndProcessReplyLinks(List<String> text){
        
        
        for (String s : text) {
            if(containsPattern(REPLY_MARKERS, s)){
                String prefix = "", suffix = "";
                
                int beginOfReply= s.indexOf(REPLY_MARKERS[0]);
                int beginOfIdent = s.indexOf(REPLY_MARKERS[1]);
                int endOfIdent = s.indexOf(REPLY_MARKERS[2]);

                String title = s.substring(beginOfReply, beginOfIdent);
                String ident = s.substring(beginOfIdent+1, endOfIdent);
                
                if(!s.startsWith(REPLY_MARKERS[0])){
                    prefix = s.substring(0, beginOfReply);
                } 
                if(!s.endsWith(REPLY_MARKERS[2])){
                    suffix = s.substring(endOfIdent+1);
                } 
                s = prefix + String.format(REPLY_LINK, title, ident) + suffix;
            }
        }
        return text;
    }
}
