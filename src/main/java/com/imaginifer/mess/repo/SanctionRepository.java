/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Sanction;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public interface SanctionRepository extends JpaRepository<Sanction, Long>{
    
    public Sanction findFirstSanctionBySanctionId(long sanctionId);
    @Query("select s from Sanction s where s.valid = true order by s.sanctionId")
    public List<Sanction> getAllSanctions();
}
