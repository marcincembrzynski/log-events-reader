package reader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reader.model.CompletedLogEvent;
import reader.repository.CompletedLogEventRepository;
import reader.service.LogEventService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogEventServiceTest {

    private LogEventService logReaderService;

    @Mock
    private CompletedLogEventRepository completedLogEventRepository;

    private List<CompletedLogEvent> expected;

    @Before
    public void before(){
        logReaderService = new LogEventService(completedLogEventRepository);
        CompletedLogEvent a = new CompletedLogEvent("scsmbstgra", 5, "APPLICATION_LOG", "12345", true);
        CompletedLogEvent b = new CompletedLogEvent("scsmbstgrb", 3, null, null, false);
        CompletedLogEvent c = new CompletedLogEvent("scsmbstgrc", 8, null, null, true);
        CompletedLogEvent d = new CompletedLogEvent("scsmbstgrd", 6, null, null, true);
        expected = Arrays.asList(a, b, c, d);
        when(completedLogEventRepository.save(eq(a))).thenReturn(a);
        when(completedLogEventRepository.save(eq(b))).thenReturn(b);
        when(completedLogEventRepository.save(eq(c))).thenReturn(c);
        when(completedLogEventRepository.save(eq(d))).thenReturn(d);
    }

    @Test
    public void shouldRead() throws Exception {

        List<CompletedLogEvent> actual = logReaderService.read("logfile.txt");

        Assert.assertEquals(expected, actual);
    }
}
