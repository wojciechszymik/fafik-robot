package com.fafik.robot.control.protocol;

/**
 * Created by Wojciech on 18.09.2016.
 */
public class Command {

    public static final long DEFAULT_COMMAND_DURATION = 500;

    private CommandName name;
    private CommandType type;
    private Long timeStamp;
    private Long duration;

    public Command() {
    }

    public Command(CommandName name, Long timeStamp) {
        this.name = name;
        this.timeStamp = timeStamp;

    }

    private Command(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.timeStamp = builder.timeStamp;
        this.duration = builder.duration;
    }

    public CommandName getName() {
        return name;
    }

    public CommandType getType() {
        return type;
    }

    public boolean isStart(){
        return type == CommandType.START;
    }

    public boolean isStop(){
        return type == CommandType.STOP;
    }

    public boolean isDrive(){
        return  name == CommandName.FORWARD
                || name == CommandName.BACK
                || name == CommandName.STOP_ALL;
    }

    public boolean isTurn(){
        return  name == CommandName.RIGHT
                || name == CommandName.LEFT;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public Long getDuration() {
        return duration != null ? duration : DEFAULT_COMMAND_DURATION;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name=" + name +
                ", type=" + type +
                ", timeStamp=" + timeStamp +
                ", duration=" + duration +
                '}';
    }

    public static class Builder{
        private CommandName name;
        private CommandType type = CommandType.START;
        private Long timeStamp = System.currentTimeMillis();
        private Long duration;


        public Builder name(CommandName name) {
            this.name = name;
            return this;
        }

        public Builder suspend() {
            this.name = CommandName.SUSPEND;
            return this;
        }

        public Builder forward() {
            this.name = CommandName.FORWARD;
            return this;
        }

        public Builder back() {
            this.name = CommandName.BACK;
            return this;
        }

        public Builder left() {
            this.name = CommandName.LEFT;
            return this;
        }

        public Builder right() {
            this.name = CommandName.RIGHT;
            return this;
        }

        public Builder stopAll() {
            this.name = CommandName.STOP_ALL;
            return this;
        }

        public Builder type(CommandType type) {
            this.type = type;
            return this;
        }

        public Builder start() {
            this.type = CommandType.START;
            return this;
        }

        public Builder stop() {
            this.type = CommandType.STOP;
            return this;
        }

        public Builder timeStamp(Long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder duration(Long duration) {
            this.duration = duration;
            return this;
        }

        public Command build(){
            return new Command(this);
        }
    }
}
