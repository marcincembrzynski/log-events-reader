package reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import reader.model.CompletedLogEvent;
import reader.service.LogEventService;

import java.util.List;

@Slf4j
@ComponentScan("reader")
@SpringBootApplication
public class App implements CommandLineRunner{

    @Autowired
    private LogEventService logEventService;

    @Value("${default.log.file}")
    private String logFile;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String file = args.length == 0 ? logFile : args[0];
        List<CompletedLogEvent> list = logEventService.read(file);
        log.info("{}", list);
        log.info("number of found and persisted completed log events: {}", list.size());
    }
}
