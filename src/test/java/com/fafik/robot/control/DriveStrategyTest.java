package com.fafik.robot.control;

import akka.actor.ActorSystem;
import com.fafik.robot.control.gopigo.GoPiGoAdapterDummy;
import com.fafik.robot.control.protocol.Command;
import com.fafik.robot.control.protocol.CommandName;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * Created by Wojciech on 05.02.2017.
 */
public class DriveStrategyTest {

    private DriveStrategy driveStrategy;

    private GoPiGoAdapterDummy goPiGoAdapter;

    @Before
    public void setUp(){
        ActorSystem system = ActorSystem.create("testSystem");
        DriveStrategyFactory factory = new DriveStrategyFactory(system);
        goPiGoAdapter = mock(GoPiGoAdapterDummy.class);
        driveStrategy = factory.create(goPiGoAdapter, "ACTOR");

    }

    @Test
    public void afterCommandForwardShouldGoForward() throws IOException, InterruptedException {

        driveStrategy.move(new Command.Builder().forward().build());
        verify(goPiGoAdapter, timeout(300)).forward();

    }

    @Test
    public void whenForwardAfterCommandRightShouldRestoreForward() throws IOException, InterruptedException {

        driveStrategy.move(new Command.Builder().forward().build());
        verify(goPiGoAdapter, timeout(300)).forward();

        driveStrategy.move(new Command.Builder().right().build());
        verify(goPiGoAdapter, timeout(300)).right();

        driveStrategy.move(new Command.Builder().right().stop().build());
        verify(goPiGoAdapter, timeout(300)).forward();

    }

}