/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.dto.Carrier;
import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Topic;
import com.imaginifer.mess.entity.Message;
import com.imaginifer.mess.dto.TopicView;
import com.imaginifer.mess.dto.CommenterView;
import com.imaginifer.mess.dto.MessageView;
import com.imaginifer.mess.numeralconv.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 * @author imaginifer
 */
public class ControllerSupport {
    
    public static List<MessageView> convertMessage (List<Message> x){
        List<MessageView> view = new ArrayList<>();
        for (Message m : x) {
            view.add(new MessageView(m.getCommenter().getUsername(), 
                    m.isDeleted()? "[TÖRÖLT] "+m.getText():m.getText(), 
                    customFormattedDate(m.getDate()),
                    m.getIdent(), m.getMsgId(), 
                    romanizedNumber(m.getNrInTopic()), 
                    m.isDeleted(), 
                    m.getTopic().getTitle(), 
                    m.getTopic().getTopicId(), 
                    m.isReply()?romanizedNumber(m.getReplyTo().getNrInTopic()):"",
                    m.isReply()?m.getReplyTo().getMsgId():0));
        }
        return view;
    }
    
    public static List<TopicView> convertTopic (List<Object[]> x){
        List<TopicView> view = new ArrayList<>();
        for (Object[] o : x) {
            Topic t = (Topic) o[0];
            long l = (Long) o[1];
            view.add(new TopicView(t.getTopicId(), t.getText(), t.getTitle(), l, 
                    customFormattedDate(t.getLastUpdate())));
        }
        return view;
    }
    
    public static List<CommenterView> convertCommenter(List<Commenter> x){
        List<CommenterView> view = new ArrayList<>();
        for (Commenter c : x) {
            view.add(new CommenterView(c.getUsername(), c.getCommenterId(), c.isDirector()
                    , customFormattedDate(c.getJoinDate())));
        }
        return view;
    }
    
    private static String customFormattedDate(LocalDateTime t){
        String s = String.join(". ", new String[] {String.valueOf(t.getYear()), 
            romanizedNumber(t.getMonthValue()), String.valueOf(t.getDayOfMonth())}); 
        return s+". "+t.toLocalTime().format(DateTimeFormatter.ofPattern("H:mm:ss"));
    }
    
    private static String romanizedNumber(long nr){
        String s = "";
        try {
            s = NumeralConverter.romanize(nr);
        } catch (NumeralConvException ex) {
            System.out.println(ex.getMessage());
        }
        return s;
    }
    
    public static List<TopicView> listOfSearchLimits(){
        List<TopicView> result = new ArrayList<>();
        result.add(new TopicView(0, "50 találatig"));
        result.add(new TopicView(1, "150 találatig"));
        result.add(new TopicView(2, "450 találatig"));
        result.add(new TopicView(3, "900 találatig"));
        return result;
    }

    public static List<TopicView> listOfOrderings(){
        List<TopicView> result = new ArrayList<>();
        result.add(new TopicView(0, "Létrehozás"));
        result.add(new TopicView(1, "Frissítés"));
        result.add(new TopicView(3, "Üzenetek"));
        result.add(new TopicView(5, "Cím"));
        return result;
    }

    public static List<TopicView> listOfStatus(){
        List<TopicView> result = new ArrayList<>();
        result.add(new TopicView("no", "Mind"));
        result.add(new TopicView("", "Csak törletlen"));
        result.add(new TopicView("yes", "Csak törölt"));
        return result;
    }
    
    public static String searchUrl(Carrier cr) {
        
        int order = cr.getDesc() == null ? cr.getOrd() : cr.getOrd()!=0? cr.getOrd()+1 : 0; 
        String ord = order!=0 ? "ord=" + order : "";
        String ct = cr.getCt() != 0 ? "&ct="+cr.getCt() : "";
        String del = cr.getDel() != null ? "&only="+cr.getDel() : "";
        String tx = !cr.getTx().isEmpty() ? "&tx=" + cr.getTx() : "";
        String nm = !cr.getNm().isEmpty() ? "&nm=" + cr.getNm() : "";
        String tp = cr.getTp()!=0 ? "&top=" + cr.getTp() : "";
        String start = (cr.getOrd()== 0
                && cr.getCt() == 0
                && cr.getTx().isEmpty()
                && cr.getNm().isEmpty()
                && cr.getTp()==0
                && cr.getDel() == null) ? "" : "?";
        return start + ord + ct + nm + tx + tp + del;
    }
    
    public static String hiba(String err) {
        String[] st = {"Ismeretlen hiba",
            "A megadott felhasználónév már foglalt, kérjük válasszon másikat!",
            "A két megadott jelszó nem egyezik!",
            "A hitelesítő hivatkozás elévült."};
        int q = 0;
        try {
            q = Integer.parseInt(err);
        } catch (NumberFormatException e) {
            return st[q];
        }
        int k = q < 0 || q > st.length-1 ? 0 : q;
        //int k = Math.max(0, Math.min(st.length - 1, q));
        return st[k];
    }
    
    public static String responseMsg(String num){
        String[] s = {"Ez itt a visszajelző oldal.",
                "A megadott címére hitelesítő üzenetet fog kapni, kattintson az "
                + "ebben található hivatkozásra, és beléphet új fiókjával!",
                "Felhasználói fiókjának hitelesítése sikeres volt, már beléphet jelszavával!"
        };
        int q = 0;
        try {
            q = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return s[q];
        }
        int k = q < 0 || q > s.length-1 ? 0 : q;
        return s[k];
    }
    

}
