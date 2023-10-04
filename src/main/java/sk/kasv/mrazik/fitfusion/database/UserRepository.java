package sk.kasv.mrazik.fitfusion.database;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sk.kasv.mrazik.fitfusion.models.classes.user.SocialInfo;
import sk.kasv.mrazik.fitfusion.models.classes.user.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);

    User findByUsernameOrEmail(String username, String email);

}