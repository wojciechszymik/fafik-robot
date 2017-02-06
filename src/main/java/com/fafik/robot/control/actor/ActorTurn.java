package com.fafik.robot.control.actor;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.fafik.robot.control.protocol.Command;
import com.fafik.robot.control.gopigo.GoPiGoAdapter;
import com.fafik.robot.control.DriveStrategy;

import java.io.IOException;

import static com.fafik.robot.control.protocol.CommandName.LEFT;
import static com.fafik.robot.control.protocol.CommandName.RIGHT;

/**
 * Created by Wojciech on 18.01.2017.
 */
public class ActorTurn extends AbstractLoggingActor {

    private GoPiGoAdapter goPiGoAdapter;

    private ActorRef movingDrive;

    public ActorTurn(GoPiGoAdapter goPiGoAdapter, ActorRef movingDrive) {
        this.goPiGoAdapter = goPiGoAdapter;
        this.movingDrive = movingDrive;

        receive(ReceiveBuilder
                        .match(Command.class, cmd -> cmd.getName() == LEFT, this::left)
                        .match(Command.class, cmd -> cmd.getName() == RIGHT, this::right)
                        .build()
        );
    }

    public static Props props(GoPiGoAdapter goPiGoAdapter, ActorRef movingDrive){
        return Props.create(ActorTurn.class, goPiGoAdapter, movingDrive);
    }

    public void right(Command command) throws IOException {
        turn(command, () -> goPiGoAdapter.right());
    }

    public void left(Command command) throws IOException {
        turn(command, () -> goPiGoAdapter.left());
    }

    public void turn(Command command, Runnable turn) throws IOException {
        if(command.isStart()){
            movingDrive.tell(new Command.Builder().suspend().build(), self());
            turn.run();
        }else {
            movingDrive.tell(new Command.Builder().suspend().stop().build(), self());
        }
    }

}
