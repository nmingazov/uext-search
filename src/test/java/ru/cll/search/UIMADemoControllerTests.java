package ru.cll.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class UIMADemoControllerTests {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void aboutInitialText() throws Exception {
        mockMvc.perform(
                    get("/getDirtyAnnotations")
                        .param("text", "HaddaWay")
                )
                .andExpect(status().isOk())
                .andExpect(request().attribute("initialText", "HaddaWay"));
    }

    @Test
    public void aboutXmlView() throws Exception {
        mockMvc.perform(
                get("/getXmi")
                        .param("text", "Немного русского языка")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType("application", "xml")));
    }
}
