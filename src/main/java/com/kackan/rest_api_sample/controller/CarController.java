package com.kackan.rest_api_sample.controller;

import com.kackan.rest_api_sample.model.Car;
import com.kackan.rest_api_sample.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<Car>> getCars() {
            List<Car>cars=carService.getCars();
            cars.forEach(car -> car.addIf(!car.hasLinks(),()->linkTo(CarController.class).slash(car.getId()).withSelfRel()));
            Link link = linkTo(CarController.class).withSelfRel();
            CollectionModel<Car> carsCollection = new CollectionModel<>(cars, link);
            return new ResponseEntity<>(carsCollection, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<Car>> getCarById(@PathVariable Long id) {
        Optional<Car> carById = carService.getCarById(id);
        if (carById.isPresent()) {
            Car car=carById.get();
            Link link = linkTo(CarController.class).slash(id).withSelfRel();
            car.addIf(!car.hasLinks(), () -> link);

            EntityModel<Car> entityCar = new EntityModel<>(car);
            return new ResponseEntity<>(entityCar, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/color", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CollectionModel<Car>> getCarsByColor(@RequestParam(required = false) String color) {
            List<Car> cars = carService.getCarsByColor(color);
            cars.forEach(car -> car.addIf(!car.hasLinks(),()->linkTo(CarController.class).slash(car.getId()).withSelfRel()));
            Link link = linkTo(CarController.class).withSelfRel();
            CollectionModel<Car> carsCollection = new CollectionModel<>(cars, link);
            return new ResponseEntity<>(carsCollection, HttpStatus.OK);

    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<Car>> addCar(@RequestBody Car car) {
            Link link = linkTo(CarController.class).slash(car.getId()).withSelfRel();
            EntityModel<Car> carEntity = new EntityModel<>(car, link);
            return carService.addCar(car).map(c->new ResponseEntity<>(carEntity,HttpStatus.OK)).orElseGet(() ->new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }


    @PutMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EntityModel<Car>> updateCar(@RequestBody Car car) {
        Optional<Car> carOp = carService.getCarById(car.getId());
        if (carOp.isPresent()) {
            Link link = linkTo(CarController.class).slash(car.getId()).withSelfRel();
            EntityModel<Car> carEntity = new EntityModel<>(car, link);
            carService.updateCar(car);
            return new ResponseEntity<>(carEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<Car>> updateOneFieldOfCar(@PathVariable Long id, @RequestBody Map<String, Object> names) {
        Optional<Car> carOp = carService.getCarById(id);
        if (carOp.isPresent()) {
            carService.updateOneFieldOfCar(id, names);
            Car updated=carService.getCarById(id).get();
            Link link = linkTo(CarController.class).slash(updated.getId()).withSelfRel();
            EntityModel<Car> carEntity = new EntityModel<>(updated, link);
            return new ResponseEntity<>(carEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

        @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
        public ResponseEntity<EntityModel<Car>> deleteCar (@PathVariable Long id){
            Optional<Car> car = carService.getCarById(id);
            if (car.isPresent()) {
                Link link = linkTo(CarController.class).slash(car.get().getId()).withSelfRel();
                EntityModel<Car> carEntity = new EntityModel<>(car.get(), link);
                carService.removeCarById(id);
                return new ResponseEntity<>(carEntity, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
}
