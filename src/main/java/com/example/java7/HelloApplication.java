package com.example.java7;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Parking Simulation");

        VBox root = new VBox(10);

        TextField threadsCountField = new TextField();
        threadsCountField.setPromptText("Enter number of cars");
        Button startButton = new Button("Start Parking");

        root.getChildren().addAll(threadsCountField, startButton);

        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);

        List<Car> cars = new ArrayList<>();

        startButton.setOnAction(e -> {
            int numberOfCars = Integer.parseInt(threadsCountField.getText());
            createCarThreads(root, cars, numberOfCars);
            startCarThreadsSimultaneously(cars);
        });

        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });

        primaryStage.show();
    }

    private void createCarThreads(VBox root, List<Car> cars, int numberOfCars) {
        root.getChildren().removeAll();

        for (int i = 1; i <= numberOfCars; i++) {
            Car car = new Car("Car " + i);
            cars.add(car);
        }

        root.getChildren().addAll(cars);
    }

    private void startCarThreadsSimultaneously(List<Car> cars) {
        ParkingLot parkingLot = new ParkingLot(10);

        CountDownLatch latch = new CountDownLatch(cars.size());

        for (Car car : cars) {
            car.setParkingLot(parkingLot);

            Thread thread = new Thread(() -> {
                try {
                    latch.await();

                    car.startParking();

                    car.leaveParking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            car.setThread(thread);

            thread.setPriority((int) (Math.random() * Thread.MAX_PRIORITY));
            int priority = thread.getPriority();
            car.updatePriority(priority);
            thread.start();

            latch.countDown();
        }
    }
}
