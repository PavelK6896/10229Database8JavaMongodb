package app.web.pavelk.database8.repository;


import app.web.pavelk.database8.schema.One;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OneRepository extends MongoRepository<One, String> {

}
