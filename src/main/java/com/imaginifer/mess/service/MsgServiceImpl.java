/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.exceptions.EntityNotFoundException;
import com.imaginifer.mess.repo.TopicRepository;
import com.imaginifer.mess.repo.CustomMsgRepoImpl;
import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Topic;
import com.imaginifer.mess.entity.Message;
import com.imaginifer.mess.dto.*;
import com.imaginifer.mess.entity.Forum;
import com.imaginifer.mess.entity.ImageModel;
import com.imaginifer.mess.entity.MsgCounter;
import com.imaginifer.mess.enums.TopicStatus;
import com.imaginifer.mess.numeralconv.*;
import com.imaginifer.mess.repo.ImageRepository;
import com.imaginifer.mess.repo.SearchRepository;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
 *
 * @author imaginifer
 */
@Service
@Transactional(readOnly = true)
public class MsgServiceImpl {
    
    private final CustomMsgRepoImpl msgrepo;
    private final TopicRepository topicrepo;
    private final SearchRepository sr;
    private final WebUtilService wu;
    private final ImageRepository ir;

    
    @Autowired
    public MsgServiceImpl(TopicRepository topicrepo, CustomMsgRepoImpl msgrepo, 
            WebUtilService wu, SearchRepository sr, ImageRepository ir) {
        
        this.topicrepo = topicrepo;
        this.msgrepo = msgrepo;
        this.wu = wu;
        this.sr = sr;
        this.ir = ir;
    }

    @Transactional(readOnly = false)
    public long addNew(MessageData ms) {
        Commenter c = wu.getCurrentUser();
        Topic tp = topicHandling(ms.getText(), ms.getChosenTopic(), ms.getNewTopic(), 
                /*ms.getForumId()*/1, ms.isNotUpdating() && ms.getNewTopic() != null || !ms.getNewTopic().isEmpty());
        if(tp.getStatus() == TopicStatus.ARCHIVED){
            return 0;
        }
        String nr = ms.getNewTopic() == null || ms.getNewTopic().isEmpty()? getNewNrInTopic(tp.getTopicId()):"I";
        LocalDateTime current = LocalDateTime.now();
        msgrepo.addNew(new Message(c, ms.getText(), current, tp, nr,wu.getRequestIdent()));
        if(tp.getStatus() != TopicStatus.LOCKED && !ms.isNotUpdating()){
            tp.setLastUpdate(current);
        }
        updateCounter(c, tp.getForum());
        ageTopics(tp.getForum().getForumId());
        return tp.getTopicId();
    }

    private Topic topicHandling(String text, long topicId, String newTitle, long forumId,
            boolean hidden) {
        Forum f = topicrepo.getForumById(forumId);
        if (newTitle == null || newTitle.isEmpty()) {
            f.newMessage();
            return topicrepo.findFirstTopicByTopicId(topicId);
        }
        
        Topic t = new Topic(newTitle, text, f, hidden);
        f.newTopic();
        return topicrepo.newTopic(t);
    }

    public List<String> allTopicTitles() {
        List<String> x = topicrepo.findAllTopicTitle();
        return x;
    }
    
    public List<TopicView> allTopicTitlesWithIds(){
        /*TreeSet<Topic> x = new TreeSet<>(new Comparator<Topic>(){
           @Override
            public int compare(Topic o1, Topic o2) {
                if(o1.getLastUpdate().isAfter(o2.getLastUpdate()) 
                        || o1.getLastUpdate().isEqual(o2.getLastUpdate())){
                    return -1;
                }
                return 1;
            }
        });*/
        List<Object[]> x = topicrepo.displayTopics(1);
        List<TopicView> result = new ArrayList<>();
        result.add(new TopicView(0, "Bármely topik"));
        for (Object[] o : x) {
            Topic tp = (Topic) o[0];
            result.add(new TopicView(tp.getTopicId(), tp.getTitle()));
        }
        return result;
    }

    public List<MessageView> getMsg(int order, int count, int textOption, String name,
             String text, String title, long topic, String only) {
        
        int deleted = !only.isEmpty() && wu.isDirector() ? (only.equals("yes") ? 2 : 1) : 0;
        
        return ControllerSupport.convertMessage(sr.filterMessages(order!=0, countNr(count), 
                name, textOption!=0 ? 
                        TextProcessingSupport.splitText(text, " "):
                        new ArrayList<>(Arrays.asList(new String[]{text})), 
                title, topic, deleted, textOption));
    }
    
    public List<MessageView> getTopic(long topic){
        return ControllerSupport.convertMessage(msgrepo.displayTopic(topic, wu.isDirector()));
    }
    
    private int countNr(int count){
        int ct = 50;
        switch(count){
            case 3:
                ct *= 2;
            case 2:
                ct *= 3;
            case 1:
                ct *= 3;
            default:
                break;
        }
        return ct;
    }
    
    private String getNewNrInTopic(long topicId){
        long nr = 1+msgrepo.countMessagesInTopic(topicId);
        String res = "N";
        try {
            res = NumeralConverter.romanize(nr);
        } catch (NumeralConvException e) {
            System.out.println(e.getMessage());
        } finally {
           return res;
        }
    }
    
    public List<MessageView> pickMsg(long id, boolean all) {
        List<Message> list = new ArrayList<>();
        boolean allowed = wu.isDirector();
        try{
            list = msgrepo.pickWithReplies(id);
        }catch(NoSuchElementException ex){
            throw new EntityNotFoundException("Ilyen üzenet nem létezik!");
        }
        if (list.isEmpty() || !allowed && list.get(0).isDeleted()){
            throw new EntityNotFoundException("Ilyen üzenet nem létezik!");
        }
        if(all){
            list.addAll(list.get(0).getReplies());
            //list.sort((m1, m2) -> Long.compare(m1.getMsgId(), m2.getMsgId()));
            if(!allowed){
                list = list.stream().filter(m -> !m.isDeleted())
                        .collect(Collectors.toList());
            }
        }
        return ControllerSupport.convertMessage(list);
    }
    
