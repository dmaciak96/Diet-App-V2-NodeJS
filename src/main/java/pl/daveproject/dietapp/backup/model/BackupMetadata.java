package pl.daveproject.dietapp.backup.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import pl.daveproject.dietapp.security.model.ApplicationUser;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BackupMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreatedDate
    private Instant creationDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private int version;

    @Lob
    @JdbcType(VarbinaryJdbcType.class)
    private byte[] products;

    @Lob
    @JdbcType(VarbinaryJdbcType.class)
    private byte[] recipes;

    @Lob
    @JdbcType(VarbinaryJdbcType.class)
    private byte[] shoppingLists;

    @Lob
    @JdbcType(VarbinaryJdbcType.class)
    private byte[] bmi;

    @Lob
    @JdbcType(VarbinaryJdbcType.class)
    private byte[] caloricNeeds;

    @ManyToOne(cascade = CascadeType.MERGE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ApplicationUser applicationUser;
}
