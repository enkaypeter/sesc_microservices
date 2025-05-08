// FinanceClient.java
package dev.enkay.student_service.integration;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dev.enkay.student_service.dto.finance.*;

@Service
public class FinanceClient {
  private final WebClient webClient;

  public FinanceClient(WebClient financeWebClient) {
    this.webClient = financeWebClient;
  }

  public void registerStudent(String studentId) {
    CreateAccountRequest req = new CreateAccountRequest(studentId);
    webClient.post()
        .uri("/finance/accounts")
        .bodyValue(req)
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }

  public InvoiceResponse createInvoice(String studentId, double amount, String dueDate) {
    InvoiceRequest.Account account = new InvoiceRequest.Account(studentId);
    InvoiceRequest req = new InvoiceRequest(amount, dueDate, InvoiceType.TUITION_FEES, account);

    InvoiceResponse invoice = webClient.post()
        .uri("/finance/invoices")
        .bodyValue(req)
        .retrieve()
        .bodyToMono(InvoiceResponse.class)
        .block();

      return invoice;
  }

  public List<InvoiceResponse> fetchAllInvoices() {
    InvoicesWrapper wrapper = webClient.get()
        .uri("/finance/invoices")              // adjust per your gateway path
        .retrieve()
        .bodyToMono(InvoicesWrapper.class)
        .block();

    return wrapper != null && wrapper.getEmbedded() != null
        ? wrapper.getEmbedded().getInvoiceList()
        : List.of();
  }


  @Data
  @AllArgsConstructor
  static class CreateAccountRequest {
    private String studentId;
  }
}
