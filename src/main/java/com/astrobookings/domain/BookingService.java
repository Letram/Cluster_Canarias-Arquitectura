package com.astrobookings.domain;

import com.astrobookings.domain.dtos.BookingDto;
import com.astrobookings.domain.ports.BookingRepositoryPort;
import com.astrobookings.domain.ports.FlightRepositoryPort;
import com.astrobookings.domain.ports.RocketRepositoryPort;
import com.astrobookings.domain.models.Booking;
import com.astrobookings.domain.models.Flight;
import com.astrobookings.domain.models.FlightStatus;
import com.astrobookings.domain.models.Rocket;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingService {
    private final BookingRepositoryPort bookingRepositoryPort;
    private final FlightRepositoryPort flightRepositoryPort;
    private final RocketRepositoryPort rocketRepositoryPort;

    public BookingService(BookingRepositoryPort bookingRepositoryPort, FlightRepositoryPort flightRepositoryPort, RocketRepositoryPort rocketRepositoryPort) {
        this.bookingRepositoryPort = bookingRepositoryPort;
        this.flightRepositoryPort = flightRepositoryPort;
        this.rocketRepositoryPort = rocketRepositoryPort;
    }

    public BookingDto createBooking(String flightId, String passengerName) throws Exception {

        // Find flight
        Flight flight = flightRepositoryPort.findAll().stream()
                .filter(f -> f.getId().equals(flightId))
                .findFirst()
                .orElse(null);
        if (flight == null) {
            throw new IllegalArgumentException("Flight not found");
        }

        // Check flight status
        if (flight.getStatus() == FlightStatus.CANCELLED || flight.getStatus() == FlightStatus.SOLD_OUT) {
            throw new IllegalArgumentException("Flight is not available for booking");
        }

        // Get rocket capacity
        Rocket rocket = rocketRepositoryPort.findAll().stream()
                .filter(r -> r.getId().equals(flight.getRocketId()))
                .findFirst()
                .orElse(null);
        if (rocket == null) {
            throw new IllegalArgumentException("Rocket not found");
        }
        int capacity = rocket.getCapacity();

        // Count current bookings
        List<Booking> existingBookings = bookingRepositoryPort.findByFlightId(flightId);
        int currentBookings = existingBookings.size();

        if (currentBookings >= capacity) {
            throw new IllegalArgumentException("Flight is sold out");
        }

        // Calculate discount
        double discount = calculateDiscount(flight, currentBookings, capacity);

        // Final price
        double finalPrice = flight.getBasePrice() * (1 - discount);

        // Process payment
        String transactionId = PaymentGateway.processPayment(finalPrice);

        // Create booking
        Booking booking = new Booking();
        booking.setFlightId(flightId);
        booking.setPassengerName(passengerName);
        booking.setFinalPrice(finalPrice);
        booking.setPaymentTransactionId(transactionId);
        Booking savedBooking = bookingRepositoryPort.save(booking);

        // Update flight status
        currentBookings++;
        if (currentBookings >= capacity) {
            flight.setStatus(FlightStatus.SOLD_OUT);
        } else if (currentBookings >= flight.getMinPassengers() && flight.getStatus() == FlightStatus.SCHEDULED) {
            flight.setStatus(FlightStatus.CONFIRMED);
            NotificationService.notifyConfirmation(flightId, currentBookings);
        }
        flightRepositoryPort.save(flight);

        // Return JSON (mixing responsibility)
        return bookingToDto(savedBooking);
    }

    private double calculateDiscount(Flight flight, int currentBookings, int capacity) {
        LocalDateTime now = LocalDateTime.now();
        long daysUntilDeparture = ChronoUnit.DAYS.between(now, flight.getDepartureDate());

        // Precedence: only one discount
        if (currentBookings + 1 == capacity) {
            return 0.0; // Last seat, no discount
        } else if (currentBookings + 1 == flight.getMinPassengers()) {
            return 0.3; // One short of min, 30% off
        } else if (daysUntilDeparture > 180) {
            return 0.1; // >6 months, 10% off
        } else if (daysUntilDeparture >= 7 && daysUntilDeparture <= 30) {
            return 0.2; // 1 month to 1 week, 20% off
        } else {
            return 0.0; // No discount
        }
    }

    public List<BookingDto> getBookings(String flightId, String passengerName) {
        List<Booking> bookings;
        if (flightId != null && !flightId.isEmpty()) {
            bookings = bookingRepositoryPort.findByFlightId(flightId);
            if (passengerName != null && !passengerName.isEmpty()) {
                bookings = bookings.stream()
                        .filter(b -> b.getPassengerName().equalsIgnoreCase(passengerName))
                        .collect(java.util.stream.Collectors.toList());
            }
        } else if (passengerName != null && !passengerName.isEmpty()) {
            bookings = bookingRepositoryPort.findByPassengerName(passengerName);
        } else {
            bookings = bookingRepositoryPort.findAll();
        }

        return bookings.stream()
                .map(this::bookingToDto)
                .collect(java.util.stream.Collectors.toList());
    }

    private BookingDto bookingToDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setFlightId(booking.getFlightId());
        bookingDto.setPassengerName(booking.getPassengerName());
        bookingDto.setFinalPrice(booking.getFinalPrice());
        bookingDto.setPaymentTransactionId(booking.getPaymentTransactionId());
        return bookingDto;
    }
}