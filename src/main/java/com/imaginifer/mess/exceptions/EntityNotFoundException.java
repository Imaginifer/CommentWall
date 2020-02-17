/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.exceptions;

/**
 *
 * @author imaginifer
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Creates a new instance of <code>EntityNotFoundException</code> without
     * detail message.
     */
    public EntityNotFoundException() {
    }

    /**
     * Constructs an instance of <code>EntityNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
