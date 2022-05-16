/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.enums.*;

/**
 *
 * @author imaginifer
 */
public class SettingsDetail {
    public static final int MSG_LIMIT = 300;
    public static final int THREAD_LIMIT = 150;
    public static final int CHAR_LIMIT = 2500;
    public static final int MEDIA_MB_LIMIT = 16;
    public static final int THUMBNAIL_SIZE = 128;
    
    public static final SanctionType[] LOCAL_ENACTING_SANCTIONS = {SanctionType.ADMINISTRATOR, SanctionType.MODERATOR};
    public static final UserRank[] GLOBAL_ENACTING_RANKS = {UserRank.ROLE_DIRECTOR, UserRank.ROLE_SUBJECTOR};
}
