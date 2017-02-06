package com.fafik.robot;

import akka.NotUsed;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.model.ws.TextMessage;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.japi.JavaPartialFunction;
import akka.stream.javadsl.Flow;
import com.fafik.robot.control.DriveStrategy;
import com.fafik.robot.control.protocol.Command;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Wojciech on 2016-07-17.
 */
public class RouteFlow extends AllDirectives {

    private DriveStrategy driveStrategy;
    private ObjectMapper objectMapper;

    public RouteFlow(DriveStrategy driveStrategy) {
        this.driveStrategy = driveStrategy;
        this.objectMapper = new ObjectMapper();
    }

    public Route createRoute() {
        return path("control", () -> handleWebSocketMessages(control()));
    }

    public Flow<Message, Message, NotUsed> control() {
        System.out.println("init control");
        return Flow.<Message>create()
                        .collect(new JavaPartialFunction<Message, Message>() {
                            @Override
                            public Message apply(Message msg, boolean isCheck) throws Exception {
                                return handleTextMessage(msg.asTextMessage());
                            }
                        });
    }


    public TextMessage handleTextMessage(TextMessage msg) {

        try {
            Command command = objectMapper.readValue(msg.getStrictText(), Command.class);
            driveStrategy.move(command);
            return TextMessage.create("Message received: " + command.getName());
        } catch (IOException e) {
            e.printStackTrace();
            return TextMessage.create("Error during parsing a message: " + msg.getStrictText());
        }

    }


}
