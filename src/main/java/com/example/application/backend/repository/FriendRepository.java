package com.example.application.backend.repository;

import com.example.application.backend.entity.Friend;
import com.example.application.backend.entity.GiftList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import java.util.List;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUserId(Long userId);

    List<Friend> findByUserIdAndNicknameContainingIgnoreCase(Long userId, String nickname);
}
