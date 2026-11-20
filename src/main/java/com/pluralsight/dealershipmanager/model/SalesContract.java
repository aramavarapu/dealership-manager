package com.pluralsight.dealershipmanager.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.math.RoundingMode;
public class SalesContract extends Contract {

    private static final BigDecimal SALES_TAX_RATE = new BigDecimal("0.05");
    private static final BigDecimal DEFAULT_RECORDING_FEE = new BigDecimal("100.00");
    private static final BigDecimal LOW_PROCESSING_FEE = new BigDecimal("295.00");
    private static final BigDecimal HIGH_PROCESSING_FEE = new BigDecimal("495.00");
    private static final BigDecimal PRICE_CUTOFF = new BigDecimal("10000");

    private BigDecimal salesTaxAmount;
    private BigDecimal recordingFee;
    private boolean financed;

    public SalesContract(LocalDate contractDate, String customerName, String customerEmail, Vehicle vehicleSold, boolean financed) {
        super(contractDate, customerName, customerEmail, vehicleSold);
        this.financed = financed;
        //Rounds BigDecimal
        this.salesTaxAmount = vehicleSold.getPrice().multiply(SALES_TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        this.recordingFee = DEFAULT_RECORDING_FEE;
    }

    public BigDecimal getSalesTaxAmount() {
        return salesTaxAmount;
    }

    public void setSalesTaxAmount(BigDecimal salesTaxAmount) {
        this.salesTaxAmount = salesTaxAmount;
    }

    public BigDecimal getRecordingFee() {
        return recordingFee;
    }

    public void setRecordingFee(BigDecimal recordingFee) {
        this.recordingFee = recordingFee;
    }

    public boolean isFinanced() {
        return financed;
    }

    public void setFinanced(boolean financed) {
        this.financed = financed;
    }

    public BigDecimal getProcessingFee() {
        if (getVehicleSold().getPrice().compareTo(PRICE_CUTOFF) < 0) {
            return LOW_PROCESSING_FEE;
        }
        return  HIGH_PROCESSING_FEE;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return getVehicleSold().getPrice().add(salesTaxAmount).add(recordingFee).add(getProcessingFee());
    }

    @Override
    public BigDecimal getMonthlyPayment() {
        if (!financed) {
            return BigDecimal.ZERO.setScale(2);
        }
        if (getVehicleSold().getPrice().compareTo(PRICE_CUTOFF) >= 0) {
            return LoanCalculator.calculateMonthlyPayment(getTotalPrice(), new BigDecimal("4.25"), 48);
        }
        return LoanCalculator.calculateMonthlyPayment(getTotalPrice(), new BigDecimal("5.25"), 24);
    }
}
