package apps.amaralus.cameldemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyService {

    public SomeData processData(SomeData someData) {
        log.info("Some data: {}", someData);
        return someData;
    }
}
