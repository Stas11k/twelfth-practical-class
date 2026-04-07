package ua.edu.ukma;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("src", "main", "resources", "payments.csv");
        try {
            PaymentLoader.LoadResult result = PaymentLoader.loadWithStats(path);
            System.out.println("Valid payments: " + result.payments().size());
            System.out.println("Invalid lines: " + result.invalidLines());
            for (Payment payment : result.payments()) {
                System.out.println(payment);
            }
        } catch (IOException e) {
            System.out.println("File read error: " + e.getMessage());
        }


        Path reportPath = Path.of("report.txt");
        try {
            PaymentLoader.LoadResult result = PaymentLoader.loadWithStats(path);
            System.out.println("Valid payments: " + result.payments().size());
            System.out.println("Invalid lines: " + result.invalidLines());
            PaymentReportWriter writer = new PaymentReportWriter(result.invalidLines());
            writer.writeReport(reportPath, result.payments());
        } catch (IOException e) {
            System.out.println("File read/write error: " + e.getMessage());
        }

        Path inbox = Path.of("src", "main", "java", "ua", "edu", "ukma", "practical-data", "inbox");
        Path archive = Path.of("src", "main", "java", "ua", "edu", "ukma", "practical-data", "archive");
        try {
            InboxArchiver.archiveTmpFiles(inbox, archive);
            System.out.println("Archiving done.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }


        Path base = Path.of("data");
        try {
            Path safe = PathSafety.safeResolve(base, "reports/2025.txt");
            System.out.println("Safe path: " + safe);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            Path unsafe = PathSafety.safeResolve(base, "../secret.txt");
            System.out.println("Safe path: " + unsafe);
        } catch (Exception e) {
            System.out.println("Blocked: " + e.getMessage());
        }


        Path statusFile = Path.of("status.bin");
        try {
            StatusFile.createFile(statusFile, 5);
            StatusFile.updateStatus(statusFile, 2, (byte) 1);
            byte value = StatusFile.readStatus(statusFile, 2);
            System.out.println("Byte at index 2: " + value);
        } catch (IOException e) {
            System.out.println("Status file error: " + e.getMessage());
        }
    }
}