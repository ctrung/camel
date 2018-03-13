package camel.routeid;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static org.apache.camel.LoggingLevel.INFO;

/**
 * Test if routeId can be change during exchange.
 *
 * <p>Answer is no : the route is built once at startup, last defined routeId wins</p>
 *
 * <pre>
 Logs examples :
 2018-03-13 13:23:33.137  INFO 19285 --- [#0 - file://dir] ID-2                                     : starting
 2018-03-13 13:23:33.154  INFO 19285 --- [#0 - file://dir] ID-2                                     : log with ID-1
 2018-03-13 13:23:33.154  INFO 19285 --- [#0 - file://dir] ID-2                                     : ending
 * </pre>
 *
 * @author clement.trung
 */

@Component
@Profile("when-route-id")
public class WhenRouteIdRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("file:dir")
				.log(INFO, "starting")
				.setHeader("foo", simple("foo"))
				.choice()
					.when(simple("${header.foo} == 'foo'"))
						.routeId("ID-1")
						.log(INFO, "log with ID-1")
					.otherwise()
						.routeId("ID-2")
						.log(INFO, "log with ID-2")
				.end()
				.log(INFO, "ending");

	}
}
