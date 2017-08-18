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

import java.io.IOException;
import java.text.ParseException;
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
            log.info("Start sending notification!");
            List<MetadataDocument> metadataDocumentList = getRestService().findAllMetadataDocumentsByPeriod(Long.valueOf(fixedDelayString));
            List<Document> documents = getDocuments(metadataDocumentList);
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



    private List<Document> getDocuments(List<MetadataDocument> metadataDocumentList) throws IOException, ParseException {
        List<Document> documents = new ArrayList<>();
        for (MetadataDocument metadataDocument : metadataDocumentList) {
            documents.add(getRestService().findDocumentById(metadataDocument.getId()));
        }
        return documents;
    }
}
