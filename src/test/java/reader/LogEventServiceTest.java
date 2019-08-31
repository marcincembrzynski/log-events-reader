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
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogEventServiceTest {

    private LogEventService logReaderService;

    @Mock
    private CompletedLogEventRepository completedLogEventRepository;

    @Before
    public void before(){
        logReaderService = new LogEventService(completedLogEventRepository);
        CompletedLogEvent a = new CompletedLogEvent("scsmbstgra", 5, "APPLICATION_LOG", "12345", true);
        CompletedLogEvent b = new CompletedLogEvent("scsmbstgrb", 3, null, null, false);
        CompletedLogEvent c = new CompletedLogEvent("scsmbstgrc", 8, null, null, true);
        CompletedLogEvent d = new CompletedLogEvent("scsmbstgrd", 6, null, null, true);
        when(completedLogEventRepository.save(eq(a))).thenReturn(a);
        when(completedLogEventRepository.save(eq(b))).thenReturn(b);
        when(completedLogEventRepository.save(eq(c))).thenReturn(c);
        when(completedLogEventRepository.save(eq(d))).thenReturn(d);
    }

    @Test
    public void shouldRead() throws Exception {

        List<CompletedLogEvent> actual = logReaderService.read("logfile.txt");
        actual.forEach(e -> System.out.println(e));
        Assert.assertEquals(4, actual.size());
        Assert.assertEquals("scsmbstgra", actual.get(0).getId());
        Assert.assertEquals("scsmbstgrb", actual.get(1).getId());
        Assert.assertEquals("scsmbstgrc", actual.get(2).getId());
        Assert.assertEquals("scsmbstgrd", actual.get(3).getId());

        Assert.assertTrue(actual.get(0).isAlert());
        Assert.assertFalse(actual.get(1).isAlert());
        Assert.assertTrue(actual.get(2).isAlert());
        Assert.assertTrue(actual.get(3).isAlert());
    }
}
