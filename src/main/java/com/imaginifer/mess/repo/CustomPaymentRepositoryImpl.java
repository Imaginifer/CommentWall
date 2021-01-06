/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository{

    @PersistenceContext
    EntityManager em;
    CriteriaBuilder cb = em.getCriteriaBuilder();
    
    @Override
    public List<Address> getAddresses(long id) {
        
        CriteriaQuery<Address> cq = cb.createQuery(Address.class);
        Root<Address> a = cq.from(Address.class);
        
        Predicate[] pred = new Predicate[1];
        if(id != 0){
            pred[0] = cb.equal(a.get(Address_.addressId), id);
        } else {
            pred = new Predicate[0];
        }
        
        cq.select(a).distinct(true).where(pred).orderBy(cb.asc(a.get(Address_.currency)));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Payment> searchPayments(int status, String txid, long commenterId, String currency) {
        
        CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
        Root<Payment> py = cq.from(Payment.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        if(status != 0){
            predicates.add(cb.equal(py.get(Payment_.resolved), status == 2));
        }
        if(txid != null && !txid.isEmpty()){
            predicates.add(cb.equal(py.get(Payment_.transactionId), txid));
        }
        if(currency != null && !currency.isEmpty()){
            predicates.add(cb.equal(py.get(Payment_.address).get(Address_.currency), currency));
        }
        if(commenterId != 0){
            predicates.add(cb.equal(py.get(Payment_.customer).get(Commenter_.commenterId), commenterId));
        }
        
        Predicate[] pp = predicates.toArray(new Predicate[predicates.size()]);
        cq.select(py).distinct(true).where(pp).orderBy(cb.desc(py.get(Payment_.creation)));
        
        return em.createQuery(cq).getResultList();
    }
    
}
