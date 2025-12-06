package com.astrobookings.sales.infrastructure.persistence.mappers;

import com.astrobookings.sales.domain.models.booking.SalesBooking;
import com.astrobookings.sales.domain.models.booking.SalesBookingPrice;
import com.astrobookings.sales.domain.models.flight.SalesFlight;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;
import com.astrobookings.sales.infrastructure.persistence.models.Booking;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class BookingMapper implements DomainMapper<SalesBooking, Booking> {

    private FlightInfoProvider flightInfoProvider;

    @Override
    public SalesBooking toDomain(Booking infrastructureModel) {

        SalesBooking booking = new SalesBooking();
        booking.setId(infrastructureModel.id());
        booking.setPassengerName(infrastructureModel.passengerName());
        booking.setPaymentTransactionId(infrastructureModel.paymentTransactionId());

        SalesFlight flight = flightInfoProvider.getFlightById(infrastructureModel.flightId());
        booking.setFlight(flight);

        booking.setFinalPrice(new SalesBookingPrice(infrastructureModel.finalPrice()));

        return booking;
    }

    @Override
    public Booking toInfrastructure(SalesBooking domainModel) {
        return new Booking(
                domainModel.getId(),
                domainModel.getFlight().getId(),
                domainModel.getPassengerName(),
                domainModel.getFinalPrice(),
                domainModel.getPaymentTransactionId()
        );
    }
}
