package camel.pollenrich;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * Poll all files at once.
 *
 * <p>Dynamic folder filtering uses file protocol filter option (previous day folder in this route)</p>
 * 
 * @author clement.trung
 */

@Component
@Configuration
@Profile("scheduledPollAllFilesAtOnce2")
public class ScheduledPollAllFilesAtOnce2Route extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("sftp:oasis@localhost/ftp/paragon/in/receptionlog/arch?" +
                    "scheduler=quartz2&" +
                    "scheduler.cron=0+27+13+*+*+?&" +
                    "filter=#myFilter&" +
                    "recursive=true&" +
					"knownHostsFile=.ssh/known_hosts&" +
					"privateKeyFile=.ssh/id_rsa&" +
					"privateKeyPassphrase=&" +
					"exclude=.*\\.tmp&" +
					"noop=true&" +
					"autoCreate=false")
            .log("files found : ${header.CamelFileName} >> ${body}");
	}

    static class MyFileFilter<T> implements GenericFileFilter<T> {
        public boolean accept(GenericFile<T> file) {

            if(file.isDirectory()) return true; // recursive option enabled, traversal directory is allowed

            LocalDate yesterday = LocalDate.now().minusDays(1);
            return file.getFileName().matches(yesterday.format(ofPattern("yyyy/MM/dd"))+ "/RECEPTION_.*.psv");
        }
    }

    @Bean(name="myFilter")
    public <T> ScheduledPollAllFilesAtOnce2Route.MyFileFilter<T> myFilter(){
        return new ScheduledPollAllFilesAtOnce2Route.MyFileFilter<>();
    }
}
