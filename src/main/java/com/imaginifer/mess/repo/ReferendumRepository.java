/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Referendum;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public interface ReferendumRepository extends JpaRepository<Referendum, Long>{
    
    public Referendum findFirstReferendumByReferendumId(long referendumId);
    @Query("select r from Referendum r where r.closed = false order by r.creationDate")
    public List<Referendum> getAllOpenReferendums();
    @EntityGraph("loadWithVoters")
    @Query("select r from Referendum r where r.referendumId = :id")
    public Referendum findFirstReferendumByReferendumIdWithVotes(@Param("id")long referendumId);
}
