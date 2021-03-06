package camel.pollenrich;

import java.io.File;
import java.util.Arrays;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Use a bean to poll all files at once.
 *
 * <p>Drawback is you have to implement polling yourself.</p>
 * 
 * @author clement.trung
 */

@Component
@Profile("scheduledPollAllFilesAtOnce1")
public class ScheduledPollAllFilesAtOnce1Route extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("quartz2://trigger1?trigger.repeatCount=0")
			.bean(ScheduledPollAllFilesAtOnce1Route.class, "listFiles")
			.split().body()
			.log("poll enrich all files at once : ${body}");
	}
	
	public File[] listFiles(String body){
		
		String[] ns = new File("dir")
			.list(new SuffixFileFilter(".csv"));
		
		return Arrays.stream(ns)
		    .map(File::new)
		    .toArray(File[]::new);
	}
}
