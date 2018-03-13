package camel.routeid;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

import static org.apache.camel.LoggingLevel.INFO;

/**
 * Test if routeId can be change during exchange.
 *
 * <p>Answer is no : the route is built once at startup, last defined routeId wins</p>
 *
 * <pre>
 Logs examples :
 2018-03-13 13:09:52.934  INFO 13537 --- [#0 - file://dir] ID-2                                     : log avec ID-1
 2018-03-13 13:09:52.936  INFO 13537 --- [#0 - file://dir] ID-2                                     : log avec ID-2
 * </pre>
 *
 * @author clement.trung
 */

@Component
@Profile("multiple-route-id")
public class MultipleRouteIdRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("file:dir")
				.routeId("ID-1")
				.log(INFO, "log with ID-1")
				.routeId("ID-2")
				.log(INFO, "log with ID-2");
	}
}
