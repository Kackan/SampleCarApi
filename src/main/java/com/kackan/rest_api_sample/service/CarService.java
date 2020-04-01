package com.kackan.rest_api_sample.service;

import com.kackan.rest_api_sample.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CarService {

    public Optional<List<Car>> getCars();
    public Optional<Car> getCarById(Long id);
    public Optional<List<Car>> getCarsByColor(String color);
    public Optional<Car> addCar(Car car);
    public Optional<Car> updateCar(Car car);
    public Optional<Car> updateOneFieldOfCar(Long id, Map<String,Object> names);
    public Optional<Car>removeCarById(Long id);
}
