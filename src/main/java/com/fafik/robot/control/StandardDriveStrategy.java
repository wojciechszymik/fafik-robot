package com.fafik.robot.control;

import com.fafik.robot.control.gopigo.GoPiGoAdapter;
import com.fafik.robot.control.protocol.Command;
import com.fafik.robot.control.protocol.CommandName;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Wojciech on 18.01.2017.
 */
public class StandardDriveStrategy implements DriveStrategy {

    private GoPiGoAdapter goPiGoAdapter;

    private Command lastCommand;
    private Optional<Command> suspendedCommand;
    private boolean turning = false;
    private boolean driving = false;

    public StandardDriveStrategy(GoPiGoAdapter goPiGoAdapter) {
        this.goPiGoAdapter = goPiGoAdapter;
    }

    @Override
    public void move(Command command)throws IOException {

        if(command.getName() == CommandName.FORWARD){
            forward(command);
        } else if(command.getName() == CommandName.BACK){
            back(command);
        } else if(command.getName() == CommandName.RIGHT){
            right(command);
        } else if(command.getName() == CommandName.LEFT){
            left(command);
        } else if(command.getName() == CommandName.STOP_ALL){
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
        while(waitStart + command.getDuration() > System.currentTimeMillis()){
            try {
                Thread.sleep(command.getDuration());
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
        System.out.println("going = " + command.getName());
    }

    public boolean isTurning() {
        return turning;
    }

    public boolean isDriving() {
        return driving;
    }
}
