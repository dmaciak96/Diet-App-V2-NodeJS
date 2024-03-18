package pl.daveproject.dietapp.security.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private boolean enabled;

    @Transient
    private String oldPassword;

    @Transient
    private String newPassword;

    @Version
    private Integer version;

    @Lob
    @JdbcType(VarbinaryJdbcType.class)
    private byte[] avatar;

    @OneToMany(mappedBy = "applicationUser", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private List<Authority> authorities = new ArrayList<>();

    public String getFullName() {
        return "%s %s".formatted(firstName, lastName);
    }
}
