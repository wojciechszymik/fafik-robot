package com.fafik.robot.control;

import java.io.IOException;

/**
 * Created by Wojciech on 18.01.2017.
 */
public class StandardMovingStrategy implements MovingStrategy {

    private GoPiGoAdapter goPiGoAdapter;

    private Command lastCommand;
    private boolean turning = false;

    public StandardMovingStrategy(GoPiGoAdapter goPiGoAdapter) {
        this.goPiGoAdapter = goPiGoAdapter;
    }

    @Override
    public void move(Command command)throws IOException {

    }

    public void forward(Command command)throws IOException {
        if(!isTurning()){
            setLastCommand(command);
            goPiGoAdapter.forward();
        }
    }

    public void back(Command command)throws IOException {
        if (!isTurning()) {
            setLastCommand(command);
            goPiGoAdapter.back();
        }
    }

    public void stop(Command command)throws IOException {
        setLastCommand(command);
        goPiGoAdapter.stop();
    }

    public void right(Command command) throws IOException {
        setLastCommand(command);
        turning = true;
        goPiGoAdapter.right();
        while (command.getTimeStamp() + command.getCommandDuration() > System.currentTimeMillis()){

            try {
                Thread.sleep(command.getCommandDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        turning = false;
        goPiGoAdapter.stop();
    }

    public void left(Command command) throws IOException {
        setLastCommand(command);
        turning = true;
        goPiGoAdapter.left();

        while(command.getTimeStamp() + command.getCommandDuration() > System.currentTimeMillis()){
            try {
                Thread.sleep(command.getCommandDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        turning = false;
        goPiGoAdapter.stop();
    }

    private void setLastCommand(Command command) {
        lastCommand = command;
        System.out.println("going = " + command.getType());
    }

    private boolean isNewCommand(Command command) {
        if(lastCommand != null && !lastCommand.equals(command.getType())) {
            return (lastCommand.getTimeStamp() + lastCommand.getCommandDuration()) < command.getTimeStamp();
        }
        return true;
    }

    private boolean isTurning() {
        return turning;
    }


}
