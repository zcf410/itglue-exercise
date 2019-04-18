package itglue.interview.mortgage;

import org.springframework.stereotype.Component;

@Component
public class MortgageResponse {
    private boolean successful;
    private String reason;
    private String paymentAmount;
    private String mortgageAmount;
    private String oldInterestRate;
    private String newInterestRate;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getMortgageAmount() {
        return mortgageAmount;
    }

    public void setMortgageAmount(String mortgageAmount) {
        this.mortgageAmount = mortgageAmount;
    }

    public String getOldInterestRate() {
        return oldInterestRate;
    }

    public void setOldInterestRate(String oldInterestRate) {
        this.oldInterestRate = oldInterestRate;
    }

    public String getNewInterestRate() {
        return newInterestRate;
    }

    public void setNewInterestRate(String newInterestRate) {
        this.newInterestRate = newInterestRate;
    }
}
