package dev.enkay.student_service.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Data
@AllArgsConstructor
@Builder
public class InvoiceRequest {
  private double amount;
  private String dueDate; 
  private InvoiceType type;
  private Account account;

  @Data
  @AllArgsConstructor

  public static class Account {
    private String studentId;
  }
}