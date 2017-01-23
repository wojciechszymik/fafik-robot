package com.fafik.robot.control;

import java.io.IOException;

/**
 * Created by Wojciech on 18.01.2017.
 */
public interface MovingStrategy {
    void move(Command command) throws IOException;

    boolean isTurning();

    boolean isDriving();
}
