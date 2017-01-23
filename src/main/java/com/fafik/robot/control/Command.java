package com.fafik.robot.control;

import java.util.Objects;

/**
 * Created by Wojciech on 18.09.2016.
 */
public class Command {

    public static final long DEFAULT_COMMAND_DURATION = 500;

    private CommandName commandName;

    private CommandType commandType;

    private Long timeStamp;
    private Long commandDuration;

    public Command() {
    }

    public Command(CommandName commandName, Long timeStamp) {
        this.commandName = commandName;
        this.timeStamp = timeStamp;

    }

    private Command(Builder builder) {
        this.commandName = builder.commandName;
        this.commandType = builder.commandType;
        this.timeStamp = builder.timeStamp;
        this.commandDuration = builder.commandDuration;
    }

    public CommandName getCommandName() {
        return commandName;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public boolean isStart(){
        return commandType == CommandType.START;
    }

    public boolean isStop(){
        return commandType == CommandType.STOP;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public Long getCommandDuration() {
        return commandDuration != null ? commandDuration : DEFAULT_COMMAND_DURATION;
    }

    @Override
    public String toString() {
        return "Command{" +
                "commandName=" + commandName +
                ", commandType=" + commandType +
                ", timeStamp=" + timeStamp +
                ", commandDuration=" + commandDuration +
                '}';
    }

    public static class Builder{
        private CommandName commandName;
        private CommandType commandType;
        private Long timeStamp = System.currentTimeMillis();
        private Long commandDuration;


        public Builder commandName(CommandName commandName) {
            this.commandName = commandName;
            return this;
        }

        public Builder commandType(CommandType commandType) {
            this.commandType = commandType;
            return this;
        }

        public Builder timeStamp(Long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder commandDuration(Long commandDuration) {
            this.commandDuration = commandDuration;
            return this;
        }

        public Command build(){
            return new Command(this);
        }
    }
}
