package com.fafik.robot.control.actor;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.fafik.robot.control.protocol.Command;
import com.fafik.robot.control.gopigo.GoPiGoAdapter;
import com.fafik.robot.control.DriveStrategy;

import java.io.IOException;
import java.rmi.activation.ActivationSystem;

/**
 * Created by Wojciech on 18.01.2017.
 */
public class ActorDriveManager implements DriveStrategy {

    private ActorRef actorManager;

    public ActorDriveManager (ActorSystem system, GoPiGoAdapter goPiGoAdapter){
        actorManager = system.actorOf(ActorManager.props(goPiGoAdapter), "actorManager");
    }

    static class ActorManager extends AbstractLoggingActor {

        private ActorRef drive;
        private ActorRef turn;

        public ActorManager(GoPiGoAdapter goPiGoAdapter) {
            this.drive = getContext().actorOf(ActorDrive.props(goPiGoAdapter), "driveActor");
            this.turn = getContext().actorOf(ActorTurn.props(goPiGoAdapter, drive), "turnActor");

            receive(ReceiveBuilder
                            .match(Command.class, this::move)
                            .build()
            );
        }

        public static Props props(GoPiGoAdapter goPiGoAdapter){
            return Props.create(ActorManager.class, goPiGoAdapter);
        }

        public void move(Command command)throws IOException {

            if(command.isDrive()){
                drive.forward(command, context());
            } else if(command.isTurn()){
                turn.forward(command, context());
            }
        }
    }


    @Override
    public void move(Command command)throws IOException {
        actorManager.tell(command, ActorRef.noSender());
    }



}
