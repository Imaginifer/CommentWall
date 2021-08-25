/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Pass;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public interface PassRepository extends JpaRepository<Pass, Long>{

    public Pass findOnePassByPassId(long passId);
    @Query("select p from Pass p order by p.passId")
    public List<Pass> getAllPasses();
}
