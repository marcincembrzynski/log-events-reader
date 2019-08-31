package reader.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.function.Predicate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletedLogEvent {
    @Id
    private String id;
    private long duration;
    private String type;
    private String host;
    private boolean alert;

    public CompletedLogEvent(LogEvent logEvent1, LogEvent logEvent2, Predicate<Long> isAlert) {
        this.id = logEvent1.getId();
        this.duration = Math.abs(logEvent2.getTimestamp() - logEvent1.getTimestamp());
        this.alert = isAlert.test(duration);
        if(logEvent1 instanceof ApplicationLogEvent){
            this.host = ((ApplicationLogEvent) logEvent1).getHost();
            this.type = ((ApplicationLogEvent) logEvent1).getType();
        }
    }
}
