package ru.cll.search;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * author: Nikita
 * since: 16.05.2014
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class DocumentControllerTests {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    final List<String> documentIdHolder = new ArrayList<String>(0);
    public static final String testText = "Абсолютно небольшой русский текст, который нужно сохранить в базу.";

    @Test
    public void shouldSaveDocumentAndReturnId() throws Exception {
        mockMvc.perform(
                post("/postDocument")
                        .param("text", testText)
        )
                .andExpect(status().isCreated())
                .andExpect(request().attribute("documentId", new Matcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return o != null;
                    }

                    @Override
                    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {}
                    @Override
                    public void describeTo(Description description) {}
                }))
                .andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult result) throws Exception {
                        documentIdHolder.add(String.valueOf(result.getRequest().getAttribute("documentId")));
                    }
                });
        assert(!documentIdHolder.isEmpty());
    }

    @Test
    public void emptyDocumentShouldntBeHandled() throws Exception {
        mockMvc.perform(
                post("/postDocument").param("text", "")
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetDocumentById() throws Exception {
        assert(!documentIdHolder.isEmpty());
        mockMvc.perform(
                get("/getDocumentPlainText")
                        .param("id", documentIdHolder.get(0))
        )
                .andExpect(status().isOk())
                .andExpect(request().attribute("documentText", testText));
    }
}