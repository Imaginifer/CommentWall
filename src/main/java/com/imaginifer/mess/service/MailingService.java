/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.repo.CustomCommenterRepoImpl;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 *
 * @author imaginifer
 */
@Service
public class MailingService {
    
    private JavaMailSender sender;
    private CustomCommenterRepoImpl cr;
    private final String cim = "kividrotposta@gmail.com";

    @Autowired
    public MailingService(JavaMailSender sender, CustomCommenterRepoImpl ub) {
        this.sender = sender;
        this.cr = ub;
    }
    
    public void sendSimpleTestMail(){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(cim);
        mail.setTo("povazson@seznam.cz");
        mail.setSubject("Próba");
        mail.setText("Ha ezt megkaptad, a levelezés működik.");
        sender.send(mail);
    }
    
    public void sendValidator(String address, long passId){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(cim);
        mail.setTo(address);
        mail.setSubject("Hitelesítő hivatkozás");
        mail.setText("http://localhost:8080/messaging/valid?n=" + makeActivator(passId));
        sender.send(mail);
    }
    
    private String makeActivator(long ident){
        Random r = new Random();
        int chaff = r.nextInt(9000)+1000;
        StringBuilder sb = new StringBuilder();
        sb.append(chaff).append(ident);
        int kvant = Integer.parseInt(sb.toString()) * 17;
        return String.valueOf(kvant);
    }

    public long disentangleActivator(long valid) {
        String s;
        try {
            s = String.valueOf(valid / 17).substring(4);
        } catch (NullPointerException e) {
            return 0;
        }
        return Long.parseLong(s);
    }
}
