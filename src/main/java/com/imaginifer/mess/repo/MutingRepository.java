/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Muting;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public interface MutingRepository extends JpaRepository<Long, Muting>{
    
    public void newMuting(Muting muting);
    public void deleteMuting(Muting muting);
    public Muting findOneMutingByMutingId(long mutingId);
    @Query("select m from Muting m where m.whoMutes.commenterId = : i order by m.mutingId desc")
    public List<Muting> findAllMutingsByCommenterId(@Param("i") long commenterId);
}
