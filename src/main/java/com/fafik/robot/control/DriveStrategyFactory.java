package com.fafik.robot.control;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.fafik.robot.control.actor.ActorDriveManager;
import com.fafik.robot.control.gopigo.GoPiGoAdapter;

/**
 * Created by Wojciech on 05.02.2017.
 */
public class DriveStrategyFactory {

    private ActorSystem system;

    public DriveStrategyFactory(ActorSystem system) {
        this.system = system;
    }

    public DriveStrategy create(GoPiGoAdapter goPiGoAdapter, String name){
        if(name.equalsIgnoreCase("ACTOR")){
            return createActorStrategy(goPiGoAdapter);
        }
        return new StandardDriveStrategy(goPiGoAdapter);
    }

    private DriveStrategy createActorStrategy(GoPiGoAdapter goPiGoAdapter) {

        ActorDriveManager actorDriveManager = new ActorDriveManager(system, goPiGoAdapter);

        return actorDriveManager;
    }
}
