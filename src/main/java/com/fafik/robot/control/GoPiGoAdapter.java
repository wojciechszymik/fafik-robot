package com.fafik.robot.control;

import java.io.IOException;

/**
 * Created by Wojciech on 15.01.2017.
 */
public interface GoPiGoAdapter {

    void forward()throws IOException;

    void back()throws IOException;

    void stop()throws IOException;

    void left() throws IOException;

    void right() throws IOException;
}
