package sk.kasv.mrazik.fitfusion.models.classes.social.comment;

import java.util.UUID;

public record CommentDTO(UUID id, UUID postId, String username, String content, int likes) {
}
