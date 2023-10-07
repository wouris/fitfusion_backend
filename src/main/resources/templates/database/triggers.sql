-- Create the trigger to delete related records
DROP TRIGGER IF EXISTS UserDelete;
DROP TRIGGER IF EXISTS PostDelete;
DELIMITER //
CREATE TRIGGER UserDelete
    BEFORE DELETE
    ON users
    FOR EACH ROW
BEGIN
    -- Delete related records from 'account_info'
    DELETE FROM account_info WHERE account_info.user_id = OLD.id;

    -- Delete related records from 'workouts'
    DELETE FROM workouts WHERE workouts.user_id = OLD.id;

    -- Delete related records from 'followers' (both from and to user)
    DELETE FROM followers WHERE followers.user_id = OLD.id OR followers.follower_id = OLD.id;

    -- Delete related records from 'following' (both user and whoUserID)
    DELETE FROM following WHERE following.user_id = OLD.id OR following.following_id = OLD.id;

    -- Delete related records from 'posts'
    DELETE FROM posts WHERE posts.user_id = OLD.id;
END;

CREATE TRIGGER PostDelete
    BEFORE DELETE
    ON posts
    FOR EACH ROW
BEGIN
    -- Delete related records from 'comments'
    DELETE FROM comments WHERE comments.post_id = OLD.id;

    -- Delete related records from 'likes'
    DELETE FROM likes WHERE likes.post_id = OLD.id;
END;
//
DELIMITER ;