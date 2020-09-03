package com.projekt_pai.budzet.repositories;

import com.projekt_pai.budzet.Additional.Temp;
import com.projekt_pai.budzet.entities.Finance;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceRepository extends CrudRepository<Finance,Integer> {
    @Query("SELECT * FROM finance WHERE category_id = ?1")
    List<Finance> findAllByUserId(@Param("user")Integer user);

    Finance findFinanceById(@Param("id") Integer id);

    List<Finance> findAllByType(@Param("type") String type);

    List<Finance> findAllByNameContains(@Param("keyword") String keyword);

    List<Finance> findAllByUserIdAndType(@Param("user_id") Integer user_id,
                                                 @Param("type") String type);
    List<Finance> findAllByDateAfter(@Param("date") String date);

    List<Finance> findAllByDateBefore(@Param("date") String date);

    List<Finance> findAllByCategoryId(@Param("categoryId") Integer categoryId);




    List<Finance> findAllByUserIdAndTypeAndDateContains(@Param("user_id") Integer user_id,
                                                        @Param("type") String type,
                                                        @Param("date") String date);

    List<Finance> findAllByUserIdAndTypeAndCategoryId(@Param("user_id") Integer user_id,
                                                      @Param("type") String type,
                                                      @Param("category" ) Integer category);

    List<Finance> findAllByUserIdAndTypeAndCategoryIdAndDateContains(@Param("user_id") Integer user_id,
                                                                     @Param("type") String type,
                                                                     @Param("category" ) Integer category,
                                                                     @Param("date") String date);
}

