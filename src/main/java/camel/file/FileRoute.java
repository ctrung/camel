package camel.file;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("file")
public class FileRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("file:dir")
			.log("file : ${body}");
	}
}