    /*public List<Message> findMsg(String name, String text, List<Message> in) {
        List<Message> x = new ArrayList<>();
        Pattern f1 = Pattern.compile(name);
        Pattern f2 = Pattern.compile(text);
        in.forEach(ms -> {
            Matcher m1 = f1.matcher(ms.getUsername());
            Matcher m2 = f2.matcher(ms.getText());
            if (m1.find() && m2.find()) {
                x.add(ms);
            }
        });
        return x;
    }*/

    
    @PreAuthorize("hasRole('DIRECTOR')")
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
    public void deleteMsg(long del, boolean restore) {
        try{
            msgrepo.hideOrRestore(del, restore);
        }catch(NoSuchElementException ex){
            throw new EntityNotFoundException("Message with "+del+" not found!");
        }
    }

    @PreAuthorize("hasRole('DIRECTOR')")
    @Transactional(readOnly = false)
    public void removeTopic(long top) {
        topicrepo.removeTopic(top);
    }

    public List<TopicView> displayTopics(long forumId){
        /*List<Topic> topics = topicrepo.displayTopics();
        topics.sort((t1, t2) -> {
            List<Message> topic1 = t1.getMessages();
            topic1.sort((m1,m2) -> m2.getDate().compareTo(m1.getDate()));
            List<Message> topic2 = t2.getMessages();
            topic2.sort((m1,m2) -> m2.getDate().compareTo(m1.getDate()));
            return topic2.get(0).getDate()
                    .compareTo(topic1.get(0).getDate());});*/
        return ControllerSupport.convertTopic(topicrepo.displayTopics(forumId));
    }
    
    public String getTopicName(long topicId){
        return topicrepo.findFirstTopicByTopicId(topicId).getTitle();
    }
    
    public long getTopicId(long msgId){
        return msgrepo.getMessageById(msgId).getTopic().getTopicId();
    }
    
    private void updateCounter(Commenter c, Forum f){
        MsgCounter x = msgrepo.getMessageCounter(c.getCommenterId(), f.getForumId());
        if(x == null){
            msgrepo.newMessageCounter(new MsgCounter(c, f));
        } else {
            x.update();
        }
    }
    
    @Transactional(readOnly = false)
    public void newReply(String text, long replied){
        Message m = msgrepo.getMessageById(replied);
        String nr = getNewNrInTopic(m.getTopic().getTopicId());
        Commenter c = wu.getCurrentUser();
        msgrepo.addNew(new Message(c, text, LocalDateTime.now(),
                m.getTopic(), nr, m, wu.getRequestIdent()));
        updateCounter(c, m.getTopic().getForum());
        ageTopics(m.getTopic().getForum().getForumId());
    }
    
    private void ageTopics(long forumId){
        List<Object[]> topics = topicrepo.displayTopics(forumId);
        for (int i = 0; i < topics.size(); i++) {
            Topic t = (Topic) topics.get(i)[0];
            long countMsg = (Long) topics.get(i)[1];
            if(t.getStatus() != TopicStatus.PERMANENT && countMsg >= SettingsDetail.MSG_LIMIT){
                t.setStatus(TopicStatus.LOCKED);
            }
            if(i > SettingsDetail.THREAD_LIMIT){
                t.setStatus(TopicStatus.ARCHIVED);
            }
        }
    }
    
    @Transactional(readOnly = false)
    private boolean processImage (MultipartFile img, Message body, boolean spoilered) throws IOException{
        
        if(img == null || img.getSize() > (SettingsDetail.MEDIA_MB_LIMIT*1024*1024)){
            return false;
        }
        
        byte[] fullImage = img.getBytes();
        byte[] thumbnail = generateThumbnail(fullImage);
        String name = img.getOriginalFilename();
        String type = img.getContentType();
        long fileSize = img.getSize(); //byte!
        
        ImageModel res = new ImageModel(Math.round((float)fileSize/1024), spoilered, name, type, body, fullImage, thumbnail);
        ir.save(res);
        return true;
    }
    
    private byte[] generateThumbnail(byte[] orig) throws IOException{
        
        BufferedImage bi = resizeImage(ImageIO.read(new ByteArrayInputStream(orig)));
        
        byte[] res = null;
        ByteArrayOutputStream x = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", x);
        x.flush();
        res = x.toByteArray();
        return res;
    }
    
    private BufferedImage resizeImage(BufferedImage original){
        if(original.getHeight() <= SettingsDetail.THUMBNAIL_SIZE 
                && original.getWidth() <= SettingsDetail.THUMBNAIL_SIZE){
            return original;
        }
        float shrinkRatio = original.getHeight() > original.getWidth() ? 
                (float) SettingsDetail.THUMBNAIL_SIZE / original.getHeight() : 
                (float) SettingsDetail.THUMBNAIL_SIZE / original.getWidth();
        int newHeight = Math.round(original.getHeight()*shrinkRatio);
        int newWidth = Math.round(original.getWidth()*shrinkRatio);
        
        Image x = original.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        BufferedImage bi = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        bi.getGraphics().drawImage(x, 0, 0, null);
        return bi; 
    }
    
    public String imageUploadTest(MultipartFile img){
        Message m = msgrepo.getMessageById(1);
        try{
            processImage(img, m, false);
        } catch (IOException e) {
            return e.getMessage();
        }
        return "Sikeres feltöltés!";
    }
    
}
