package com.epam.project.repository;


import com.epam.project.model.CallMe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<CallMe, Integer> {}