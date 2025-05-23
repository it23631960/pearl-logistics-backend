package com.example.pearllogistics.ItemManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findById(Long id);

    @Query("SELECT new com.example.pearllogistics.ItemManagement.ItemCategoryDTO(i.category) FROM Item i")
    List<ItemCategoryDTO> findAllCategories();
}
