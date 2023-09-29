package sk.kasv.mrazik.fitfusion.database;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import sk.kasv.mrazik.fitfusion.models.social.Post;

public interface PostRepository extends MongoRepository<Post, ObjectId>{
    
}
