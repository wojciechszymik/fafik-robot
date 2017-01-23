package com.fafik.robot.control;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Wojciech on 23.01.2017.
 */
public class StandardMovingStrategyTest {

    private MovingStrategy movingStrategy;

    @Before
    public void setUp(){
        movingStrategy = new StandardMovingStrategy(new GoPiGoAdapterDummy());
    }

    @Test
    public void moveForwardStartShouldSetDriving() throws IOException {
        //given
        Command commandForward = new Command.Builder().commandName(CommandName.FORWARD).commandType(CommandType.START).build();

        //when
        movingStrategy.move(commandForward);

        //then
        assertThat(movingStrategy.isTurning()).isFalse();
        assertThat(movingStrategy.isDriving()).isTrue();
    }

    @Test
    public void moveForwardStopShouldStopDriving() throws IOException {
        //given
        Command commandForwardStart = new Command.Builder().commandName(CommandName.FORWARD).commandType(CommandType.START).build();
        Command commandForwardStop = new Command.Builder().commandName(CommandName.FORWARD).commandType(CommandType.STOP).build();

        //when
        movingStrategy.move(commandForwardStart);

        //then
        assertThat(movingStrategy.isTurning()).isFalse();
        assertThat(movingStrategy.isDriving()).isTrue();

        //when
        movingStrategy.move(commandForwardStop);
        //then
        assertThat(movingStrategy.isTurning()).isFalse();
        assertThat(movingStrategy.isDriving()).isFalse();
    }

    @Test
    public void turningLeftShouldSuspendDriving() throws IOException {
        //given
        Command commandForwardStart = new Command.Builder().commandName(CommandName.FORWARD).commandType(CommandType.START).build();
        Command commandLeftStart = new Command.Builder().commandName(CommandName.LEFT).commandType(CommandType.START).build();

        //when
        movingStrategy.move(commandForwardStart);

        //then
        assertThat(movingStrategy.isTurning()).isFalse();
        assertThat(movingStrategy.isDriving()).isTrue();

        //when
        movingStrategy.move(commandLeftStart);
        //then
        assertThat(movingStrategy.isTurning()).isFalse();
        assertThat(movingStrategy.isDriving()).isTrue();
    }

}