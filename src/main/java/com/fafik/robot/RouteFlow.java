package com.fafik.robot;

import akka.NotUsed;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.marshallers.jackson.;
import akka.http.javadsl.model.ws.TextMessage;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.japi.JavaPartialFunction;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import com.fafik.robot.control.Command;
import com.fafik.robot.control.CommandType;
import com.fafik.robot.control.DeviceManager;

import java.util.Date;

/**
 * Created by Wojciech on 2016-07-17.
 */
public class RouteFlow extends AllDirectives {

    private DeviceManager deviceManager;//= new DeviceManager();

    public RouteFlow(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    public Route createRoute() {
        return path("control", () -> handleWebSocketMessages(greeter()));
    }

    public Flow<Message, Message, NotUsed> greeter() {
        System.out.println("init greeter");
        return Flow.<Message>create()
                        .collect(new JavaPartialFunction<Message, Message>() {
                            @Override
                            public Message apply(Message msg, boolean isCheck) throws Exception {
                                //System.out.println("msg.getStrictText() = " + msg.asTextMessage().getStrictText());
                                if (isCheck) {
                                    if (msg.isText()) {
                                        return null;
                                    } else {
                                        throw noMatch();
                                    }
                                } else {
                                    return handleTextMessage(msg.asTextMessage());
                                }
                            }
                        });
    }


    public TextMessage handleTextMessage(TextMessage msg) {

//        Marshaller.stringToEntity()
        Jackson.as

        if (msg.isStrict()) // optimization that directly creates a simple response...
        {
            //System.out.println("msg.getStrictText() = " + msg.getStrictText());
            deviceManager.move(new Command(CommandType.valueOf(msg.getStrictText()), new Date().getTime()));
            return TextMessage.create("Hello dear " + msg.getStrictText());
        }
        else // ... this would suffice to handle all text messages in a streaming fashion
        {
            return TextMessage.create(Source.single("Hello ").concat(msg.getStreamedText()));
        }
    }


}
