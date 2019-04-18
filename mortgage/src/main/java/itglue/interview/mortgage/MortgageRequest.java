package itglue.interview.mortgage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.web.bind.annotation.RequestParam;

public class MortgageRequest {
    
     private Float askingPrice;
     private Float downPayment=0f;
     private PaymentSchedule schedule;
     private int amortizationInYears;
     private Float paymentAmount;

     public Float getAskingPrice() {
          return askingPrice;
     }

     public void setAskingPrice(Float askingPrice) {
          this.askingPrice = askingPrice;
     }

     public Float getDownPayment() {
          return downPayment;
     }

     public void setDownPayment(Float downPayment) {
          this.downPayment = downPayment;
     }

     public PaymentSchedule getSchedule() {
          return schedule;
     }

     public void setSchedule(PaymentSchedule schedule) {
          this.schedule = schedule;
     }

     public int getAmortizationInYears() {
          return amortizationInYears;
     }

     public void setAmortizationInYears(int amortizationInYears) {
          this.amortizationInYears = amortizationInYears;
     }

     public Float getPaymentAmount() {
          return paymentAmount;
     }

     public void setPaymentAmount(Float paymentAmount) {
          this.paymentAmount = paymentAmount;
     }

     static class MortgageRequestBuilder{

     List<Consumer<MortgageRequest>> operations = new ArrayList<>();

          MortgageRequestBuilder withAskingPrice(Float askingPrice) throws RequestInvalidException{
               if (askingPrice <=0 ){
                    throw new RequestInvalidException("Invalid asking price");

               }
               operations.add(m -> m.askingPrice = askingPrice);
               return this;
          }

          MortgageRequest build(){
               MortgageRequest mortgageRequest = new MortgageRequest();
               for(Consumer c:operations){
                    c.accept(mortgageRequest);
               }
               return mortgageRequest;
          }

          MortgageRequestBuilder withAmortizationInYears(int amortizationInYears) throws RequestInvalidException{
               if (amortizationInYears<5 || amortizationInYears>25)
                    throw new RequestInvalidException("Invalid amortization");

               operations.add(m -> m.amortizationInYears = amortizationInYears);
               return this;
          }

          MortgageRequestBuilder withDownPayment(float downPayment, float askingPrice) throws RequestInvalidException {
               float mortgage = askingPrice - downPayment;
               float leastDownPayment = ( mortgage - 500000f ) * 0.1f + 25000f;
               if  (downPayment < leastDownPayment)
                    throw new RequestInvalidException("Invalid downpayment");

               operations.add(m -> m.downPayment = downPayment);
               return this;
          }

          MortgageRequestBuilder withDownPayment(float downPayment) throws RequestInvalidException {
               operations.add(m -> m.downPayment = downPayment);
               return this;
          }

          MortgageRequestBuilder withSchedule(String schedule) throws RequestInvalidException {

               operations.add(m -> m.schedule = PaymentSchedule.valueOf(schedule.toUpperCase()));
               return this;
          }

          MortgageRequestBuilder withPaymentAmount(float paymentAmount){
               operations.add(m -> m.paymentAmount = paymentAmount);
               return this;
          }

     }


}
