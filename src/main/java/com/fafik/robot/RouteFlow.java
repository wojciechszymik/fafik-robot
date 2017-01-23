package com.fafik.robot;

import akka.NotUsed;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.model.ws.TextMessage;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.japi.JavaPartialFunction;
import akka.stream.javadsl.Flow;
import com.fafik.robot.control.Command;
import com.fafik.robot.control.DeviceManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Wojciech on 2016-07-17.
 */
public class RouteFlow extends AllDirectives {

    private DeviceManager deviceManager;//= new DeviceManager();

    ObjectMapper objectMapper;

    public RouteFlow(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
        this.objectMapper = new ObjectMapper();
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

        try {
            Command command = objectMapper.readValue(msg.getStrictText(), Command.class);
            deviceManager.move(command);
            return TextMessage.create("Message received: " + command.getCommandName());
        } catch (IOException e) {
            e.printStackTrace();
            return TextMessage.create("Error during parsing a message: " + msg.getStrictText());
        }

    }


}
