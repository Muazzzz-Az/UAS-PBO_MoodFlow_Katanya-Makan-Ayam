package com.kelompok.moodflow.repository;

import com.kelompok.moodflow.model.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MoodRepository extends JpaRepository<MoodEntry, Long> {
    List<MoodEntry> findByUserIdOrderByTimestampDesc(Long userId);
}