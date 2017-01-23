package com.fafik.robot.control;

import java.io.IOException;

/**
 * Created by Wojciech on 15.01.2017.
 */
public interface GoPiGoAdapter {

    void forward();

    void back();

    void stop();

    void left();

    void right();
}
