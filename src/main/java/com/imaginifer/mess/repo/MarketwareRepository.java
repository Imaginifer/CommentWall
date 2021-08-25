/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Marketware;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public interface MarketwareRepository extends JpaRepository<Marketware, Long>{
    
    public Marketware findFirstMarketwareByMarketwareId(long marketwareId);
    @Query("select m from Marketware m order by m.marketwareId asc")
    public List<Marketware> getAllMarketwares();
}
