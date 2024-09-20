package com.codenal.employee.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Departments {
	
	@Id
	@Column(name = "dept_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deptNo;
	
	@Column(name = "dept_name")
	private String deptName;
	
	@Column(name = "dept_create_date")
	private LocalDate deptCreateDate;

}
