package dev.enkay.student_service.dto.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoicesWrapper {

  @JsonProperty("_embedded")
  private Embedded embedded;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Embedded {
    @JsonProperty("invoiceList")
    private List<InvoiceResponse> invoiceList;
  }
}