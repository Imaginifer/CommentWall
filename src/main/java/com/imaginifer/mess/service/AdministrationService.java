/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.dto.TopicView;
import com.imaginifer.mess.entity.*;
import com.imaginifer.mess.enums.SanctionType;
import com.imaginifer.mess.enums.UserRank;
import com.imaginifer.mess.repo.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author imaginifer
 */
@Service
@Transactional(readOnly = true)
public class AdministrationService {
    
    private final WebUtilService wu;
    private final SanctionRepository sr;
    private final MutingRepository mr;
    private final TopicRepository tr;
    private final CustomCommenterRepoImpl cr;
    private final ReferendumRepository rr;
    private final NomineeRepository nr;

    @Autowired
    public AdministrationService(WebUtilService wu, SanctionRepository sr, 
            MutingRepository mr, TopicRepository tr, CustomCommenterRepoImpl cr, 
            ReferendumRepository rr, NomineeRepository nr) {
        this.wu = wu;
        this.sr = sr;
        this.mr = mr;
        this.tr = tr;
        this.cr = cr;
        this.rr = rr;
        this.nr = nr;
    }
    
    @Transactional(readOnly = false)
    public boolean castVote(long nomineeId){
        if(!wu.hasRank(wu.getCurrentUser(), UserRank.ROLE_ELECTOR)){
            throw new AccessDeniedException("Nincs felhatalmazása!");
        }
        Nominee n = nr.findFirstNomineeByNomineeId(nomineeId);
        Referendum r = rr.findFirstReferendumByReferendumIdWithVotes(n.getReferendum().getReferendumId());
        if(!r.isClosed() && r.castVote(wu.getCurrentUser(), nomineeId)){
            n.vote();
            return Boolean.TRUE;
        }
        return false;
    }
    
    public List<TopicView> displayReferendum(long referendumId){
        Referendum r = rr.findFirstReferendumByReferendumIdWithVotes(referendumId);
        return ControllerSupport.convertReferendum(r, true);
    }
    
    @Transactional(readOnly = false)
    public void newSanction(long commenterId, long forumId, SanctionType type, int duration){
        if(wu.hasRank(wu.getCurrentUser(), new UserRank[]{UserRank.ROLE_DIRECTOR, UserRank.ROLE_SUBJECTOR, UserRank.ROLE_ARBITRATOR}) 
                || wu.isSanctionedHere(wu.getCurrentUser(), new SanctionType[]{SanctionType.ADMINISTRATOR,SanctionType.MODERATOR}, forumId)){
            Forum scope = forumId == 0 ? null : tr.getForumById(forumId);
            Sanction s = new Sanction(type, cr.findCommenterById(commenterId), 
                    wu.getCurrentUser(), scope, duration);
            sr.save(s);
        } else {
            throw new AccessDeniedException("Nincs felhatalmazása!");
        }
    }
    
    @PreAuthorize("hasRole('DIRECTOR') OR hasRole('ARBITRATOR')")
    @Transactional(readOnly = false)
    public void liftSanction(long sanctionId){
        sr.findFirstSanctionBySanctionId(sanctionId).setValid(false);
    }
    
    @Transactional(readOnly = false)
    public void muteUser(long id){
        Muting m = new Muting(wu.getCurrentUser(), cr.findCommenterById(id));
        mr.save(m);
    }
    
    @Transactional(readOnly = false)
    public void unmuteUser(long mutingId){
        Muting m = mr.findOneMutingByMutingId(mutingId);
        if(m != null){
            mr.delete(m);
        }
    }
    
    public List<TopicView> listMutings(){
        List<Muting> x = mr.findAllMutingsByCommenterId(wu.getCurrentUser().getCommenterId());
        return ControllerSupport.convertMuting(x);
    }
    
    
}
