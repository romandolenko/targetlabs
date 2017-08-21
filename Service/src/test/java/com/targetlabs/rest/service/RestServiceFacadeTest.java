package com.targetlabs.rest.service;

import com.targetlabs.rest.config.Application;
import com.targetlabs.rest.config.MailConfiguration;
import com.targetlabs.rest.dao.FileSystemDocumentDAOImpl;
import com.targetlabs.rest.facade.DocumentServiceController;
import com.targetlabs.rest.notification.NotificationService;
import com.targetlabs.rest.protocol.MetadataDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 20.08.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class, MailConfiguration.class})
@WebAppConfiguration
public class RestServiceFacadeTest {

    private static final Logger log = LoggerFactory.getLogger(RestServiceFacadeTest.class);

    private MockMvc mockMvc;

    @Autowired
    private DocumentService documentService;

    @MockBean
    private NotificationService notificationService;

    private static final String userName = "TestUser";
    private static final String localization = "France";
    private static final String documentName = "test.txt";

    @Before
    public void setup() throws Exception {
        DocumentServiceController documentServiceController = new DocumentServiceController();
        ReflectionTestUtils.setField(documentServiceController, "documentService", documentService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(documentServiceController).build();
    }

    @After
    public void testDown() {
        deleteDirectory(new File(FileSystemDocumentDAOImpl.ROOT_DIRECTORY));
    }

    @Test
    public void uploadFile_ShouldReturnMetadata() throws Exception {
//        String userName = "TestUser";
//        String localization = "France";
        Date uploadDate = new Date();
        final byte[] content = "Hello World".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("file", documentName, "text/plain", content);

        MetadataDocument metadataDocument = documentService.saveDocument(mockFile, userName, localization, uploadDate);

        mockMvc.perform(fileUpload("/service/upload")
                .file(mockFile)
                .param("userName", userName)
                .param("localization", localization)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userName", is(metadataDocument.getUserName())))
                .andExpect(jsonPath("$.documentName", is(metadataDocument.getDocumentName())))
                .andExpect(jsonPath("$.localization", is(metadataDocument.getLocalization())))
                .andExpect(jsonPath("$.date").exists());
    }

    @Test
    public void findMetadataDocuments_SHouldReturnMetadata() throws Exception {

        String userName = "TestUser";
        String localization = "France";

        List<MetadataDocument> metadataDocuments = documentService.findMetadataDocuments(userName, localization);

        mockMvc.perform(get("/service/metadata")
                .param("userName", userName)
                .param("localization", localization)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userName", is(userName)))
                .andExpect(jsonPath("$[0].documentName", is(documentName)))
                .andExpect(jsonPath("$[0].localization", is(localization)))
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].userName", is(userName)))
                .andExpect(jsonPath("$[1].documentName", is(documentName)))
                .andExpect(jsonPath("$[1].localization", is(localization)))
                .andExpect(jsonPath("$[1].date").exists());
    }

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }
}
