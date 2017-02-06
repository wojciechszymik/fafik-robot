package com.fafik.robot.control.actor;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.fafik.robot.control.protocol.Command;
import com.fafik.robot.control.gopigo.GoPiGoAdapter;
import com.fafik.robot.control.DriveStrategy;

import java.io.IOException;
import java.util.Optional;

import static com.fafik.robot.control.protocol.CommandName.*;

/**
 * Created by Wojciech on 18.01.2017.
 */
public class ActorDrive extends AbstractLoggingActor {

    private GoPiGoAdapter goPiGoAdapter;

    private Optional<Command> activeCommand;
    private Optional<Command> suspendedCommand;
    private boolean driving = false;

    public ActorDrive(GoPiGoAdapter goPiGoAdapter) {
        this.goPiGoAdapter = goPiGoAdapter;

        receive(ReceiveBuilder
                .match(Command.class, command -> command.getName() == FORWARD, this::forward)
                .match(Command.class, command -> command.getName() == BACK, this::back)
                .match(Command.class, command -> command.getName() == STOP_ALL, this::stop)
                .match(Command.class, command -> command.getName() == SUSPEND, this::suspend)
                .build()
        );
    }

    public static Props props(GoPiGoAdapter goPiGoAdapter){
        return Props.create(ActorDrive.class, goPiGoAdapter);
    }

    public void move(Command command)throws IOException {
        if(command.getName() == FORWARD){
            forward(command);
        }
        else if (command.getName() == BACK){
            back(command);
        }

    }

    public void forward(Command command)throws IOException {
        if(command.isStart()){
            setActiveCommand(command);
            goPiGoAdapter.forward();
            driving();
        }
        else {
            stop();
        }
    }

    public void stop() throws IOException {
        goPiGoAdapter.stop();
        activeCommand = Optional.empty();
        drivingStop();
    }

    public void back(Command command)throws IOException {
        if(command.isStart()){
            setActiveCommand(command);
            goPiGoAdapter.back();
            driving();
        }
        else {
            stop();
        }
    }
    public void stop(Command command)throws IOException {
        stop();
    }

    private void driving() {
        driving = true;
    }

    private void drivingStop() {
        driving = false;
    }


    public void suspend(Command command) throws IOException {
        if(driving && command.isStart()){
            suspendedCommand = activeCommand;
            activeCommand = Optional.empty();
            log().info("Suspending command: {}", suspendedCommand.get().getName());
            try {
                Thread.sleep(command.getDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            restoreDriving();
        }
        else if(!driving && command.isStop()){
            stop();
        }

    }

    public void restoreDriving() throws IOException {
        log().info("Restoring command: {}", suspendedCommand.get().getName());
        move(suspendedCommand.get());
        suspendedCommand = Optional.empty();

    }

    private void setActiveCommand(Command command) {
        activeCommand = Optional.of(command);
    }

    public Optional<Command> getActiveCommand() {
        return activeCommand;
    }

}
