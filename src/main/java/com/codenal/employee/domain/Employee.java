

package com.codenal.employee.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
import lombok.Setter;

@Entity
@Table(name="employee")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Setter
public class Employee {

    @Id
    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "emp_pw")
    private String empPw;

    @Column(name = "emp_name")
    private String empName;

    @Column(name = "emp_ssn")
    private String empSsn;

    @Column(name = "emp_phone")
    private String empPhone;

    @Column(name = "emp_address")
    private String empAddress;

    @Column(name = "emp_address_detail")
    private String empAddressDetail;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "emp_hire")
    private LocalDate empHire;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "emp_end")
    private LocalDate empEnd;

    @Column(name = "emp_status")
    private String empStatus;

    @Column(name = "emp_profile_picture")
    private String empProfilePicture;

    @Column(name = "emp_sign_image")
    private String empSignImage;

    @Column(name = "dept_no")
    private Integer deptNo;

    @Column(name = "job_no")
    private Integer jobNo;

    @Column(name = "emp_auth")
    private String empAuth;
}

