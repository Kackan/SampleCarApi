package com.kackan.rest_api_sample.service;

import com.kackan.rest_api_sample.model.Car;
import com.kackan.rest_api_sample.model.TypeOfParameter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService{

    List<Car>cars;
    private CarServiceImpl()
    {
        cars=new ArrayList<>();
        cars.add(new Car(1L,"Marka1","Model1","black"));
        cars.add(new Car(2L,"Marka2","Model2","red"));
        cars.add(new Car(3L,"Marka3","Model3","black"));
    }

    @Override
    public List<Car> getCars() {
        return cars;
     }

    @Override
    public Optional<Car> getCarById(Long id) {
        return cars.stream().filter(car -> car.getId().equals(id)).findFirst();
    }

    @Override
    public List<Car> getCarsByColor(String color) {
        return cars.stream().filter(car -> car.getColor().equals(color)).collect(Collectors.toList());
    }

    @Override
    public Optional<Car> addCar(Car car) {
        cars.add(car);
        return Optional.of(car);
    }

    @Override
    public Optional<Car> updateCar(Car car) {
        Optional<Car> old = cars.stream().filter(c->c.getId().equals(car.getId())).findFirst();

        if(old.isPresent())
        {
            cars.remove(old.get());
            cars.add(car);
            return Optional.of(car);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Car> updateOneFieldOfCar(Long id, Map<String,Object> names) {
        Optional<Car> first = cars.stream().filter(c -> c.getId().equals(id)).findFirst();

        if(first.isPresent())
        {
            Car newCar=first.get();

            if(names.containsKey(TypeOfParameter.COLOR.name().toLowerCase()))
            {
                newCar.setColor((String) names.get(TypeOfParameter.COLOR.name().toLowerCase()));
            }

            if(names.containsKey(TypeOfParameter.MODEL.name().toLowerCase()))
            {
                newCar.setModel((String) names.get(TypeOfParameter.MODEL.name().toLowerCase()));
            }

            if(names.containsKey(TypeOfParameter.MARK.name().toLowerCase()))
            {
                newCar.setMark((String) names.get(TypeOfParameter.MARK.name().toLowerCase()));
            }

            return Optional.of(newCar);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Car> removeCarById(Long id) {
        Optional<Car> carToRemove = cars.stream().filter(c -> c.getId().equals(id)).findFirst();
        carToRemove.ifPresent(car -> cars.remove(car));
        return Optional.empty();
    }

}
