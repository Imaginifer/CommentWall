/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Address;
import com.imaginifer.mess.entity.Payment;
import java.util.List;

/**
 *
 * @author imaginifer
 */
public interface CustomPaymentRepository {
    public List<Payment> searchPayments(int status, String txid, long commenterId, String currency);
    public List<Address> getAddresses(long id);
}
