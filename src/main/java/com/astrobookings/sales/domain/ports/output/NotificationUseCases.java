package com.astrobookings.sales.domain.ports.output;

import com.astrobookings.sales.domain.models.Booking;

import java.util.List;

public interface NotificationUseCases {
    static void notifyConfirmation(String flightId, int passengerCount) {
        System.out.println(
                "[NOTIFICATION SERVICE] Flight " + flightId + " CONFIRMED - Notifying " + passengerCount + " passenger(s)");
    }

    static void notifyCancellation(String flightId, int passengerCount) {
        System.out.println(
                "[NOTIFICATION SERVICE] Flight " + flightId + " CANCELLED - Notifying " + passengerCount + " passenger(s)");
    }

    static void notifyCancellation(String flightId, List<Booking> bookings) {
        System.out.println(
                "[NOTIFICATION SERVICE] Flight " + flightId + " CANCELLED - Notifying " + bookings.size() + " passenger(s)");
        for (Booking booking : bookings) {
            System.out.println("[NOTIFICATION SERVICE] Sending cancellation email to " + booking.getPassengerName()
                    + " (Booking: " + booking.getId() + ", Refund: $" + booking.getFinalPrice() + ")");
        }
    }
}
