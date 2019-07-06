/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg;

import com.progmatic.msg.service.*;
import java.time.LocalDateTime;
import java.util.*;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.*;
import static org.mockito.Mockito.times;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author imaginifer
 */
public class ControlClassTest {
    
    MockMvc mockmvc;
    private MsgServiceImpl msg; // pótlékosztályok
    @Mock private UserStats us;
    
    public ControlClassTest() {
    }
    
    /*@Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);         // enélkül nullpointerre fut
        msg = Mockito.mock(MsgServiceImpl.class);   // @Mock annotációval is működik init helyett
        //us = Mockito.mock(UserStats.class);
        List<Message> msgReturn = new ArrayList<>();
        msgReturn.add(new Message("Test", "TEST", LocalDateTime.of(2008, 11, 27, 23, 6, 54),1));
        msgReturn.add(new Message("Test2", "TEST2", LocalDateTime.now(),2));    //próbaüzenet
        Mockito.when(msg.getMsg(Mockito.anyString()
                , Mockito.anyString()
                , Mockito.anyString()
                , Mockito.anyString()
                , Mockito.anyBoolean()
                , Mockito.anyString()))     //tetszőleges bemeneti érték
                .thenReturn(msgReturn);     // a visszaküldendő
        List<Message> msgReturn2 = new ArrayList<>();
        msgReturn2.add(new Message("Test", "TEST", LocalDateTime.of(2008, 11, 27, 23, 6, 54),1));
        Mockito.when(msg.pickMsg(Mockito.anyString())).thenReturn(msgReturn2);
        mockmvc = MockMvcBuilders.standaloneSetup(new ControlClass(msg, us))
                .build();
    }*/

    /**
     * Test of displayMsg method, of class ControlClass.
     */
    @Test
    public void testDisplayMsg() throws Exception{
        mockmvc.perform(MockMvcRequestBuilders.get("/messaging"))
                .andExpect(MockMvcResultMatchers.view().name("m1"));    // tesztelt visszatérés a view-vel
    }
    
    @Test
    public void testDisplayMsg2() throws Exception{
        mockmvc.perform(MockMvcRequestBuilders.get("/messaging"))  
                .andExpect(MockMvcResultMatchers.status().isOk())    // visszatérés státuszkóddal, lehet isForbidden, isNotFound, stb.
                .andExpect(MockMvcResultMatchers.model().attributeExists("messages"))   // attribútum tesztelése
                .andExpect(MockMvcResultMatchers.model().attribute("messages", Matchers.hasSize(2)))  // attribútum érték tesztelése, mockolt objektum kéne
                .andExpect(MockMvcResultMatchers.model().attribute("messages", Matchers.hasItem(
                        Matchers.allOf(
                                Matchers.hasProperty("username", Matchers.is("Test:")),
                                Matchers.hasProperty("text", Matchers.is("TEST"))
                        )
                )));
                
                
        //List<Message> returned = (List<Message>) result.getModelAndView().getModel().get("messages");   // a map listává alakítása
        //assertEquals(2, returned.size());
    }

    /**
     * Test of displayOne method, of class ControlClass.
     */
    @Test
    public void testDisplayOne() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/messaging/2"))
                //.andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.view().name("m1"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("messages", Matchers.hasSize(1)));
    }

    /**
     * Test of newMsgForm method, of class ControlClass.
     */
    @Test
    public void testNewMsgForm() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/messaging/new"))
                .andExpect(MockMvcResultMatchers.view().name("m2"));
    }

    /**
     * Test of newMsg method, of class ControlClass.
     */
    /*@Test
    public void testNewMsg() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.post("/messaging/new")
                .param("name", "author").param("text","example"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost:8080/messaging"));
        
        ArgumentCaptor<MessageData> msgParam = ArgumentCaptor.forClass(MessageData.class); // a post által hozzáadott
        Mockito.verify(msg, times(1)).addNew(msgParam.capture().getName(),msgParam.capture().getText());
    }*/

    /**
     * Test of searchForm method, of class ControlClass.
     */
    @Test
    public void testSearchForm() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/messaging/search"))
                .andExpect(MockMvcResultMatchers.view().name("m3"));
    }

    /**
     * Test of searchRes method, of class ControlClass.
     */
    @Test
    public void testSearchRes() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.post("/messaging/search"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost:8080/messagingnull"));
    }

    /**
     * Test of problem method, of class ControlClass.
     */
    @Test
    public void testProblem() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/messaging/problem?err=0"))
                .andExpect(MockMvcResultMatchers.view().name("hiba"));
    }

    /**
     * Test of hideMessage method, of class ControlClass.
     */
    @Test
    public void testHideMessage() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/messaging/delete/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost:8080/messaging"));
    }

    /**
     * Test of restoreMessage method, of class ControlClass.
     */
    @Test
    public void testRestoreMessage() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/messaging/restore/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost:8080/messaging"));
    }
    
}
