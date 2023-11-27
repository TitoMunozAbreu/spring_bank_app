package com.example.repositories;

import com.example.models.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRespository extends JpaRepository<OperationEntity, Integer> {
}
