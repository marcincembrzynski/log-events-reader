package reader;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reader.model.CompletedLogEvent;
import reader.repository.CompletedLogEventRepository;
import reader.service.LogEventService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogEventServiceIntegrationTest {

    @Autowired
    private LogEventService logEventService;

    @Autowired
    private CompletedLogEventRepository completedLogEventRepository;

    private List<String> ids;

    @Before
    public void before(){
        ids = Arrays.asList("scsmbstgra", "scsmbstgrb", "scsmbstgrc", "scsmbstgrd");
    }

    @Test
    public void shouldStoreCompletedLogEvents() throws IOException{

        ids.forEach(id -> completedLogEventRepository.findById(id).ifPresent(e -> completedLogEventRepository.deleteById(e.getId())));

        List<CompletedLogEvent> actual = logEventService.read("logfile.txt");

        ids.forEach(id -> Assert.assertTrue(completedLogEventRepository.findById(id).isPresent()));


    }
}
