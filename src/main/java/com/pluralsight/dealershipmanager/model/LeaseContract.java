package com.pluralsight.dealershipmanager.model;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
public class LeaseContract extends Contract {

    private static final BigDecimal ENDING_VALUE_RATE = new BigDecimal("0.50");
    private static final BigDecimal LEASE_FEE_RATE = new BigDecimal("0.07");

    private BigDecimal expectedEndingValue;
    private BigDecimal leaseFee;

    public LeaseContract(LocalDate contractDate, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(contractDate, customerName, customerEmail, vehicleSold);
        this.expectedEndingValue = vehicleSold.getPrice().multiply(ENDING_VALUE_RATE).setScale(2, RoundingMode.HALF_UP);
        this.leaseFee = vehicleSold.getPrice().multiply(LEASE_FEE_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getExpectedEndingValue() {
        return expectedEndingValue;
    }

    public void setExpectedEndingValue(BigDecimal expectedEndingValue) {
        this.expectedEndingValue = expectedEndingValue;
    }

    public BigDecimal getLeaseFee() {
        return leaseFee;
    }

    public void setLeaseFee(BigDecimal leaseFee) {
        this.leaseFee = leaseFee;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return expectedEndingValue.add(leaseFee);
    }

    @Override
    public BigDecimal getMonthlyPayment() {
        return LoanCalculator.calculateMonthlyPayment(getTotalPrice(), new BigDecimal("4.0"), 36);
    }
}
