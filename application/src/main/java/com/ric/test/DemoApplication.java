package com.ric.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.Cluster;
import akka.cluster.metrics.AdaptiveLoadBalancingGroup;
import akka.cluster.metrics.CpuMetricsSelector;
import akka.cluster.routing.ClusterRouterGroup;
import akka.cluster.routing.ClusterRouterGroupSettings;
import akka.routing.RoundRobinPool;
import com.ric.test.actor.publisher.PublisherActor;
import com.ric.test.actor.worker.WorkerActor;
import com.ric.test.config.spring.SpringExtension;
import com.ric.test.config.spring.SpringProps;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.util.List;

import static akka.pattern.Patterns.ask;
import static java.util.Collections.singletonList;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	private ActorSystem system;

	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) throws IOException {
		// REST SERVICE
		SpringApplication.run(DemoApplication.class, args);

	}

	@Bean
	public ActorSystem actorSystem(ApplicationContext context) {

		ActorSystem system = ActorSystem.create("demosystem", ConfigFactory.load());
		SpringExtension.getInstance().get(system).initialize(context);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					Cluster cluster = Cluster.get(system);
					cluster.leave(cluster.selfAddress());
				})
		);

		return system;
	}

	@Bean("clusterDemoRouter")
	@Profile("consumer")
	public ActorRef clusterProcessorRouter() {
		List<String> path = singletonList("/user/localDemoRouter");
		return system.actorOf(new ClusterRouterGroup(new AdaptiveLoadBalancingGroup(CpuMetricsSelector.getInstance(), path),
				new ClusterRouterGroupSettings(100, path, false, "worker")).props(), "clusterDemoRouter");
	}

	@Bean("localDemoRouter")
	@Profile("worker")
	public ActorRef localProcessorRouter() {
		return system.actorOf(SpringProps.create(system, WorkerActor.class)
				.withDispatcher("processor-dispatcher")
				.withRouter(new RoundRobinPool(10)), "localDemoRouter");
	}

	@Bean("localPublisherRouter")
	@Profile("publisher")
	public ActorRef localPublisherRouter() {
		System.out.println("PUB -> 1");
		return system.actorOf(
				SpringProps.create(system, PublisherActor.class, Integer.valueOf(0)));
	}

}