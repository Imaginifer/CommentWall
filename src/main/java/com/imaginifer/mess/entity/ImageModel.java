/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author imaginifer
 */
@Entity
public class ImageModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;
    private int imageSize;
    private boolean spoilered, hidden;
    private String imageTitle, contentType;
    @ManyToOne(optional=false)
    private Message attachedTo;
    @Lob
    private byte[] image;
    @Lob
    private byte[] thumbnail;

    public ImageModel(int imageSize, boolean spoilered, String imageTitle, 
            String contentType, Message attachedTo, byte[] image, byte[] thumbnail) {
        this.imageSize = imageSize;
        this.spoilered = spoilered;
        this.imageTitle = imageTitle;
        this.contentType = contentType;
        this.attachedTo = attachedTo;
        this.image = image;
        this.thumbnail = thumbnail;
        hidden = false;
    }

    public ImageModel() {
    }
    
    

    public void setSpoilered(boolean spoilered) {
        this.spoilered = spoilered;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public long getImageId() {
        return imageId;
    }

    public int getImageSize() {
        return imageSize;
    }

    public boolean isSpoilered() {
        return spoilered;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public Message getAttachedTo() {
        return attachedTo;
    }

    public byte[] getImage() {
        return image;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }
    
    
    
    
}
