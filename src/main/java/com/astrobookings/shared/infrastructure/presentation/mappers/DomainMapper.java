package com.astrobookings.shared.infrastructure.presentation.mappers;

public interface DomainMapper<D, E> {

    D toDomain(E infrastructureModel);

    E toInfrastructure(D domainModel);

}
