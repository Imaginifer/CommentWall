/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.service;

import com.progmatic.msg.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author imaginifer
 */
@Service
public class MsgServiceImpl {
    private int nrId;
    private List<Message> m;

    public MsgServiceImpl() {
        nrId=0;
        m=fillerMsg();
    }

    public void addNew( String name, String text){
        m.add(new Message(name, text, LocalDateTime.now(),++nrId));
    }
    private List<Message> fillerMsg(){
        List<Message> x = new ArrayList<>();
        //int nrId=0;
        x.add(new Message("Techno Kolos", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", LocalDateTime.of(2008, 11, 27, 23, 6, 54),++nrId));
        x.add(new Message("Feles Elek", "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", LocalDateTime.of(2017, 3, 14, 2, 55, 7),++nrId));
        x.add(new Message("Citad Ella", "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.", LocalDateTime.of(1998, 6, 20, 9, 15, 9),++nrId));
        x.add(new Message("Tank Aranka", "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",LocalDateTime.of(2011, 9, 5, 3, 43, 21),++nrId));
        return x;
    }
    public List<Message> getMsg(String order, String count, String name, String text){
        return countMsg(count, orderMsg(order, findMsg(name, text)));
    }
    public List<Message> orderMsg(String order, List<Message> y){
        int q;
        List<Message> x = new ArrayList<>();
        y.forEach((Message ms) -> x.add(ms));
        try {
            q=Integer.parseInt(order);
        } catch (NumberFormatException e) {
            q=0;
        }
        switch(q){
            case 1:
                x.sort((ms1,ms2) -> {
                    return ms1.getDate().compareTo(ms2.getDate());
                });
                break;
            case 2:
                x.sort((ms1,ms2) -> {
                    return ms2.getDate().compareTo(ms1.getDate());
                });
                break;
            case 3:
                x.sort((ms1,ms2) -> {
                    return ms1.getUsername().compareTo(ms2.getUsername());
                });
                break;
            case 4:
                x.sort((ms1,ms2) -> {
                    return ms2.getUsername().compareTo(ms1.getUsername());
                });
                break;
            case 6:
                x.sort((ms1, ms2) -> {
                    return ms2.getMsgId() - ms1.getMsgId();
                });
            default:
                break;
        }
        return x;
    }
    public List<Message> countMsg(String count, List<Message> ls){
        List<Message> x = new ArrayList<>();
        int n=0;
        if (count.equals("full") || ls.size()<2){
            return ls;
        } else {
            while(n<Math.max(2,ls.size()/2)){
                x.add(ls.get(n++));
            }
        }
        
        return x;
    }
    
    public List<Message> pickMsg (String id){
        int q=0;
        try {
            q=Integer.parseInt(id);
        } catch (NumberFormatException e) {
            q=0;
        }
        int n=Math.min(m.get(m.size()-1).getMsgId(), Math.max(0, q));
        List<Message> y = new ArrayList<>();
        //y.add(x.get(0));
        for (Message message : m) {
            if(message.getMsgId()==n){
                y.add(message);
                return y;
            }
        }
        return y;
    }
    
    public String searchUrl(Carrier cr){
        
        String o=cr.getOrd();
        if(cr.getDesc()!=null){
            try {
                int k = Integer.parseInt(o) + 1;
                o = Integer.toString(k);
            } catch (NumberFormatException e) {
            }
        }
        String ord=!cr.getOrd().equals("")?"ord="+o:"";
        String ct=cr.getCt()!=null?"ct=h":"";
        String tx=!cr.getTx().equals("")?"tx="+cr.getTx():"";
        String nm=!cr.getNm().equals("")?"nm="+cr.getNm():"";
        String start = (cr.getOrd().equals("")
                &&cr.getCt()==null
                &&cr.getTx().equals("")
                &&cr.getNm().equals(""))?"":"?";
        String cts=ct.equals("")?"":"&";
        return start+ord+spc(ct)+ct+spc(nm)+nm+spc(tx)+tx;
    }
    private String spc (String inp){
        return inp.equals("")?"":"&";
    }
    
    public List<Message> findMsg(String name, String text){
        List<Message> x = new ArrayList<>();
        Pattern f1 = Pattern.compile(name);
        Pattern f2 = Pattern.compile(text);
        m.forEach(ms -> {
            Matcher m1 = f1.matcher(ms.getUsername());
            Matcher m2 = f2.matcher(ms.getText());
            if(m1.find() && m2.find()){
                x.add(ms);
            }
        });
        return x;
    }
}
