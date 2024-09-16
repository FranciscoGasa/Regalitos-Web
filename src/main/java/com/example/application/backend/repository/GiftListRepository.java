package com.example.application.backend.repository;

import com.example.application.backend.entity.GiftList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GiftListRepository extends JpaRepository<GiftList, Long> {
    Optional<GiftList> findById(Long listId);

    List<GiftList> findByUserId(Long userId);

    List<GiftList> findByUserIdAndNameContainingIgnoreCase(Long userId, String name);

}

