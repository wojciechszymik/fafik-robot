package com.fafik.robot.control.gopigo;

/**
 * Created by Wojciech on 05.02.2017.
 */
public class GoPiGoAdapterFactory {

    public static GoPiGoAdapter create(String name){
        if("MOCK".equals(name)){
            return new GoPiGoAdapterDummy();
        }
        return new GoPiGoAdapterImpl();
    }
}
