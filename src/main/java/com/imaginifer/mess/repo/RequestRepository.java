/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.RequestLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public interface RequestRepository extends JpaRepository<Integer, RequestLog>{
    
    public RequestLog findOneRequestLogByAddresHash(int addressHash);
    public void newRequestLog(RequestLog requestLog);
    public void deleteRequestLog(RequestLog requestLog);
    public List<RequestLog> getAllRequestLogs();
}
