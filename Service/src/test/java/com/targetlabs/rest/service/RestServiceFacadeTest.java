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
import java.io.IOException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private static final String body = "Hello World";

    @Before
    public void setup() throws Exception {
        deleteDirectory(new File(FileSystemDocumentDAOImpl.ROOT_DIRECTORY));
        DocumentServiceController documentServiceController = new DocumentServiceController();
        ReflectionTestUtils.setField(documentServiceController, "documentService", documentService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(documentServiceController).build();
    }

    @After
    public void tearDown() {
//        deleteDirectory(new File(FileSystemDocumentDAOImpl.ROOT_DIRECTORY));
    }

    @Test
    public void uploadFile_ShouldReturnMetadata() throws Exception {
        MockMultipartFile mockFile = buildMockMultipartFile();
        mockMvc.perform(fileUpload("/service/upload")
                .file(mockFile)
                .param("userName", userName)
                .param("localization", localization)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userName", is(userName)))
                .andExpect(jsonPath("$.documentName", is(documentName)))
                .andExpect(jsonPath("$.localization", is(localization)))
                .andExpect(jsonPath("$.date").exists());
    }

    @Test
    public void uploadFile_MissedFile_ShouldReturnException() throws Exception {
        mockMvc.perform(fileUpload("/service/upload")
                .param("userName", userName)
                .param("localization", localization)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Required request part 'file' is not present")));
    }

    @Test
    public void uploadFile_MissedUserName_ShouldReturnException() throws Exception {
        MockMultipartFile mockFile = buildMockMultipartFile();
        mockMvc.perform(fileUpload("/service/upload")
                .file(mockFile)
                .param("localization", localization)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Required String parameter 'userName' is not present")));
    }

    @Test
    public void uploadFile_MissedLocalization_ShouldReturnException() throws Exception {
        MockMultipartFile mockFile = buildMockMultipartFile();
        mockMvc.perform(fileUpload("/service/upload")
                .file(mockFile)
                .param("userName", userName)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findMetadataDocuments_AllParams_ShouldReturnMetadata() throws Exception {
        saveDocument();
        mockMvc.perform(get("/service/metadata")
                .param("userName", userName)
                .param("localization", localization)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userName", is(userName)))
                .andExpect(jsonPath("$[0].documentName", is(documentName)))
                .andExpect(jsonPath("$[0].localization", is(localization)))
                .andExpect(jsonPath("$[0].date").exists());
    }

    @Test
    public void findMetadataDocuments_ByUserName_ShouldReturnMetadata() throws Exception {
        saveDocument();
        mockMvc.perform(get("/service/metadata")
                .param("userName", userName)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userName", is(userName)))
                .andExpect(jsonPath("$[0].documentName", is(documentName)))
                .andExpect(jsonPath("$[0].localization", is(localization)))
                .andExpect(jsonPath("$[0].date").exists());
    }

    @Test
    public void findMetadataDocuments_ByLocalization_ShouldReturnMetadata() throws Exception {
        saveDocument();
        mockMvc.perform(get("/service/metadata")
                .param("localization", localization)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userName", is(userName)))
                .andExpect(jsonPath("$[0].documentName", is(documentName)))
                .andExpect(jsonPath("$[0].localization", is(localization)))
                .andExpect(jsonPath("$[0].date").exists());
    }

    @Test
    public void findMetadataDocuments_WithoutParams_ShouldReturnMetadata() throws Exception {
        saveDocument();
        mockMvc.perform(get("/service/metadata")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userName", is(userName)))
                .andExpect(jsonPath("$[0].documentName", is(documentName)))
                .andExpect(jsonPath("$[0].localization", is(localization)))
                .andExpect(jsonPath("$[0].date").exists());
    }

    @Test
    public void findDocumentIds_AllParams_ShouldReturnListOfIds() throws Exception {
        saveDocument();
        mockMvc.perform(get("/service/documents")
                .param("userName", userName)
                .param("localization", localization)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void findDocumentIds_ByUserName_ShouldReturnListOfIds() throws Exception {
        saveDocument();
        mockMvc.perform(get("/service/documents")
                .param("userName", userName)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void findDocumentIds_ByLocalization_ShouldReturnListOfIds() throws Exception {
        saveDocument();
        mockMvc.perform(get("/service/documents")
                .param("localization", localization)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void findDocumentIds_WithoutParams_ShouldReturnListOfIds() throws Exception {
        saveDocument();
        mockMvc.perform(get("/service/documents")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void findDocumentIds_InvalidParams_ShouldReturnNotFoundException() throws Exception {
        saveDocument();
        mockMvc.perform(get("/service/documents")
                .param("userName", "InvalidParameter")
                .param("localization", "InvalidParameter")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void findDocumentIds_InvalidUserName_ShouldReturnNotFoundException() throws Exception {
        saveDocument();
        mockMvc.perform(get("/service/documents")
                .param("userName", "InvalidParameter")
                .param("localization", localization)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void findDocumentIds_InvalidLocalization_ShouldReturnNotFoundException() throws Exception {
        saveDocument();
        mockMvc.perform(get("/service/documents")
                .param("userName", userName)
                .param("localization", "InvalidParameter")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getDocument_ById_ShouldReturnDocument() throws Exception {
        MetadataDocument metadataDocument = saveDocument();
        mockMvc.perform(get("/service/documents/" + metadataDocument.getId())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(body)));
    }

    @Test
    public void getDocument_ByInvalidId_ShouldReturnNotFound() throws Exception {
        MetadataDocument metadataDocument = saveDocument();
        mockMvc.perform(get("/service/documents/" + "InvalidParameter")).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Document is not found!")));
    }

    private MockMultipartFile buildMockMultipartFile(){
        final byte[] content = body.getBytes();
        return new MockMultipartFile("file", documentName, "text/plain", content);
    }

    private MetadataDocument saveDocument() throws IOException {
        Date uploadDate = new Date();
        final byte[] content = body.getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("file", documentName, "text/plain", content);
        return documentService.saveDocument(mockFile, userName, localization, uploadDate);
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
