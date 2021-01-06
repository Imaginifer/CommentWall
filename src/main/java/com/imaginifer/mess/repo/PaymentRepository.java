/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public interface PaymentRepository extends JpaRepository<Long, Payment>, CustomPaymentRepository{
    
    public void newPayment(Payment payment);
    public void deletePayment(Payment payment);
    public Payment findOnePaymentByPaymentId(long paymentId);
    
}
