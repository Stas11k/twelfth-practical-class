package ua.edu.ukma;

public record Payment (String id, String email, PaymentStatus status, long amountCents){
}
