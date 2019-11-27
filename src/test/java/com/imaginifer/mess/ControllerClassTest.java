/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess;

import com.imaginifer.mess.ControllerClass;
import com.imaginifer.mess.dto.MessageData;
import com.imaginifer.mess.dto.MessageView;
import com.imaginifer.mess.service.MsgServiceImpl;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author imaginifer
 */
public class ControllerClassTest {
    
    MockMvc mockmvc;
    private MsgServiceImpl msg;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        msg = Mockito.mock(MsgServiceImpl.class);
        List<MessageView> msgReturn = new ArrayList<>();
        msgReturn.add(new MessageView("Commenter1", "Test message no.1", "2008.11.27 23:06:54", 1, true, "tests", 0));
        msgReturn.add(new MessageView("Commenter1", "Test message no.2", "2018.09.01 15:48:21", 2, true, "tests", 1));
        Mockito.when(msg.getMsg(Mockito.anyString()
                , Mockito.anyString()
                , Mockito.anyString()
                , Mockito.anyString()
                , Mockito.anyString()
                , Mockito.anyBoolean()
                , Mockito.anyString()))     
                .thenReturn(msgReturn);     
        List<MessageView> msgReturn2 = new ArrayList<>();
        msgReturn2.add(new MessageView("Commenter1", "Test message no.1", "2008.11.27 23:06:54", 1, true, "tests", 0));
        Mockito.when(msg.pickMsg(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean()))
                .thenReturn(msgReturn2);
        mockmvc = MockMvcBuilders.standaloneSetup(new ControllerClass(msg))
                .build();
        
    }
    
    @Test
    public void testDisplayMsg1() throws Exception{
         mockmvc.perform(MockMvcRequestBuilders.get("/messaging"))
                .andExpect(MockMvcResultMatchers.view().name("front.html"));
    }
    
    @Test
    public void testDisplayMsg2() throws Exception{
        mockmvc.perform(MockMvcRequestBuilders.get("/messaging"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messages"))
                .andExpect(MockMvcResultMatchers.model().attribute("messages", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.model().attribute("messages", Matchers
                        .hasItem(Matchers.allOf(Matchers.hasProperty("username", Matchers.is("Commenter1")), 
                                Matchers.hasProperty("topic", Matchers.is("tests"))))));
    }
    
    @Test
    public void testNewMsgForm() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/messaging/new"))
                .andExpect(MockMvcResultMatchers.view().name("writenew.html"));
    }
    
    @Test
    public void testNewMsg() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.post("/messaging/new")
                .param("newTopic", "More Tests").param("text","example")
                .param("replied", "").param("chosenTopic", "tests"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost:8080/messaging"));
        
        /*ArgumentCaptor<MessageData> msgParam = ArgumentCaptor.forClass(MessageData.class);
        Mockito.verify(msg).addNew(msgParam.capture().getText(),
                 msgParam.capture().getChosenTopic(), msgParam.capture().getNewTopic());*/
    }
}
