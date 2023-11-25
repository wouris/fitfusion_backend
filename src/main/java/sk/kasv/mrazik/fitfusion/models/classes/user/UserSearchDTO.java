package sk.kasv.mrazik.fitfusion.models.classes.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.kasv.mrazik.fitfusion.models.enums.Role;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDTO {
    private UUID id;
    private String username;
    private Role role;
}
