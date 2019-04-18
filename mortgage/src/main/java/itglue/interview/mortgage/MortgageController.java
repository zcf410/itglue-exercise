package itglue.interview.mortgage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MortgageController {

    @Autowired
    MortgageService mortgageService;

    @GetMapping(path = "/payment-amount")
    public MortgageResponse paymentAmount(@RequestParam Float askingPrice,
                                          @RequestParam Float downPayment,
                                          @RequestParam String schedule,
                                          @RequestParam int amortizationInYears){
        MortgageResponse mortgageResponse = new MortgageResponse();
        MortgageRequest mortgageRequest = null;
        try {
            mortgageRequest = new MortgageRequest.MortgageRequestBuilder()
                    .withAskingPrice(askingPrice)
                    .withDownPayment(downPayment, askingPrice)
                    .withSchedule(schedule)
                    .withAmortizationInYears(amortizationInYears)
                    .build();
        }catch (RequestInvalidException e){
            mortgageResponse.setSuccessful(false);
            mortgageResponse.setReason(e.getMessage());
            return  mortgageResponse;
        }

        mortgageResponse.setSuccessful(true);
        mortgageResponse.setPaymentAmount(String.format("%.2f", mortgageService.getPaymentAmount(mortgageRequest)));
        return mortgageResponse;
    }

    @GetMapping(path = "/mortgage-amount")
    public MortgageResponse mortgageAmount(@RequestParam Float paymentAmount,
                                          @RequestParam(required = false) Float downPayment,
                                          @RequestParam String schedule,
                                          @RequestParam int amortizationInYears){
        MortgageResponse mortgageResponse = new MortgageResponse();

        MortgageRequest mortgageRequest = null;
        try {
            mortgageRequest = new MortgageRequest.MortgageRequestBuilder()
                    .withPaymentAmount(paymentAmount)
                    .withDownPayment(downPayment)
                    .withSchedule(schedule)
                    .withAmortizationInYears(amortizationInYears)
                    .build();
        }catch (RequestInvalidException e){
            mortgageResponse.setSuccessful(false);
            mortgageResponse.setReason(e.getMessage());
            return  mortgageResponse;
        }
        mortgageResponse.setSuccessful(true);
        mortgageResponse.setMortgageAmount(String.format("%.2f", mortgageService.getMaxMortgage(mortgageRequest)));
        return mortgageResponse;
    }

    @RequestMapping(value = "/interest-rate", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public MortgageResponse interestRate(@RequestBody MortgageConfig mortgageConfig){
        MortgageResponse mortgageResponse = new MortgageResponse();
        MortgageRequest mortgageRequest = null;
        if (Float.valueOf(mortgageConfig.getEffectiveInterestRate())<=0){
            mortgageResponse.setSuccessful(false);
            return mortgageResponse;
        }

        mortgageResponse.setOldInterestRate(mortgageService.setInterestRate(Float.valueOf(mortgageConfig.getEffectiveInterestRate())));
        mortgageResponse.setSuccessful(true);
        mortgageResponse.setNewInterestRate(mortgageService.getInterestRate());
        return  mortgageResponse;
    }

}

class Validator<MortGageResponse>{
    MortgageResponse mortgageResponse;
    List<Consumer> validators = new ArrayList<>();


    void add(Consumer c){
        validators.add(c);
    }

    void validate(){
        for (Consumer c:validators){
            c.accept(mortgageResponse);
        }
    }
}
