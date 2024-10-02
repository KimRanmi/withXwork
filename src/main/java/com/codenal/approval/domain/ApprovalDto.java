package com.codenal.approval.domain;

import java.time.LocalDate;

import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.employee.domain.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class ApprovalDto {
	
	private Long approval_no;
	private Long emp_id;
	private int cate_code;
	private LocalDate approval_reg_date;
	private String approval_title;
	private String approval_content;
	private int approval_status;
	private Long annual_leave_usage_no;
	
	public Approval toEntity() {
		return Approval.builder()
				.approvalNo(approval_no)
				.approvalTitle(approval_title)
				.approvalContent(approval_content)
				.approvalRegDate(approval_reg_date)
				.approvalStatus(approval_status)
				.build();
	}
	
	public ApprovalDto toDto(Approval approval) {
		return ApprovalDto.builder()
				.approval_no(approval.getApprovalNo())
				.approval_title(approval.getApprovalTitle())
				.approval_content(approval.getApprovalContent())
				.approval_reg_date(approval.getApprovalRegDate())
				.approval_status(approval.getApprovalStatus())
				.cate_code(approval.getApprovalCategory().getCateCode())
				.emp_id(approval.getEmployee().getEmpId())
				.annual_leave_usage_no(approval.getAnnualLeaveUsage().getAnnualUsageNo())
				.build();
	}
}