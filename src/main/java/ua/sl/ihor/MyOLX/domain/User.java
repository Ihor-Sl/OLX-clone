package ua.sl.ihor.MyOLX.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usr")
@Where(clause = "is_deleted = false")
@Data
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"email"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @Size(min = 3, max = 100, message = "Min name length - 3, max username length - 100")
    @NotNull(message = "Name required")
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Email must be valid")
    @NotNull(message = "Email required")
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 8, message = "Min password length - 8")
    @NotNull(message = "Password required")
    private String password;

    @Column(name = "location")
    private String location;

    @Column(name = "is_online", nullable = false)
    private boolean isOnline;

    @Column(name = "last_visit")
    private LocalDateTime lastVisit;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Column(name = "is_account_non_locked", columnDefinition = "boolean not null default true")
    private boolean isAccountNonLocked;

    @Column(name = "is_deleted", columnDefinition = "boolean not null default false")
    private boolean isDeleted;

    // -------UserDetails-------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
