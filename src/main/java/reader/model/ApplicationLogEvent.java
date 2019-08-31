package reader.model;

import lombok.*;

@Getter
@Setter
@ToString
public class ApplicationLogEvent extends LogEvent {
    private String type;
    private String host;
}
