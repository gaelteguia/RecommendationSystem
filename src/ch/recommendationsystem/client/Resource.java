package ch.recommendationsystem.client;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.james.mime4j.dom.datetime.DateTime;
import org.eclipse.persistence.annotations.Convert;

import com.sun.jndi.cosnaming.IiopUrl.Address;

@Entity
@XmlRootElement
@Table(name = "resource")
// @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
// property = "@id")
public class Resource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_resource")
	private String id;
	@NotNull(message = "Veuillez saisir un titre")
	@Size(min = 3, message = "Le titre doit contenir au moins 3 caractères")
	@Column(nullable = false, unique = true)
	private String title;
	private String description;
	private String content = "";
	private String URL;
	private List<CardKeyword> keywords;
	// @Column(length = 36)
	private String phone;

	// @NotNull(message = "Veuillez saisir une adresse email")
	// @Pattern(regexp = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)", message =
	// "Merci de saisir une adresse mail valide")
	private String email;

	@Column(columnDefinition = "TIMESTAMP", name = "start_time")
	@Convert("dateTimeConverter")
	private DateTime startTime;

	@Column(columnDefinition = "TIMESTAMP", name = "end_time")
	@Convert("dateTimeConverter")
	private DateTime endTime;

	@Column(name = "creation_date")
	private Timestamp creationDate;

	@Column(name = "modification_date")
	private Timestamp modificationDate;
	private String image;
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "id_address")
	private Address address;
	private String organizer;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "id_website")
	private Website website;

	private String distance;
	private Double price;
	@ManyToOne
	@JoinColumn(name = "id_creator")
	private User creator;

	private Boolean validated;
	private Boolean activity;
	private String year;
	private String promotedLearning;
	private String basicExpectation;
	private String technicalRequirements;
	private String thematicAxis;

	private Boolean transverseCapacity;
	private String catalog;

	private String chapter;
	private String collection;
	private String generalComments;
	private String skill;
	private String complement;
	private Boolean cantonalComplement;
	private String component;

	private String termsOfUse;
	private String teachingContext;

	private String cost;

	private String trainingCurriculum;
	private String cycle;
	private String dated;
	@Column(columnDefinition = "TIMESTAMP", name = "start_time")
	@Convert("dateTimeConverter")
	private DateTime updateDate;
	@Column(columnDefinition = "TIMESTAMP", name = "start_time")
	@Convert("dateTimeConverter")
	private DateTime publicationDate;

	private String schoolLevel;
	private String degrees;

	private String semanticDensity;
	private String descriptor;

	private String generalDescription;
	private String educationalDescription;

	private String difficulty;

	private String discipline;
	private String disciplinaryField;
	private String copyright;
	@Column(columnDefinition = "TIMESTAMP", name = "start_time")
	@Convert("dateTimeConverter")
	private DateTime duration;
	@Column(columnDefinition = "TIMESTAMP", name = "start_time")
	@Convert("dateTimeConverter")
	private DateTime learningTime;

	private String editor;

	private String location;
	private String physicalLocation;

	private String entity;
	private String entrance;
	private String state;
	private String multipleRequirements;

	private String format;
	private String generalEducation;
	private String gd;
	private String fe;
	private String kind;
	private String hierarchy;
	private String href;

	private String login;

	private String embed;
	private String picture;
	private String teachingIndication;
	private String issn;
	private String languages;
	private String resourceLanguage;

	private String schoolingLanguage;

	private String lehrplan21;
	private String link;
	private String MERLink;
	private String necessaryMaterial;
	private String module;
	// private String keywords;
	private String teachingMedium;
	private String interactivityLevel;

	private String granularityLevel;

	private String name;
	private String numberOfPages;
	private String number;
	private String goal;
	private String learningObjective;
	private String odr;
	private String official;
	private String paging;
	private String specializedPedagogy;
	private String per;
	private String perimeter;
	private String periodicity;
	private String weighting;
	private String cantonalPrecision;

	private String progressionOfLearning;
	private String globalTrainingProject;
	private String postedInBSN;

	private String production;
	private String grouping;
	private String installationNotes;

	private String resources;
	private String relatedResources;
	private String rn;
	private String role;
	private String metadataSchema;
	private String learningSequence;
	private String source;
	private String subtitle;
	private String secondarySubtitle;
	private String subtypes;
	private String cantonalSpecificity;
	private String specificity;
	private String structure;
	private String remove;
	private String cut;
	private String fileSize;
	private String taxon;

	private String necessaryTime;

	private String thematic;
	private String theme;
	private String areaOfExpertise;
	private String type;
	private String interactivityType;

	private String resourceType;

	private String relationshipType;

	private String documentaryType;
	private String educationalType;
	private String urls;
	private String thumbnailURL;
	private String downloadURL;
	private String finalUser;
	private String version;
	private String maximumVersion;
	private String minimalVersion;
	private String vignette;

	private String prioritySights;

	// @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch =
	// FetchType.LAZY)
	// @JoinTable(name = "resource_user", joinColumns = {
	// @JoinColumn(name = "resources_id_resource") }, inverseJoinColumns =
	// {
	// @JoinColumn(name = "users_id_user") })
	// @JsonManagedReference
	// @MapKey(name = "email")
	// @JsonIgnore

	public Resource() {
	}

	public Resource(Long id) {
		this.id = Long.toString(id);
	}

	public Resource(String title) {
		this.title = title;
	}

	public Resource(String id, String title, Address address) {
		this.id = id;
		this.title = title;
		this.address = address;
	}

	public Resource(String id, String title, String URL) {
		this.id = id;
		this.title = title;
		this.setURL(URL);

	}

	public Resource(String id, String title, String description, Address address) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title.toUpperCase();
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public Website getWebsite() {
		return website;
	}

	// @JsonIgnore
	public void setWebsite(Website website) {
		this.website = website;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public void setModificationDate(Timestamp modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Resource m = (Resource) o;
		return Objects.equals(title, m.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title);
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	public Boolean getActivity() {
		return activity;
	}

	public void setActivity(Boolean activity) {
		this.activity = activity;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getPromotedLearning() {
		return promotedLearning;
	}

	public void setPromotedLearning(String promotedLearning) {
		this.promotedLearning = promotedLearning;
	}

	public String getBasicExpectation() {
		return basicExpectation;
	}

	public void setBasicExpectation(String basicExpectation) {
		this.basicExpectation = basicExpectation;
	}

	public String getTechnicalRequirements() {
		return technicalRequirements;
	}

	public void setTechnicalRequirements(String technicalRequirements) {
		this.technicalRequirements = technicalRequirements;
	}

	public String getThematicAxis() {
		return thematicAxis;
	}

	public void setThematicAxis(String thematicAxis) {
		this.thematicAxis = thematicAxis;
	}

	public Boolean getTransverseCapacity() {
		return transverseCapacity;
	}

	public void setTransverseCapacity(Boolean transverseCapacity) {
		this.transverseCapacity = transverseCapacity;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getGeneralComments() {
		return generalComments;
	}

	public void setGeneralComments(String generalComments) {
		this.generalComments = generalComments;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public List<CardKeyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<CardKeyword> keywords) {
		this.keywords = keywords;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
