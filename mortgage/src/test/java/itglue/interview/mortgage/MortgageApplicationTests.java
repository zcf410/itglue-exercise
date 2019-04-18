package itglue.interview.mortgage;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import javafx.application.Application;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MortgageApplicationTests {
	@Autowired
	MortgageController controller;
	@Autowired
	private MockMvc mvc;

	@Test
	public void contexLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void testGetPaymentAmount() throws Exception,RequestInvalidException{

		MortgageRequest mortgageRequest = null;
		float paymentAmount = 10000, downPayment = 20000;
		String schedule= "MONTHLY";
		int amortizationInYears = 20;

		mortgageRequest = new MortgageRequest.MortgageRequestBuilder()
					.withPaymentAmount(paymentAmount)
					.withDownPayment(downPayment)
					.withSchedule(schedule)
					.withAmortizationInYears(amortizationInYears)
					.build();

		mvc.perform(get("/payment-amount")
				.param("askingPrice","1000000")
				.param("downPayment","200000")
				.param("schedule", "MONTHLY")
				.param("amortizationInYears","20")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("paymentAmount").value("5480.31"));
		}

	//negative test when down payment is less than minium
	@Test
	public void testGetPaymentAmountNagative() throws Exception,RequestInvalidException{

		mvc.perform(get("/payment-amount")
				.param("askingPrice","1000000")
				.param("downPayment","20000")
				.param("schedule", "MONTHLY")
				.param("amortizationInYears","20")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("successful").value("false"))
				.andExpect(jsonPath("reason").value("Invalid downpayment"));
	}

	@Test
	public void testGetMortgageAmount() throws Exception,RequestInvalidException{


		mvc.perform(get("/mortgage-amount")
				.param("paymentAmount","2500")
				.param("downPayment","200000")
				.param("schedule", "MONTHLY")
				.param("amortizationInYears","20")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("mortgageAmount").value("916916.63"));
	}

	@Test
	public void testSetInterestRate() throws Exception,RequestInvalidException{

		mvc.perform(patch("/interest-rate")
				.content("{\"effectiveInterestRate\":0.03}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("newInterestRate").value("0.0300"))
				.andExpect(jsonPath("oldInterestRate").value("0.0250"));
		mvc.perform(patch("/interest-rate")
				.content("{\"effectiveInterestRate\":0.025}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("newInterestRate").value("0.0250"))
				.andExpect(jsonPath("oldInterestRate").value("0.0300"));
	}
}
