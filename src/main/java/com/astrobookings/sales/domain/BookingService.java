package com.astrobookings.sales.domain;

import com.astrobookings.fleet.domain.models.Flight;
import com.astrobookings.fleet.domain.models.FlightStatus;
import com.astrobookings.fleet.domain.models.Rocket;
import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.sales.domain.dtos.BookingDto;
import com.astrobookings.sales.domain.dtos.FlightInfoDto;
import com.astrobookings.sales.domain.models.Booking;
import com.astrobookings.sales.domain.ports.input.BookingUseCases;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;
import com.astrobookings.sales.domain.ports.output.NotificationUseCases;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingService implements BookingUseCases {
    private final FlightInfoProvider flightInfoProvider;
    private final BookingRepositoryPort bookingRepositoryPort;

    public BookingService(FlightInfoProvider flightInfoProvider, BookingRepositoryPort brp) {
        this.flightInfoProvider = flightInfoProvider;
        this.bookingRepositoryPort = brp;
    }

    @Override
    public BookingDto createBooking(String flightId, String passengerName) throws Exception {

        FlightInfoDto flightInfoDto = flightInfoProvider.getFlightById(flightId);

        if (flightInfoDto == null) {
            throw new IllegalArgumentException("Flight not found");
        }

        // Check flight status
        if (flightInfoProvider.isFlightCancelled(flightInfoDto.getStatus())
                || flightInfoProvider.isFlightSoldOut(flightInfoDto.getStatus())) {
            throw new IllegalArgumentException("Flight is not available for booking");
        }

        int capacity = flightInfoDto.getRocketCapacity();

        // Count current bookings
        List<Booking> existingBookings = bookingRepositoryPort.findByFlightId(flightId);
        int currentBookings = existingBookings.size();

        if (currentBookings >= capacity) {
            throw new IllegalArgumentException("Flight is sold out");
        }

        // Calculate discount
        double discount = calculateDiscount(flightInfoDto, currentBookings, capacity);

        // Final price
        double finalPrice = flightInfoDto.getBasePrice() * (1 - discount);

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
            flightInfoProvider.markFlightAsSoldOut(flightId);
        } else if (currentBookings >= flightInfoDto.getMinPassengers() && flightInfoProvider.isFlightScheduled(flightInfoDto.getStatus())) {
            flightInfoProvider.markFlightAsConfirmed(flightId);
            NotificationUseCases.notifyConfirmation(flightId, currentBookings);
        }

        // Return JSON (mixing responsibility)
        return bookingToDto(savedBooking);
    }

    private double calculateDiscount(FlightInfoDto flight, int currentBookings, int capacity) {
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

    @Override
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