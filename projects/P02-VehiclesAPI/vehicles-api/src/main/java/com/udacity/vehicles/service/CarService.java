package com.udacity.vehicles.service;

import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.CarRepository;
import com.udacity.vehicles.client.prices.PriceClient;
import  com.udacity.vehicles.client.maps.MapsClient;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;
    private final PriceClient priceClient;
    private final MapsClient mapsClient;

    public CarService(CarRepository repository, PriceClient priceClient, MapsClient mapsClient) {
        this.repository = repository;
        this.priceClient = priceClient;
        this.mapsClient = mapsClient;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        List<Car> cars = repository.findAll();
        for(Car car: cars){
            String price = priceClient.getPrice(car.getId());
            car.setPrice(price);
            Location location = mapsClient.getAddress(car.getLocation());
            car.setLocation(location);
        }
        return cars;
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        return repository.findById(id)
                .map(car -> {
                    Long vehicleId = car.getId();
                    String price = priceClient.getPrice(vehicleId);
                    car.setPrice(price);
                    Location location = mapsClient.getAddress(car.getLocation());
                    car.setLocation(location);
                    return car;
                }).orElseThrow(CarNotFoundException::new);
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        carToBeUpdated.setCondition(car.getCondition());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        Optional<Car> optionalCar = repository.findById(id);
        if(optionalCar.isEmpty()) {
            throw new CarNotFoundException();
        }
        repository.deleteById(id);
    }
}
