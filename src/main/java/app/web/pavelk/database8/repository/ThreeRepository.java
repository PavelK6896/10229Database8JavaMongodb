package app.web.pavelk.database8.repository;


import app.web.pavelk.database8.schema.Three;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThreeRepository extends MongoRepository<Three, String> {

}
