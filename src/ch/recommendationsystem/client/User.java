package ch.recommendationsystem.client;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement

@Table(name = "user")

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user")
	private String id;
	@NotNull(message = "Veuillez saisir un nom d'utilisateur")
	@Size(min = 3, message = "Le nom d'utilisateur doit contenir au moins 3 caractères")
	private String name;
	private String forename;
	@NotNull(message = "Veuillez saisir une adresse email")
	@Pattern(regexp = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)", message = "Merci de saisir une adresse mail valide")
	@Column(nullable = false, unique = true)
	private String email;
	// @NotNull(message = "Veuillez saisir un mot de passe")
	// @Pattern(regexp = ".*(?=.{8,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*", message =
	// "Le mot de passe saisi n'est pas assez sécurisé")
	private String password;
	// @Column(length = 36)
	private String phone;
	private String message;

	private boolean active;
	@Column(name = "creation_date")
	private Timestamp creationDate;

	@Column(name = "modification_date")
	private Timestamp modificationDate;

	// private Address address;
	// @ManyToMany(mappedBy = "users", cascade = { CascadeType.PERSIST,
	// CascadeType.MERGE }, fetch = FetchType.EAGER)
	// @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	// @JsonBackReference
	// @JsonIgnore
	// @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval =
	// true)
	// private Collection<ManifestationUser> manifestations = new
	// HashSet<ManifestationUser>();

	public User(String id) {
		this.id = id;
	}

	public User(String id, String name, String forename, String email, boolean active, Timestamp creationDate,
			Timestamp modificationDate, String phone, String message, String image) {

		this.id = id;

		this.name = name;
		this.forename = forename;

		this.email = email;
		this.active = active;
		this.phone = phone;
		this.message = message;

		this.creationDate = creationDate;
		this.modificationDate = modificationDate;

		// this.address = address;

	}

	public User(String name, String forename) {
		this.name = name;
		this.forename = forename;
	}

	public User(String email, String forename, String name) {
		this.email = email;
		this.forename = forename;
		this.name = name;

	}

	public User() {
	}

	public User(String id, String email, String name, String forename) {
		this.id = id;

		this.name = name;
		this.forename = forename;
	}

	public String getName() {
		return name;
	}

	public String getForename() {
		return forename;
	}

	public String getId() {
		return id;
	}

	public Timestamp getModificationDate() {
		return modificationDate;
	}

	public boolean isActive() {
		return active;
	}

	public String getEmail() {
		return email;
	}

	// public Address getAddress() {
	// return address;
	// }

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setModificationDate(Timestamp modificationDate) {
		this.modificationDate = modificationDate;
	}

	// public void setAddress(Address address) {
	// this.address = address;
	// }

	public void setId(String id) {
		this.id = id;

	}

	public void setPassword(String password) {
		this.password = password;

	}

	public String getPhone() {
		return phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	// public Collection<ManifestationUser> getManifestations() {
	// return manifestations;
	// }
	//
	// public void setManifestations(Collection<ManifestationUser> manifestations) {
	// this.manifestations = manifestations;
	// }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User) o;
		return Objects.equals(email, user.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

}
