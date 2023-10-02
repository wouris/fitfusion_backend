-- Create the trigger to delete related records
DROP TRIGGER IF EXISTS DeleteRelatedRecords;
DELIMITER //
CREATE TRIGGER DeleteRelatedRecords
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
//
DELIMITER ;