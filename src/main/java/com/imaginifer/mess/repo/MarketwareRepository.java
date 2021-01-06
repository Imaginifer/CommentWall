/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Marketware;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public interface MarketwareRepository extends JpaRepository<Long, Marketware>{
    
    public void newMarketware(Marketware m);
    public void deleteMarketware(Marketware m);
    public Marketware findFirstMarketwareByMarketwareId(long marketwareId);
    public List<Marketware> getAllMarketwares();
}
