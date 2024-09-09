package com.codenal.announce.domain;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.Jobs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="announce_read_authority")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class AnnounceReadAuthority {
	
	@Id
    private Long id;
	
	@MapsId
	@ManyToOne
	@JoinColumn(name="announce_no")
	private Announce announce;
	
	@ManyToOne
	@JoinColumn(name="dept_no")
	private Departments department;

	@ManyToOne
	@JoinColumn(name="job_no")
	private Jobs job;
}
