package com.fafik.robot;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.Http;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.fafik.robot.control.*;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class AppStart {
    public static void main(String[] args) throws IOException {
        // boot up server using the route as defined below
        final ActorSystem system = ActorSystem.create();
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        // HttpApp.bindRoute expects a route being provided by HttpApp.createRoute
        GoPiGoAdapter goPiGoAdapter = new GoPiGoAdapterMock();
        if(args.length == 1 && "MOCK".equals(args[0])){
            goPiGoAdapter = new GoPiGoAdapterMock();
        }else {
            goPiGoAdapter = new GoPiGoAdapterImpl();
        }
        DeviceManager deviceManager = new DeviceManager(new StandardMovingStrategy(goPiGoAdapter));

        final RouteFlow routeFlow = new RouteFlow(deviceManager);
        final Route route = routeFlow.createRoute();

        final Flow<HttpRequest, HttpResponse, NotUsed> handler = route.flow(system, materializer);
        final CompletionStage<ServerBinding> binding = Http.get(system).bindAndHandle(handler, ConnectHttp.toHost("0.0.0.0", 8080), materializer);
        System.out.println("binding = " + binding);

        binding.exceptionally(failure -> {
            System.err.println("Something very bad happened! " + failure.getMessage());
            system.terminate();
            return null;
        });

        //system.terminate();
    }
}