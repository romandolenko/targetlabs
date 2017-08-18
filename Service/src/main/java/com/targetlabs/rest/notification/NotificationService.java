package com.targetlabs.rest.notification;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import com.targetlabs.rest.service.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A scheduler to poll for new items in the last hour and send an email
 *
 * @author Dolenko Roman <roman.dolenko@olfatrade.com> on 15.08.2017.
 */
@PropertySource("classpath:mail.properties")
@Component
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private RestService restService;

    @Autowired
    private JavaMailSender mailSender;

    private static final String SCHEDULE_DELAY_VALUE = "${schedule.delay.seconds}";
    @Value("${mail.from}")
    private String mailFrom;
    @Value("${mail.to}")
    private String mailTo;

    @Value(SCHEDULE_DELAY_VALUE)
    private String fixedDelayString;

    /**
     *  Sends email notification with document attachments.
     */
    @Scheduled(fixedDelayString = SCHEDULE_DELAY_VALUE)
    private void sendNotification() {
        try {
            log.info("Start sending notification!");
            List<MetadataDocument> metadataDocumentList = getRestService().findAllMetadataDocumentsByPeriod(Long.valueOf(fixedDelayString));
            List<Document> documents = getDocuments(metadataDocumentList);
            if (documents.isEmpty()) {
                return;
            }
            sendMessage(documents);
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

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendMessage(List<Document> documents) throws MessagingException {
        MimeMessage message = getMailSender().createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(mailTo);
        helper.setFrom(mailFrom);
        helper.setSubject("List of uploaded documents");
        helper.setText("Please, check documents!");
        for (Document document : documents) {
            helper.addAttachment(document.getDocumentName(), new ByteArrayResource(document.getDocumentData()));
        }
        getMailSender().send(message);
    }

    private List<Document> getDocuments(List<MetadataDocument> metadataDocumentList) throws IOException, ParseException {
        List<Document> documents = new ArrayList<>();
        for (MetadataDocument metadataDocument : metadataDocumentList) {
            documents.add(getRestService().findDocumentById(metadataDocument.getId()));
        }
        return documents;
    }
}
