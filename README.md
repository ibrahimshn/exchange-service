<br />
<p align="center">
  
<h1 align="center">Exchange Service</h1>

  <p align="center">
    This application aims to enable users to perform exchange rate and conversion transactions and to query these conversions.
  </p>
</p>

<!-- ABOUT THE PROJECT -->
## About The Project

A simple foreign exchange application which is one of the most
common services used in financial applications. Requirements are as follows:

1. Exchange Rate API
   *  input: source currency, target currency list
   *  output: exchange rate list
2. Exchange API:
   *  input: source amount, source currency, target currency list
   *  output: amount list in target currencies and transaction id.
3. Exchange List API
   *  input: transaction id or conversion date range (e.g. start date and end date)
   i. only one of the inputs shall be provided for each call
   *  output: list of conversions filtered by the inputs
   
## Tech Stack
    • Java 17
    • Spring Boot 2.7.5
    • H2 Database
    • Lombok
    • Swagger
    
 **Additional Information:**

* []()Added Global Exception Handler
* []()Unit tests for business requirements written using the TDD
* []() Provides swagger api docs. You can reach it after successfully run the application.
`The URL`: http://localhost:8080/swagger-ui/index.html

<!-- USAGE EXAMPLES -->
## Usage

You can make request by import postman collections where is `exchange-service.postman_collection.json`

