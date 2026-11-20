package com.pluralsight.dealershipmanager.data;
import com.pluralsight.dealershipmanager.model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

public class ContractDataManager {

    private static final String FILE_NAME = "contracts.csv";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public void saveContract(Contract contract) {
        String type;
        String contractFields;

        if (contract instanceof  SalesContract) {
            SalesContract sale = (SalesContract) contract;
            type = "SALE";
            contractFields = money(sale.getSalesTaxAmount()) + "|" + money(sale.getRecordingFee()) + "|" + money(sale.getProcessingFee()) + "|" + money(sale.getTotalPrice()) + "|" + (sale.isFinanced() ? "YES" : "NO") + "|" + money(sale.getMonthlyPayment());
        } else if (contract instanceof  LeaseContract) {
            LeaseContract lease = (LeaseContract) contract;
            type = "LEASE";
            contractFields = money(lease.getExpectedEndingValue()) + "|" + money(lease.getLeaseFee()) + "|" + money(lease.getTotalPrice()) + "|" + money(lease.getMonthlyPayment());
        } else {
            System.out.println("Unknown contract type, nothing was saved.");
            return;
        }

        Vehicle vehicle = contract.getVehicleSold();

        String line = type + "|" + contract.getContractDate().format(DATE_FORMAT) + "|" + contract.getCustomerName() + "|" + contract.getCustomerEmail() + "|" + vehicle.getVin() + "|" + vehicle.getYear() + "|" + vehicle.getMake() + "|" + vehicle.getModel() + "|" + vehicle.getVehicleType() + "|" + vehicle.getColor() + "|" + vehicle.getOdometer() + "|" + money(vehicle.getPrice()) + "|" + contractFields;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Could not save " + FILE_NAME + ": " + e.getMessage());
        }
    }

    private String money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
