package sk.kasv.mrazik.fitfusion.database;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import sk.kasv.mrazik.fitfusion.models.user.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
}