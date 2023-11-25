package sk.kasv.mrazik.fitfusion.databases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sk.kasv.mrazik.fitfusion.models.classes.user.User;
import sk.kasv.mrazik.fitfusion.models.classes.user.UserSearchDTO;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    User findByUsernameOrEmail(String username, String email);

    @Query("SELECT new sk.kasv.mrazik.fitfusion.models.classes.user.UserSearchDTO(u.id, u.username, u.role) FROM users u")
    List<UserSearchDTO> getAllSearchDTOs();

}