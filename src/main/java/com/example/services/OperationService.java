package com.example.services;

import com.example.models.OperationEntity;
import com.example.repositories.OperationRespository;
import org.springframework.stereotype.Service;

@Service
public class OperationService {
    private OperationRespository operationRespository;

    public OperationService(OperationRespository operationRespository) {
        this.operationRespository = operationRespository;
    }

    public void create(OperationEntity operation) {
        operationRespository.save(operation);
    }
}
