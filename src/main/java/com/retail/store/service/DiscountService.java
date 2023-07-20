package com.retail.store.service;

import com.retail.store.modal.Bill;
import com.retail.store.modal.NetAmount;

public interface DiscountService {

    /**
     * Calculates the net payable amount for a given bill after applying appropriate discounts.
     *
     * @return A NetAmount object representing the net payable amount after discounts.
     * @throws RuntimeException if an error occurs during the calculation process.
     */
    NetAmount calculateNetPayableAmount(Bill bill);
}