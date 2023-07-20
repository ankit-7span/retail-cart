package com.retail.store.service.impl;

import com.retail.store.modal.Bill;
import com.retail.store.modal.NetAmount;
import com.retail.store.service.DiscountService;
import org.springframework.stereotype.Service;

/**
 * Implementation of the DiscountService interface that calculates discounts and net payable amount for a bill.
 */
@Service
public class DiscountServiceImpl implements DiscountService {

    @Override
    public NetAmount calculateNetPayableAmount(final Bill bill) {
        try {
            double discount = 0;
            if (!bill.isGroceries()) {
                discount = calculatePercentageBasedDiscount(bill);
                if (discount == 0) {
                    discount = calculateYearsBasedDiscount(bill);
                }
            }
            discount = calculateDiscountOnTotalAmount(bill, discount);
            final var netPayableAmount = bill.amount() - discount;
            return new NetAmount(Math.max(netPayableAmount, 0));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculates the additional discount based on the total bill amount.
     *
     * @param bill     The Bill object representing the user's purchase details.
     * @param discount The current discount amount calculated from other factors.
     * @return The updated discount amount with additional discount based on the total bill amount.
     */
    private static double calculateDiscountOnTotalAmount(final Bill bill, double discount) {
        if (bill.amount() > 100) {
            discount += (bill.amount() / 100) * 5;
        }
        return discount;
    }

    /**
     * Calculate the percentage-based discount for the given bill based on the user's role.
     *
     * @param bill The Bill object representing the user's purchase details.
     * @return The percentage-based discount amount.
     */
    private double calculatePercentageBasedDiscount(final Bill bill) {
        final var totalAmount = bill.amount();
        double discount = 0;
        switch (bill.user().roleEnum()) {
            case EMPLOYEE -> discount = totalAmount * 0.3;
            case AFFILIATE -> discount = totalAmount * 0.1;
        }
        return discount;
    }

    /**
     * Calculate the years-based discount for the given bill based on the customer's duration.
     *
     * @param bill The Bill object representing the user's purchase details.
     * @return The years-based discount amount.
     */
    private double calculateYearsBasedDiscount(final Bill bill) {
        final var totalAmount = bill.amount();
        double discount = 0;
        // Check user's customer duration and apply the discount if applicable
        if (bill.user().isCustomerOverTwoYears()) {
            discount = totalAmount * 0.05;
        }
        return discount;
    }
}
