package com.main.stpaul.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.entities.Documents;
import com.main.stpaul.repository.DocumentRepo;
import com.main.stpaul.services.serviceInterface.DocumentsService;

@Service
public class DocumentServiceImpl implements DocumentsService{

    @Autowired
    private DocumentRepo documentRepo;

    @Override
    public Documents addDocuments(Documents documents) {
        return this.documentRepo.save(documents);
    }

    @Override
    public List<Documents> getStudentDocuments(String studentid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudentDocuments'");
    }
    
}
