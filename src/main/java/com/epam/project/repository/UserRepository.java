package com.epam.project.repository;

import com.epam.project.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Интерфейс взаимодействия с БД
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //Главным индентификатором будет email Optional либо вернёт null либо обьект
    Optional<UserEntity> findByEmail(String email);
}