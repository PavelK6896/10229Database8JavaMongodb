package app.web.pavelk.database8;

import app.web.pavelk.database8.schema.One;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@DirtiesContext
//@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class) or
@AutoConfigureDataMongo
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TestcontainersMongodbTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0.5");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test1() throws Exception {
        List<One> ones = IntStream.range(1, 6)
                .mapToObj(i -> One.builder().name("n " + i).build())
                .collect(Collectors.toList());
        mongoTemplate.insert(ones, One.class);
        List<One> all = mongoTemplate.findAll(One.class);
        Assertions.assertEquals(5, all.size());

        mockMvc.perform(MockMvcRequestBuilders.get("/ones"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$._embedded.ones", hasSize(5)))
                .andExpect(jsonPath("$._embedded.ones[2].name", CoreMatchers.is("n 3")));

    }

}
