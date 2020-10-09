/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.enums;

/**
 *
 * @author imaginifer
 */
public enum TopicAccess {
    ALL,        //nyíltan olvasható
    USER,       //felhasználóknak olvasható/írható
    ELECTOR,    //elektoroknak olvasható/írható, felhasználóknak olvasható
    INTERNAL,   //elektoroknak olvasható/írható,
    CONFIDENTIAL//meghívásos
}
