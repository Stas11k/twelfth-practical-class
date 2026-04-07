package ua.edu.ukma;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PaymentLoader {

    public static List<Payment> load(Path csv) throws IOException {
        return loadWithStats(csv).payments();
    }

    public static LoadResult loadWithStats(Path csv) throws IOException {
        List<Payment> payments = new ArrayList<>();
        int invalidLines = 0;

        try (BufferedReader reader = Files.newBufferedReader(csv, StandardCharsets.UTF_8)) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                if (line.isBlank()) continue;
                Payment payment = parsePayment(line);
                if (payment != null) {
                    payments.add(payment);
                } else {
                    invalidLines++;
                }
            }
        }
        return new LoadResult(payments, invalidLines);
    }

    private static Payment parsePayment(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 4) return null;
        String id = parts[0].trim();
        String email = parts[1].trim();
        String statusRaw = parts[2].trim();
        String amountRaw = parts[3].trim();
        if (id.isEmpty() || email.isEmpty() || statusRaw.isEmpty() || amountRaw.isEmpty()) return null;
        PaymentStatus status;
        try {
            status = PaymentStatus.valueOf(statusRaw);
        } catch (IllegalArgumentException e) {
            return null;
        }
        long amountCents;
        try {
            amountCents = Long.parseLong(amountRaw);
        } catch (NumberFormatException e) {
            return null;
        }
        if (amountCents < 0) return null;
        return new Payment(id, email, status, amountCents);
    }

    public record LoadResult(List<Payment> payments, int invalidLines) {
    }
}