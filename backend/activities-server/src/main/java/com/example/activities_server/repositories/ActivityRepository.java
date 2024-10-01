package com.example.activities_server.repositories;

import com.example.activities_server.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findTop5ByUserIdOrderByDateDesc(Long userId);
    List<Activity> findAllByUserIdOrderByDateDesc(Long userId);

    Optional<Activity> findByUserIdAndId(Long userId, Long id);
}
