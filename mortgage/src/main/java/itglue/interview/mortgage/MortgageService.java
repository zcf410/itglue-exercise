package itglue.interview.mortgage;

import static java.lang.Math.pow;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

@Service
public class MortgageService {

    static volatile AtomicReference<Float> presetInterestRate = new AtomicReference<>(0.025f);


    public float getPaymentAmount(MortgageRequest mortgageRequest){
        float askPrice = mortgageRequest.getAskingPrice(), downPayment=mortgageRequest.getDownPayment();
        PaymentSchedule paymentSchedule = mortgageRequest.getSchedule();
        int amortyears = mortgageRequest.getAmortizationInYears();
        float principal = getInsuranceCost(askPrice, downPayment) + askPrice;
        float interestRate = presetInterestRate.get();
        double payment = principal * (interestRate * pow(1+interestRate, amortyears))/(pow(1+interestRate, amortyears)-1);
        float divideBy = getPayTimes(paymentSchedule);
        return (float)payment/divideBy;
    }

    //minium down payment rule is not taken into consideration here since downpayment is optional
    public float getMaxMortgage(MortgageRequest mortgageRequest){
        float downPayment=mortgageRequest.getDownPayment();
        PaymentSchedule paymentSchedule = mortgageRequest.getSchedule();
        int amortyears = mortgageRequest.getAmortizationInYears();

        float multiplyBy = getPayTimes(paymentSchedule);
        float interestRate = presetInterestRate.get();
        double loan = mortgageRequest.getPaymentAmount()*multiplyBy/(interestRate * pow(1+interestRate, amortyears))/(pow(1+interestRate, amortyears)-1);
        loan -= getInsuranceCost((float)loan, downPayment);
        return (float)loan - downPayment;
    }

    private int getPayTimes(PaymentSchedule paymentSchedule){
        int divideBy = 1;
        switch (paymentSchedule){
            case WEEKLY:
                divideBy=54;
                break;
            case MONTHLY:
                divideBy=12;
                break;
            case BIWEEKLY:
                divideBy=27;
                break;
            default:
                divideBy=12;
        }
        return divideBy;
    }

    private float getInsuranceCost(float askPrice, float downPayment) {
        float morgageAmount = askPrice - downPayment;
        if (morgageAmount > 1000000) return 0;
        float downRatio = downPayment/askPrice, insuranceRatio = 0f;
        if (downRatio>=0.05 && downRatio>=0.0999){
            insuranceRatio = 0.0315f;
        }else
        if (downRatio>=0.1 && downRatio>=0.1499){
            insuranceRatio = 0.024f;
        }else
        if (downRatio>=0.15 && downRatio>=0.1999){
            insuranceRatio = 0.018f;
        }

        return morgageAmount*insuranceRatio;

    }

    public String setInterestRate(Float effectiveInterestRate) {
        Float oldRate= presetInterestRate.get();
        presetInterestRate.set(effectiveInterestRate);
        return String.format("%.4f",oldRate);
    }

    public String getInterestRate(){
        return String.format("%.4f", presetInterestRate.get());
    }
}


