package reader.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reader.model.CompletedLogEvent;


@Repository
public interface CompletedLogEventRepository extends CrudRepository<CompletedLogEvent, String> {
}
