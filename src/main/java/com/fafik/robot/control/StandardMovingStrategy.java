package com.fafik.robot.control;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by Wojciech on 18.01.2017.
 */
public class StandardMovingStrategy implements MovingStrategy {

    private GoPiGoAdapter goPiGoAdapter;

    private Command lastCommand;
    private Optional<Command> suspendedCommand;
    private boolean turning = false;
    private boolean driving = false;

    public StandardMovingStrategy(GoPiGoAdapter goPiGoAdapter) {
        this.goPiGoAdapter = goPiGoAdapter;
    }

    @Override
    public void move(Command command)throws IOException {

        if(command.getCommandName() == CommandName.FORWARD){
            forward(command);
        } else if(command.getCommandName() == CommandName.BACK){
            back(command);
        } else if(command.getCommandName() == CommandName.RIGHT){
            right(command);
        } else if(command.getCommandName() == CommandName.LEFT){
            left(command);
        } else if(command.getCommandName() == CommandName.STOP_ALL){
            stop(command);
        }

    }

    public void forward(Command command)throws IOException {
        setLastCommand(command);
        if(command.isStart()){
            goPiGoAdapter.forward();
            driving();
        }
        else {
            stop();
        }
    }

    private void stop() throws IOException {
        goPiGoAdapter.stop();
        drivingStop();
        turningStop();
    }

    private void turningStop() {
        turning = false;
    }

    private void turningStart() {
        turning = true;
    }

    public void back(Command command)throws IOException {
        setLastCommand(command);
        if(command.isStart()){
            goPiGoAdapter.back();
            driving();
        }
        else {
            stop();
        }
    }
    public void stop(Command command)throws IOException {
        setLastCommand(command);
        stop();
    }

    private void driving() {
        driving = true;
        turning = false;
    }

    private void drivingStop() {
        driving = false;
    }

    public void right(Command command) throws IOException {
        turn(command, () -> goPiGoAdapter.right());
    }

    public void turn(Command command, Runnable turn) throws IOException {
        if(command.isStart()){
            if(isDriving()){
                suspendDriving();
                turningStart();
                turn.run();
                wait(command);
                restoreDriving();
            }else {
                turningStart();
                turn.run();
            }
        }else if(!isDriving()){
            stop();
        }
    }

    public void left(Command command) throws IOException {

        turn(command, () -> goPiGoAdapter.left());
    }

    private void wait(Command command) {
        long waitStart = System.currentTimeMillis();
        while(waitStart + command.getCommandDuration() > System.currentTimeMillis()){
            try {
                Thread.sleep(command.getCommandDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void suspendDriving() {
        suspendedCommand = Optional.of(lastCommand);
    }

    private void restoreDriving() throws IOException {
        turningStop();
        move(suspendedCommand.get());
        suspendedCommand = Optional.empty();

    }

    private void setLastCommand(Command command) {
        lastCommand = command;
        System.out.println("going = " + command.getCommandName());
    }

    @Override
    public boolean isTurning() {
        return turning;
    }

    @Override
    public boolean isDriving() {
        return driving;
    }
}
