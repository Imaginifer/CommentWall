/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg;

import com.progmatic.msg.dto.*;
import com.progmatic.msg.entity.Message;
import com.progmatic.msg.service.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author imaginifer
 */
@RestController
public class RestMsgControl {
    
    MsgServiceImpl msg;
    UserStats us;
    
    @Autowired
    RestMsgControl(MsgServiceImpl msg, UserStats us){
        this.msg = msg;
        this.us = us;
    }
    
    @GetMapping("/api/messaging/{msgId}")
    public MessageView getOneMessage(@PathVariable("msgId") String id){
        Message x = msg.pickMsg(id, us.isAdmin(), false).get(0);
            return new MessageView(x.getUsername(),x.getText()
                        ,x.getDate(),x.getMsgId(),x.isDeleted(),x.getTopic().getTitle()
                        ,x.isReply()?x.getReplyTo().getMsgId():0);
    }
    
    @PostMapping("/api/messaging/new")
    public MessageView newMessage (@Valid @RequestBody MessageData m){
        return new MessageView(m.getName(),m.getText()
                    ,LocalDateTime.now().format(DateTimeFormatter
                            .ofPattern("yyyy.MM.dd HH:mm:ss"))
                    ,1,false,m.getNewTopic(),0);
                    
    }
    
    @RequestMapping(value="/api/messaging/{msgId}/delete", method = RequestMethod.DELETE)
    public void deleteMsg(@PathVariable("msgId") String id){
        
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError err = new ApiError();
        err.setCode(status.value());
        err.setStatus(status.getReasonPhrase());
        err.setDetails(Arrays.asList(ex.getMessage()));
        
        return ResponseEntity.status(status).body(err);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex){
        List<String> errors = new ArrayList<>();
        for (FieldError f : ex.getBindingResult().getFieldErrors()) {
            errors.add(f.getField() + ": "+f.getDefaultMessage());
        }
        
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError err = new ApiError();
        err.setCode(status.value());
        err.setStatus(status.getReasonPhrase());
        err.setDetails(errors);
        
        return ResponseEntity.status(status).body(err);
    }
}
