package com.example.demo.repository;

import com.example.demo.model.PricingRule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PricingRuleRepository extends CrudRepository<PricingRule, Long> {
    List<PricingRule> findByRoomId(Long room_id);
}
