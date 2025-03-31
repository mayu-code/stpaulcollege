package com.main.stpaul.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.entities.Stream;
import com.main.stpaul.repository.StreamRepo;
import com.main.stpaul.services.serviceInterface.StreamService;

@Service
public class StreamServiceImpl implements StreamService{

    @Autowired
    private StreamRepo streamRepo;

    @Override
    public Stream addStream(Stream stream) {
        return this.streamRepo.save(stream);
    }
    
}
