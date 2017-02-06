package com.fafik.robot.control.gopigo;

import com.dexterind.gopigo.Gopigo;
import com.dexterind.gopigo.GopigoListener;
import com.dexterind.gopigo.events.StatusEvent;
import com.dexterind.gopigo.events.VoltageEvent;
import com.dexterind.gopigo.utils.Statuses;

import java.io.IOException;

/**
 * Created by Wojciech on 25.09.2016.
 */
public class GoPiGoAdapterImpl implements GoPiGoAdapter, GopigoListener {

    private Gopigo gopigo;

    public GoPiGoAdapterImpl() {

        gopigo = Gopigo.getInstance();
        gopigo.addListener(this);
//        gopigo.ultraSonicSensor.setPin(15);
        gopigo.setMinVoltage(5.5);
        gopigo.init();
    }

    @Override
    public void forward() {
        try {
            System.out.println("going forward");
            gopigo.motion.forward(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void back() {
        try {
            System.out.println("going back");
            gopigo.motion.backward(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            System.out.println("stop");
            gopigo.motion.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void left() {
        try {
            System.out.println("going left");
            gopigo.motion.left();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void right() {
        try {
            System.out.println("going right");
            gopigo.motion.right();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusEvent(StatusEvent statusEvent) {
        System.out.println("\f");
        System.out.println("[Status Changed]");
        switch (statusEvent.status) {
            case Statuses.INIT:
                System.out.println("OK Init");
                break;
            case Statuses.HALT:
                System.out.println("WARN Halt");
                break;
        }

    }

    @Override
    public void onVoltageEvent(VoltageEvent voltageEvent) {

        System.out.println("\f");
        System.out.println("[Voltage Event]");
        System.out.println(voltageEvent.value + " Volts");
    }






}
