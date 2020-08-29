package com.projekt_pai.budzet.repositories;

import com.projekt_pai.budzet.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(@Param("email") String email);

    User findUserById(@Param("userId") Integer id);

    List<User> findAllByEmailContaining(@Param("email")String Email);
}

/*
    */

