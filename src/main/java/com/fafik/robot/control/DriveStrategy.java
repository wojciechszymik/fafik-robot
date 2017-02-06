package com.fafik.robot.control;

import com.fafik.robot.control.protocol.Command;

import java.io.IOException;

/**
 * Created by Wojciech on 18.01.2017.
 */
public interface DriveStrategy {
    void move(Command command) throws IOException;

}
