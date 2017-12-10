package PhilosophyPath;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhilosophyPathRepository extends MongoRepository<PhilosophyPath, String> {

}