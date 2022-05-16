/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Forum;
import com.imaginifer.mess.entity.Message;
import com.imaginifer.mess.entity.Permit;
import com.imaginifer.mess.entity.Topic;
import com.imaginifer.mess.enums.UserRank;
import com.imaginifer.mess.repo.CustomCommenterRepoImpl;
import com.imaginifer.mess.repo.CustomMsgRepoImpl;
import com.imaginifer.mess.repo.TopicRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author imaginifer
 */
@Service
public class DatabaseService {
    
    private final PasswordEncoder pwd;
    private final CustomCommenterRepoImpl cr;
    private final CustomMsgRepoImpl mr;
    private final TopicRepository tr;

    @Autowired
    public DatabaseService(PasswordEncoder pwd, CustomCommenterRepoImpl cr, CustomMsgRepoImpl mr, TopicRepository tr) {
        this.pwd = pwd;
        this.cr = cr;
        this.mr = mr;
        this.tr = tr;
    }
    
    public void checkPermits(){
        if(cr.noPermits()){
            cr.addNewPermit(new Permit(UserRank.ROLE_DIRECTOR));
            cr.addNewPermit(new Permit(UserRank.ROLE_USER));
        }
    }
    
    public void addAdmin(){
        if(cr.noDirector()){
            cr.registerNew(new Commenter("Imaginifer",pwd.encode(SecurityDetail.DIR_PWD),SecurityDetail.DIR_ADDR
                    ,LocalDateTime.of(1980, 1, 1, 9, 15),cr.getPermitByName(UserRank.ROLE_DIRECTOR)));
        }
    }
    
    public List<Commenter> fillerUsers(){
        List<Commenter> comm = new ArrayList<>();
        comm.add(new Commenter("Tank Aranka", pwd.encode(SecurityDetail.GENERIC_PWD), "tankari@citromail.com",
                LocalDateTime.of(1980, 1, 2, 12, 15),cr.getPermitByName(UserRank.ROLE_USER)));
        comm.add(new Commenter("Citad Ella", pwd.encode(SecurityDetail.GENERIC_PWD), "cella@citromail.com",
                LocalDateTime.of(1980, 1, 2, 12, 35),cr.getPermitByName(UserRank.ROLE_USER)));
        comm.add(new Commenter("Techno Kolos", pwd.encode(SecurityDetail.GENERIC_PWD), "teko@citromail.com",
                LocalDateTime.of(1980, 1, 2, 14, 45),cr.getPermitByName(UserRank.ROLE_USER)));
        comm.add(new Commenter("Feles Elek", pwd.encode(SecurityDetail.GENERIC_PWD), "felelek@citromail.com",
                LocalDateTime.of(1980, 1, 3, 9, 25),cr.getPermitByName(UserRank.ROLE_USER)));
        for (Commenter commenter : comm) {
            cr.registerNew(commenter);
        }
        return comm;
    }
    
    public void fillerMessages(){
        if(cr.nrOfAllCommenters() == 0 && mr.countNrOfMessages() == 0){
            List<Commenter> com = fillerUsers();
            Forum f = new Forum("Filler","Próbaüzenetek", false);
            tr.newForum(f);
            Topic t = tr.newTopic(new Topic("Lorem ipsum", "Lorem ipsum dolor sit amet, "
                    + "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", f, false));
            mr.addNew(new Message(com.get(2), "Lorem ipsum dolor sit amet, consectetur "
                    + "adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    LocalDateTime.of(1998, 6, 20, 9, 15, 9), t, "I", "123456"));
            mr.addNew(new Message(com.get(3), "Ut enim ad minim veniam, quis nostrud "
                    + "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 
                    LocalDateTime.of(2008, 11, 27, 23, 6, 54), t, "II", "1a2b3c"));
            mr.addNew(new Message(com.get(1), "Duis aute irure dolor in reprehenderit "
                    + "in voluptate velit esse cillum dolore eu fugiat nulla pariatur.", 
                    LocalDateTime.of(2011, 9, 5, 3, 43, 21), t, "III", "000a1e"));
            mr.addNew(new Message(com.get(0), "Excepteur sint occaecat cupidatat non "
                    + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", 
                    LocalDateTime.of(2017, 3, 14, 2, 55, 7), t, "IV","555eee"));
            t.setLastUpdate(LocalDateTime.of(2017, 3, 14, 2, 55, 7));
            Topic t2 = tr.newTopic(new Topic("Nyelvtörő", "A szerencsés csősz cserszömörcés sört szörcsöl.", f, true));
            mr.addNew(new Message(com.get(0), "A szerencsés csősz cserszömörcés sört szörcsöl.", 
                    LocalDateTime.of(2002, 8, 1, 9, 8, 47), t2, "I", "000000"));
            Message m1 = new Message(com.get(1), "Sepsiszentgyörgyi szájsebész-asszisztensre sincs szükségem.", 
                    LocalDateTime.of(2002, 8, 3, 17, 32, 11), t2, "II", "aaabbb"); 
            mr.addNew(m1);
            Message m2 = new Message(com.get(3), "Fekete bikapata pattog a patika pepita kövén.", 
                    LocalDateTime.of(2007, 1, 20, 6, 14, 33), t2, "III", m1, "4d5e6f");
            mr.addNew(m2);
            mr.addNew(new Message(com.get(2), "Az ibafai papnak fapipája van, így az "
                    + "ibafai papi pipa papi fapipa.", 
                    LocalDateTime.of(2015, 5, 14, 20, 3, 14), t2, "IV", m2, "ffffff"));
            t2.setLastUpdate(LocalDateTime.of(2015, 5, 14, 20, 3, 14));        
        }
    }
}
