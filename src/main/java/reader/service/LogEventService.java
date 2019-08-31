package reader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import reader.model.*;
import reader.repository.CompletedLogEventRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Slf4j
@Service
public class LogEventService {

    private ObjectMapper objectMapper;
    private final CompletedLogEventRepository completedLogEventRepository;

    @Autowired
    public LogEventService(CompletedLogEventRepository completedLogEventRepository) {
        this.completedLogEventRepository = completedLogEventRepository;
        this.objectMapper = new ObjectMapper();
    }

    public List<CompletedLogEvent> read(String fileName) throws IOException {
        InputStream inputStream = new FileSystemResource(fileName).getInputStream();
        Predicate<Long> isAlert = e -> e > 4;
        List<CompletedLogEvent> completedLogEvents = IOUtils.readLines(inputStream, "UTF-8")
                .parallelStream()
                .map(e -> toLogEvent(e))
                .filter(e -> e.isPresent())
                .map(e -> e.get())
                .collect(Collectors.groupingBy(LogEvent::getId))
                .values()
                .parallelStream()
                .map(e -> toCompletedLogEvent(e, isAlert))
                .filter(e -> e.isPresent())
                .map(e -> e.get())
                .collect(Collectors.toList());

        return completedLogEvents.parallelStream().map(e -> save(e)).collect(Collectors.toList());
    }

    private CompletedLogEvent save(CompletedLogEvent completedLogEvent){
        log.info("persisting: {} ", completedLogEvent);
        return completedLogEventRepository.save(completedLogEvent);
    }

    private Optional<CompletedLogEvent> toCompletedLogEvent(List<LogEvent> list, Predicate<Long> isAlert){
        if(list.size() == 2){
            return Optional.of(new CompletedLogEvent(list.get(0), list.get(1), isAlert));
        }else{
            return Optional.empty();
        }
    }

    private Optional<LogEvent> toLogEvent(String value) {
        try {
            JsonNode jsonNode = objectMapper.readValue(value, JsonNode.class);
            log.info("found LogEvent {}", jsonNode );
            if(jsonNode.has("type")){
                return Optional.of(objectMapper.treeToValue(jsonNode, ApplicationLogEvent.class));
            }else{
                return Optional.of(objectMapper.treeToValue(jsonNode, DefaultLogEvent.class));
            }
        } catch (IOException ex){
            log.error("exception when deserializing log event {} ", ex);
        }
        return Optional.empty();
    }
}
