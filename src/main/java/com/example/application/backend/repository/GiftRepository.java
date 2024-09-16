package com.example.application.backend.repository;

import com.example.application.backend.entity.Gift;
import com.example.application.backend.entity.GiftList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Long> {

    Optional<Gift> findById(Long listId);

   List<Gift> findByGiftListIdAndNameContainingIgnoreCase(Long userId, String name);

    List<Gift> findByGiftListId(Long giftListId);


    List<Gift> findByFriendId(Long userId);

    List<Gift> findByFriendUserIdAndNameContainingIgnoreCase(Long userId, String name);

    List<Gift> findByNameContainingIgnoreCase(String name);

}
