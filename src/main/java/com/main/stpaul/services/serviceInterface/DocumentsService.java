package com.main.stpaul.services.serviceInterface;

import java.util.List;

import com.main.stpaul.entities.Documents;

public interface DocumentsService {
    Documents addDocuments(Documents documents);
    List<Documents> getStudentDocuments(String studentid);
}
