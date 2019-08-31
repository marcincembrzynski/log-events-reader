package reader.model;

import lombok.*;

@Getter
@Setter
@ToString
public abstract class LogEvent {

    private String id;
    private String state;
    private long timestamp;

    public boolean isStart(){
        return "STARTED".equals(state);
    }

}
