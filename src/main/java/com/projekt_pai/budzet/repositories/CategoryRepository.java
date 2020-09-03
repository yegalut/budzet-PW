package com.projekt_pai.budzet.repositories;

import com.projekt_pai.budzet.entities.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category,Integer> {
List<Category> findAllByNameContains(@Param("name")String name);
List<Category> findByType(@Param("type") String type);
List<Category> findByName(@Param("name") String name);
}
