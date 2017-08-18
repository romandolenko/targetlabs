package com.targetlabs.rest.dao;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 16.08.2017.
 */
@Service("fileSystemDocumentDAO")
public class FileSystemDocumentDAOImpl implements FileSystemDocumentDAO {

    private static final Logger log = Logger.getLogger(FileSystemDocumentDAOImpl.class);

    private static final String ROOT_DIRECTORY = "documents";
    private static final String METADATA_FILE = "metadata.properties";
    private static final String DOCUMENT_ID = "UUID";
    private static final String USER_NAME = "UserName";
    private static final String DOCUMENT_NAME = "DocumentName";
    private static final String DOCUMENT_DATE = "DocumentCreated";
    private static final String DOCUMENT_LOCALIZATION = "Localization";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String saveDocument(Document document) throws IOException {
        String id = UUID.randomUUID().toString();
        String path = getDocumentPath(id);
        File file = new File(path);
        if (file.mkdirs()) {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(new File(path), document.getDocumentName())));
            stream.write(document.getDocumentData());
            stream.close();
            return id;
        } else {
            String errorMessage = "Error while inserting document";
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }

    @Override
    public void saveMetadataDocument(MetadataDocument metadataDocument) throws IOException {
        String path = getDocumentPath(metadataDocument.getId());
        File metadataFile = new File(new File(path), METADATA_FILE);
        FileOutputStream out = new FileOutputStream(metadataFile);
        Properties properties = createProperties(metadataDocument);
        properties.store(out, "Meta data document");
    }

    @Override
    public List<MetadataDocument> findMetadataDocuments(String userName, String localization, Date date) throws IOException, ParseException {
        List<String> idList = getDocumentIdList();
        if (idList.isEmpty()) {
            return new ArrayList<>();
        }
        List<MetadataDocument> metadataList = new ArrayList<MetadataDocument>(idList.size());
        for (String id : idList) {
            MetadataDocument metadataDocument = getMetadataDocument(id);
            if (isMatched(metadataDocument, userName, localization, date)) {
                metadataList.add(metadataDocument);
            }
        }
        return metadataList;
    }

    @Override
    public List<MetadataDocument> findAllMetadataDocumentsByPeriod(Long delay) throws IOException, ParseException {
        List<MetadataDocument> metadataList = findMetadataDocuments(null, null, null);
        return metadataList.stream().filter(item -> (System.currentTimeMillis() - item.getDate().getTime()) < delay).collect(Collectors.toList());
    }

    @Override
    public Document findDocumentById(String id) throws IOException, ParseException {
        MetadataDocument metadataDocument = getMetadataDocument(id);
        if (metadataDocument == null) {
            return null;
        }
        Path path = Paths.get(getDocumentPath(metadataDocument));
        return new Document(metadataDocument.getDocumentName(), Files.readAllBytes(path));
    }

    private String getDocumentPath(MetadataDocument metadata) {
        return getDocumentPath(metadata.getId()) + File.separator + metadata.getDocumentName();
    }

    private String getDocumentPath(String id) {
        return ROOT_DIRECTORY + File.separator + id;
    }

    private Properties createProperties(MetadataDocument metadataDocument) {
        Properties properties = new Properties();
        properties.put(DOCUMENT_ID, metadataDocument.getId());
        properties.put(USER_NAME, metadataDocument.getUserName());
        properties.put(DOCUMENT_NAME, metadataDocument.getDocumentName());
        properties.put(DOCUMENT_DATE, DATE_FORMAT.format(metadataDocument.getDate()));
        properties.put(DOCUMENT_LOCALIZATION, metadataDocument.getLocalization());
        return properties;
    }

    private MetadataDocument getMetadataDocument(String id) throws IOException, ParseException {
        MetadataDocument metadataDocument = null;
        String path = getDocumentPath(id);
        File file = new File(path);
        if (file.exists()) {
            Properties properties = readProperties(id);
            metadataDocument = new MetadataDocument(properties.getProperty(DOCUMENT_ID), properties.getProperty(USER_NAME),
                    properties.getProperty(DOCUMENT_NAME), properties.getProperty(DOCUMENT_LOCALIZATION),
                    DATE_FORMAT.parse(properties.getProperty(DOCUMENT_DATE)));
        }
        return metadataDocument;
    }

    private List<String> getDocumentIdList() {
        File file = new File(ROOT_DIRECTORY);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        });
        if (directories != null) {
            return Arrays.asList(directories);
        }
        return new ArrayList<>();
    }

    private Properties readProperties(String uuid) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(getDocumentPath(uuid), METADATA_FILE));
            properties.load(inputStream);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return properties;
    }

    private boolean isMatched(MetadataDocument metadata, String userName, String localization, Date date) {
        if (metadata == null) {
            return false;
        }
        boolean match = true;
        if (userName != null) {
            match = (userName.equals(metadata.getUserName()));
        }
        if (match && date != null) {
            match = (date.equals(metadata.getDate()));
        }
        if (localization != null) {
            match = (localization.equals(metadata.getLocalization()));
        }
        return match;
    }

}
