package com.example.demo.controller;

import com.example.demo.dto.PricingRuleDto;
import com.example.demo.model.PricingRule;
import com.example.demo.service.PricingRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admin/pricing-rules")
@RestController
public class PricingRuleController {
    private final PricingRuleService pricingRuleService;

    public PricingRuleController(PricingRuleService pricingRuleService) {
        this.pricingRuleService = pricingRuleService;
    }

    // TODO: Get method for pricing within a specific range

    @GetMapping("/")
    public ResponseEntity<List<PricingRule>> getPricingRules() {
        List<PricingRule> pricingRules = pricingRuleService.allPricingRules();
        return ResponseEntity.ok(pricingRules);
    }

    @PostMapping("/")
    public ResponseEntity<PricingRule> createPricingRule(@RequestBody PricingRuleDto input) {
        PricingRule pricingRule = pricingRuleService.createPricingRule(input);
        return ResponseEntity.ok(pricingRule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PricingRule> createPricingRule(@PathVariable Long id, @RequestBody PricingRuleDto input) {
        PricingRule updatedPricingRule = pricingRuleService.updatePricingRule(id, input);
        return ResponseEntity.ok(updatedPricingRule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePricingRule(@PathVariable Long id) {
        pricingRuleService.deletePricingRule(id);
        return ResponseEntity.ok("Pricing Rule deleted successfully.");
    }
}
