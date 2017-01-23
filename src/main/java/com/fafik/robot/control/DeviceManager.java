package com.fafik.robot.control;

import java.io.IOException;

/**
 * Created by Wojciech on 14.09.2016.
 */
public class DeviceManager {

    private MovingStrategy movingStrategy;

    public DeviceManager(MovingStrategy movingStrategy) {
        this.movingStrategy = movingStrategy;
    }

    public void move(Command command){

        System.out.println(command);
        try {
            movingStrategy.move(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
