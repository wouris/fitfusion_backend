package sk.kasv.mrazik.fitfusion.database;

import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;

import sk.kasv.mrazik.fitfusion.models.social.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID>{
    
}