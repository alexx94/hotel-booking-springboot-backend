package com.example.demo.service;

import com.example.demo.dto.PricingRuleDto;
import com.example.demo.model.PricingRule;
import com.example.demo.repository.PricingRuleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PricingRuleService {
    private final PricingRuleRepository pricingRuleRepository;

    public PricingRuleService(PricingRuleRepository pricingRulesRepository) {
        this.pricingRuleRepository = pricingRulesRepository;
    }

    public List<PricingRule> allPricingRules() {
        List<PricingRule> pricingRules = new ArrayList<>();
        pricingRuleRepository.findAll().forEach(pricingRules::add);
        return pricingRules;
    }

    public List<PricingRule> getPricingRulesByRoomId(Long roomId) {
        List<PricingRule> pricingRules = new ArrayList<>();
        pricingRuleRepository.findByRoomId(roomId).forEach(pricingRules::add);
        return  pricingRules;
    }

    public PricingRule createPricingRule(PricingRuleDto input) {
        PricingRule pricingRule = new PricingRule(input.getRoom_id(), input.getStart_date(), input.getEnd_date(), input.getSpecial_price());
        return pricingRuleRepository.save(pricingRule);
    }

    public PricingRule updatePricingRule(Long id, PricingRuleDto input) {
        PricingRule pricingRule = new PricingRule(input.getRoom_id(), input.getStart_date(), input.getEnd_date(), input.getSpecial_price());
        pricingRule.setId(id);
        return pricingRuleRepository.save(pricingRule);
    }

    public void deletePricingRule(Long id) {
        pricingRuleRepository.deleteById(id);
    }

}
