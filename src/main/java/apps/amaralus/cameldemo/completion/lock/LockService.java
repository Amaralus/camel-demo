package apps.amaralus.cameldemo.completion.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.util.UUIDConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LockService {

    private static final String LOCK_KEY = UUIDConverter.getUUID("aggregation-completion").toString();

    private final LockRepository lockRepository;

    @SuppressWarnings("java:S2222")
    public boolean tryLock() {
        var acquired = lockRepository.acquire(LOCK_KEY);
        if (acquired)
            log.debug("Lock {} acquired", LOCK_KEY);
        return acquired;
    }

    public void unlock() {
        lockRepository.delete(LOCK_KEY);
        log.debug("Lock {} released", LOCK_KEY);
    }
}
