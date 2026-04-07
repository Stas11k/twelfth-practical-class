package ua.edu.ukma;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class PaymentReportWriter {
    private final int invalidLines;

    public PaymentReportWriter(int invalidLines) {
        this.invalidLines = invalidLines;
    }

    public void writeReport(Path out, List<Payment> payments) throws IOException {
        long paidTotalCents = 0;
        int newCount = 0;
        int paidCount = 0;
        int failedCount = 0;

        for (Payment payment : payments) {
            if (payment.status() == PaymentStatus.PAID) paidTotalCents += payment.amountCents();
            switch (payment.status()) {
                case NEW -> newCount++;
                case PAID -> paidCount++;
                case FAILED -> failedCount++;
            }
        }

        Path parent = out.getParent();
        if (parent != null && Files.notExists(parent)) Files.createDirectories(parent);
        Path temp;
        if (parent == null) {
            temp = Path.of(out.getFileName().toString() + ".tmp");
        } else {
            temp = parent.resolve(out.getFileName().toString() + ".tmp");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
            writer.write("invalidLines=" + invalidLines);
            writer.newLine();
            writer.write("paidTotalCents=" + paidTotalCents);
            writer.newLine();
            writer.write("NEW=" + newCount + ", PAID=" + paidCount + ", FAILED=" + failedCount);
            writer.newLine();
        }
        try {
            Files.move(temp, out, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, out, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}