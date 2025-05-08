package dev.enkay.student_service.dto.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@JsonIgnoreProperties({"_links"})
public class InvoiceResponse {
    private Long   id;
    private String reference;
    private double amount;
    private String dueDate;
    private String type;
    private String status;
    private String studentId;
}