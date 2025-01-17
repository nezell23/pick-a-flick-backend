package com.pickaflick.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

//Serialization in Java allows us to convert an Object to stream that we
//can send over the network or save it as file or store in DB for later usage.
@Entity
public class Tag implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tagId;
	@Column(nullable = false, updatable = false)
	private Long authorId;
	private String tagName;
	
//	Creates Many to Many relationship between Movies and Tags
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, mappedBy = "tags")
	private Set<Movie> movies = new HashSet<>();

	public Tag() {

	}
	
	public Tag(Long tagId, Long authorId, String tagName) {
		
		this.tagId = tagId;
		this.authorId = authorId;
		this.tagName = tagName;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	public Set<Movie> getMovies() {
		return movies;
	}
	
	public void setMovies(Set<Movie> movies) {
		this.movies = movies;
	}

	@Override
	public String toString() {
		return "Tag [tagId=" + tagId + ", authorId=" + authorId + ", tagName=" + tagName + "]";
	}

}
