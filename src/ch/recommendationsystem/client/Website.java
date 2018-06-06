package ch.recommendationsystem.client;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.URL;

@Entity
@XmlRootElement
public class Website implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_website")
	private Long id;

	@URL
	@Column(nullable = false, unique = true)
	private String url;
	// @JsonIgnore
	@Column(nullable = false, unique = true)
	private String content;

	private Collection<Resource> resources = new HashSet<Resource>();

	public Website(Long id, String url) {
		this.id = id;
		this.url = url;

	}

	public Website(Long id) {
		this.id = id;
	}

	public Website(String url) {
		this.url = url;
	}

	public Website() {
	}

	public Website(String content, String url) {
		this.content = content;
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public Long getId() {
		return id;
	}

	public void setContent(String content) {
		this.content = content.substring(0, 512);
	}

	public void setId(Long id) {
		this.id = id;

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (!url.matches("(http|ftp|ldap|file).*"))

			this.url = "http://" + url;
		else
			this.url = url;
	}

	public Collection<Resource> getResources() {
		return resources;
	}

	public void setResources(Collection<Resource> Resources) {
		this.resources = Resources;
	}

}
