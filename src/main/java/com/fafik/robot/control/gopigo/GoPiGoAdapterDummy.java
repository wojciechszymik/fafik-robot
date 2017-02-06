package com.fafik.robot.control.gopigo;

import com.fafik.robot.control.protocol.CommandName;

/**
 * Created by Wojciech on 15.01.2017.
 */
public class GoPiGoAdapterDummy implements GoPiGoAdapter {

    private CommandName state;

    @Override
    public void forward(){
        state = CommandName.FORWARD;
    }

    @Override
    public void back(){
        state = CommandName.BACK;
    }

    @Override
    public void stop(){
        state = CommandName.STOP_ALL;
    }

    @Override
    public void left(){
        state = CommandName.LEFT;
    }

    @Override
    public void right(){
        state = CommandName.RIGHT;
    }

    public CommandName getState() {
        return state;
    }
}
