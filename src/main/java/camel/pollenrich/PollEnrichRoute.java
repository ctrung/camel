package camel.pollenrich;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Poll enrich EIP only works with a single file at a time.
 * 
 * @author clement.trung
 */

@Component
@Profile("pollEnrich")
public class PollEnrichRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("quartz2://trigger1?cron=0/60+*+*+*+*+?")
			.pollEnrich().simple("file:dir")
			.log("poll enrich : ${body}");
	}
}
