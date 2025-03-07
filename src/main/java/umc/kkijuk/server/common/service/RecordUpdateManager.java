package umc.kkijuk.server.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.record.domain.Record;
import umc.kkijuk.server.record.repository.RecordRepository;

@Component
@RequiredArgsConstructor
public class RecordUpdateManager {
    private final RecordRepository recordRepository;

    @Transactional
    public void updateRecordTimestamp(Record record) {
        record.updateTimestamp();
    }
}
