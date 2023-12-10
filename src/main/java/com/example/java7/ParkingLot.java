package com.example.java7;

class ParkingLot {
    private final int totalParkingSpots;
    private int availableParkingSpots;

    public ParkingLot(int totalParkingSpots) {
        this.totalParkingSpots = totalParkingSpots;
        this.availableParkingSpots = totalParkingSpots;
    }

    public synchronized void parkCar(String carName) throws InterruptedException {
        if (availableParkingSpots > 0) {
            System.out.println(carName + " is parking. Available spots: " + --availableParkingSpots);
            simulateTimeSpent();
        } else {
            System.out.println(carName + " is waiting. No available parking spots.");
        }
    }

    public synchronized void leaveParkingSpot(String carName) {
        if (availableParkingSpots < totalParkingSpots) {
            System.out.println(carName + " is leaving. Available spots: " + ++availableParkingSpots);
            simulateTimeSpent();
        } else {
            System.out.println(carName + " is waiting. Parking lot is already empty.");
        }
    }

    private void simulateTimeSpent() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted. Exiting...");
        }
    }
}
