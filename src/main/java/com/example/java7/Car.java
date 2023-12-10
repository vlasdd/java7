package com.example.java7;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.Date;

class Car extends VBox {
    private String carName;
    private ParkingLot parkingLot;
    private Label stateLabel;
    private Label priorityLabel;
    private Label timeLabel;
    private Thread thread;
    private boolean threadInterrupted = false;

    public Car(String carName) {
        this.carName = carName;

        stateLabel = new Label("State: ");
        priorityLabel = new Label("Priority: ");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        timeLabel = new Label(time);
        Button stopButton = new Button("Stop Parking");
        stopButton.setOnAction(e -> stopParking());

        this.getChildren().addAll(new Label(carName), stateLabel, priorityLabel, timeLabel, stopButton);
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public void startParking() throws InterruptedException {
        if (parkingLot != null && !threadInterrupted) {
            parkingLot.parkCar(carName);
            Platform.runLater(() -> {
                if(!threadInterrupted) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String time = sdf.format(new Date());
                    timeLabel.setText(time);
                    updateState("Parking");
                }
            });
        }
    }

    public void leaveParking() {
        if (parkingLot != null && !threadInterrupted) {
            parkingLot.leaveParkingSpot(carName);
            Platform.runLater(() -> {
                if(!threadInterrupted) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String time = sdf.format(new Date());
                    timeLabel.setText(time);
                    updateState("Left parking");
                }
            });
        }
    }

    public void updateState(String newState) {
        Platform.runLater(() -> {
            stateLabel.setText("State: " + newState);
        });
    }

    public void updatePriority(int priority) {
        if (parkingLot != null) {
            Platform.runLater(() -> {
                priorityLabel.setText("Priority: " + priority);
            });
        }
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void stopParking() {
        threadInterrupted = true;
        thread.interrupt();
        Platform.runLater(() -> {
            stateLabel.setText("Stopped");
        });
    }
}
