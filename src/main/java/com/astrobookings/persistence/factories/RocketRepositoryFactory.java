package com.astrobookings.persistence.factories;

import com.astrobookings.persistence.InMemoryRocketRepository;
import com.astrobookings.persistence.interfaces.RocketRepository;

public class RocketRepositoryFactory {

    public static RocketRepository getRocketRepository() {
        return new InMemoryRocketRepository();
    }

}
