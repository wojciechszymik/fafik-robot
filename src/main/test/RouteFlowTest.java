import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;
import org.junit.Test;


public class RouteFlowTest {

    @Test
    public void Test() throws InterruptedException {

        System.out.println("Running test ...");

        ActorSystem actorSystem = ActorSystem.create("FirstActorsSys");
        final Materializer materializer = ActorMaterializer.create(actorSystem);

        final Source<Integer, NotUsed> source = Source.range(1, 100);

        source.runForeach(i -> System.out.println(i), materializer);
        Thread.sleep(50);


    }
}