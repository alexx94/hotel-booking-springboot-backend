package com.example.demo.listener;

import com.example.demo.event.BookingCreationEvent;
import com.example.demo.model.PricingRule;
import com.example.demo.service.PricingRuleService;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(2)
public class BookingPriceRuleListener implements ApplicationListener<BookingCreationEvent> {
    private final PricingRuleService pricingRuleService;

    public BookingPriceRuleListener(PricingRuleService pricingRuleService) {
        this.pricingRuleService = pricingRuleService;
    }

    @Override
    public void onApplicationEvent(BookingCreationEvent event) {
        Long roomId = event.getRoomId();
        LocalDate startDate = event.getStartDate();
        LocalDate endDate = event.getEndDate();
        long nights = event.getNightsCnt();
        int totalPrice = 0;

        List<PricingRule> pricingRules = pricingRuleService.getPricingRulesByRoomId(roomId);
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (PricingRule rule : pricingRules) {
                if (!date.isAfter(rule.getEnd_date()) && !date.isBefore(rule.getStart_date())) {
                    totalPrice += rule.getSpecial_price();
                    nights -= 1;
                }
            }
        }

        event.setNightsCnt(nights);
        event.setTotalPrice(totalPrice);
    }
}
