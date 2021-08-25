/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Report;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long>{
    
    //public void newReport(Report report);
    public Report findOneByReportId(long reportId);
    @Query("select r from Report r where r.relevant = true order by r.reportId desc")
    public List<Report> getAllRelevantReports();
    @Query("select r from Report r order by r.reportId desc")
    public List<Report> getAllReports();
}
