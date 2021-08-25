/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, CustomPaymentRepository{
    
    public Payment findOnePaymentByPaymentId(long paymentId);
    @Query("select p from Payment p where p.resolved = false order by p.paymentId")
    public List<Payment> getAllPendingPayments();
    
}
