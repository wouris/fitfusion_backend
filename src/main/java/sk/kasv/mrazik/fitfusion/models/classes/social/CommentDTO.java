package sk.kasv.mrazik.fitfusion.models.classes.social;

import java.util.UUID;

public record CommentDTO(UUID id, UUID postId, String username, String content) {
}
