package com.example.lostfound.repository;

import com.example.lostfound.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOrderByCreatedAtDesc();

    @Query("select i from Item i join fetch i.owner where i.id = :id")
    Optional<Item> findWithOwnerById(@Param("id") Long id);

    @Query("""
            select i from Item i
            where lower(i.title) like lower(concat('%', :keyword, '%'))
               or lower(i.description) like lower(concat('%', :keyword, '%'))
               or lower(i.location) like lower(concat('%', :keyword, '%'))
            order by i.createdAt desc
            """)
    List<Item> search(@Param("keyword") String keyword);
}
