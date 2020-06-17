package com.timurisachenko.gamification.repository;

import com.timurisachenko.gamification.domain.BadgeCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeCardRepository extends CrudRepository<BadgeCard, Long> {
    List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(final Long userId);
}
