package sk.kasv.mrazik.fitfusion.database;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.kasv.mrazik.fitfusion.models.user.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
}