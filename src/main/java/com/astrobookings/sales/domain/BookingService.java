package com.astrobookings.sales.domain;

import com.astrobookings.sales.domain.models.booking.SalesBooking;
import com.astrobookings.sales.domain.models.booking.SalesBookingPrice;
import com.astrobookings.sales.domain.models.flight.SalesFlight;
import com.astrobookings.sales.domain.ports.input.BookingUseCases;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;
import com.astrobookings.sales.domain.ports.output.NotificationUseCases;

import java.util.List;

public class BookingService implements BookingUseCases {
    private final FlightInfoProvider flightInfoProvider;
    private final BookingRepositoryPort bookingRepositoryPort;

    public BookingService(FlightInfoProvider flightInfoProvider, BookingRepositoryPort brp) {
        this.flightInfoProvider = flightInfoProvider;
        this.bookingRepositoryPort = brp;
    }

    @Override
    public SalesBooking createBooking(String flightId, String passengerName) throws Exception {

        SalesFlight salesFlight = flightInfoProvider.getFlightById(flightId);

        if (salesFlight == null) {
            throw new IllegalArgumentException("Flight not found");
        }

        if (!salesFlight.isAvailableForBooking()) {
            throw new IllegalArgumentException("Flight is not available for booking");
        }

        // Final price
        double finalPrice = salesFlight.getFinalPrice();

        // Process payment
        String transactionId = PaymentGateway.processPayment(finalPrice);

        // Create booking
        SalesBooking booking = new SalesBooking();
        booking.setFlight(salesFlight);
        booking.setPassengerName(passengerName);
        booking.setFinalPrice(new SalesBookingPrice(finalPrice));
        booking.setPaymentTransactionId(transactionId);

        SalesBooking savedBooking = bookingRepositoryPort.save(booking);

        salesFlight.setCurrentPassengers(salesFlight.getCurrentPassengers() + 1);

        if (salesFlight.canBeMarkedAsSoldOut()) {
            flightInfoProvider.markFlightAsSoldOut(flightId);
        } else if (salesFlight.canBeConfirmed()) {
            flightInfoProvider.markFlightAsConfirmed(flightId);
            NotificationUseCases.notifyConfirmation(flightId, salesFlight.getCurrentPassengers());
        }

        return savedBooking;
    }

    @Override
    public List<SalesBooking> getBookings(String flightId, String passengerName) {
        List<SalesBooking> bookings;
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

        return bookings;
    }

}