package com.targetlabs.rest.service;

import com.targetlabs.rest.config.Application;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 20.08.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class RestServiceFacadeTest {

    private static final Logger log = LoggerFactory.getLogger(RestServiceFacadeTest.class);

    private MockMvc mockMvc;

    @Autowired
    private DocumentService documentService;


}
