mortgage calculator APIs built with springboot and Java. 

GET /payment-amount
Get the recurring payment amount of a mortgage
Params:
Asking Price
Down Payment*
Payment schedule***
Amortization Period**
Return:
Payment amount per scheduled payment
Sample response message:
{"successful":true,"reason":null,"results":{"paymentAmount":null,"mortgageAmount":"916916.63","oldInterestRate":null,"newInterestRate":null}}


GET /mortgage-amount
Get the maximum mortgage amount
Params:
payment amount
Down Payment(optional)****
Payment schedule***
Amortization Period**
Return:
Maximum Mortgage that can be taken out. Sample response message:
{"successful":true,"reason":null,"results":{"paymentAmount":null,"mortgageAmount":"694793.31","oldInterestRate":null,"newInterestRate":null}}

PATCH /interest-rate
Change the interest rate used by the application
Params:
Interest Rate
Return:
message indicating the old and new interest rate
Sample response message:
{"successful":true,"reason":null,"results":{"paymentAmount":null,"mortgageAmount":null,"oldInterestRate":"0.0250","newInterestRate":"0.0900"}}
