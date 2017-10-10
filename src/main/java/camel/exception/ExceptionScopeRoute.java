package camel.exception;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("exception")
public class ExceptionScopeRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		DataFormat csv = new BindyCsvDataFormat(Invoice.class);
		
		from("file:dir")
			.unmarshal(csv)
			.log("unmarshal : ${body}")
			.split().body()
			.multicast()
				.to("jms:queue:toSalesforce", "jms:queue:toMailjet");
		
		from("jms:queue:toSalesforce")
			.onException(Exception.class)
				.useOriginalMessage()
				.handled(true)
				.setHeader("type", constant("SF_ERROR"))
				.log("erreur ${exception.stacktrace}")
			.end()
			.log("item : ${body}")
			.throwException(new Exception("prout /// "));
		
		from("jms:queue:toMailjet")
			.log("item : ${body}");
	}
}