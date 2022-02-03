package app.web.pavelk.database8;


import app.web.pavelk.database8.schema.One;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureDataMongo
//@DataMongoTest // or
//@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EmbeddedMongodbTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test1() {
        DBObject dbObject = BasicDBObjectBuilder.start()
                .add("key", "value").get();
        mongoTemplate.save(dbObject, "collection");

        Assertions.assertThat(mongoTemplate.findAll(DBObject.class, "collection"))
                .extracting("key").containsOnly("value");
    }

    @Test
    void test2() throws Exception {

        One one1 = One.builder().name("name-1").build();
        One one2 = One.builder().name("name-2").build();
        mongoTemplate.save(one1, "One");
        mongoTemplate.save(one2, "One");

        mockMvc.perform(MockMvcRequestBuilders.get("/ones"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$._embedded.ones", hasSize(2)))
                .andExpect(jsonPath("$._embedded.ones[0].name", CoreMatchers.is("name-1")))
                .andExpect(jsonPath("$.page.size", CoreMatchers.is(20)));

    }

}
