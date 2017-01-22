package com.fafik.robot.control;

/**
 * Created by Wojciech on 18.09.2016.
 */
public class Command {

    public static final long DEFAULT_COMMAND_DURATION = 500;

    private CommandType type;

    private Long timeStamp;
    private Long commandDuration;

    public Command(CommandType type, Long timeStamp) {
        this.type = type;
        this.timeStamp = timeStamp;

    }

    public CommandType getType() {
        return type;
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
                "type=" + type +
                ", timeStamp=" + timeStamp +
                ", commandDuration=" + commandDuration +
                '}';
    }
}
