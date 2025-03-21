package com.main.stpaul.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.dto.response.DocumentReponse;
import com.main.stpaul.entities.Documents;

public interface DocumentRepo extends JpaRepository<Documents,Long>{
    

    @Query("""
            SELECT new com.main.stpaul.dto.response.DocumentReponse( d.documentId,d.documentType,d.document)
            FROM Documents d
            WHERE d.student.id=:id AND d.isDelete=false
            """)
    List<DocumentReponse> findAlldocuments(@Param("id")String id);

}
