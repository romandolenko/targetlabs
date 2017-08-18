package com.targetlabs.rest.job;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import com.targetlabs.rest.service.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dolenko Roman <roman.dolenko@olfatrade.com> on 15.08.2017.
 */
@Component
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private RestService restService;

    private static final String SCHEDULE_DELAY_VALUE = "${schedule.delay.seconds}";

    @Value(SCHEDULE_DELAY_VALUE)
    private String fixedDelayString;

    @Scheduled(fixedDelayString = SCHEDULE_DELAY_VALUE)
    private void sendNotification() {
        try {
            List<MetadataDocument> metadataDocumentList = getRestService().findAllMetadataDocumentsByPeriod(Long.valueOf(fixedDelayString));
            List<Document> documents = new ArrayList<>();
            for(MetadataDocument metadataDocument : metadataDocumentList) {
                documents.add(getRestService().findDocumentById(metadataDocument.getId()));
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public RestService getRestService() {
        return restService;
    }

    public void setRestService(RestService restService) {
        this.restService = restService;
    }
}
