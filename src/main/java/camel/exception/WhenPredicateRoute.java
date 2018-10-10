package camel.exception;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("exception-when")
public class WhenPredicateRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		onException(Exception.class)
				.maximumRedeliveries(0)
				.handled(true)
				.process(e -> System.out.println("setup..."))
				.choice()
					.when(header("action").isEqualTo("1"))
						.process(e -> System.out.println("header action = " + e.getIn().getHeader("action") + " : je suis dans le 1er when"))
					.when(header("action").isEqualTo("2"))
						.process(e -> System.out.println("header action = " + e.getIn().getHeader("action") + " : je suis dans le 2eme when"))
					.end()
				.process(e -> System.out.println("teardown..."));
		
		from("file:dir")
			.setHeader("action", () -> "1")
			.throwException(new Exception());

	}
}