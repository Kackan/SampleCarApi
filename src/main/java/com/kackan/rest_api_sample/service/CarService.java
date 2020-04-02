package com.kackan.rest_api_sample.service;

import com.kackan.rest_api_sample.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CarService {

     List<Car> getCars();
     Optional<Car> getCarById(Long id);
     List<Car> getCarsByColor(String color);
     Optional<Car> addCar(Car car);
     Optional<Car> updateCar(Car car);
     Optional<Car> updateOneFieldOfCar(Long id, Map<String,Object> names);
     Optional<Car>removeCarById(Long id);
}
