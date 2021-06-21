# Coin Converter

This project has as main objective to evaluate the candidate Raphael for a position in the company Jaya. A challenge to convert one currency value to another, following the same quotation used by an external service.

Scope:   
 

> 
Develop a conversion API that has an access route for the user to pass his identification, informing the value and which quotation originates the destination of the conversion, saving each transaction carried out for a query in another route to list all transactions per user.


## How it works?

To convert a value, the CoinConverter application uses the third-party Api service hosted on: https://api.exchangeratesapi.io/

In its free version, it does not have the conversion command enabled, so the calculation must be done in the base currency according to the following formula: 
``` 
value_to = (value_from / rates_from) * rates_to
```
It is possible to execute the project by typing this command in the terminal
```
.\gradlew clean run
```
If you want to perform the tests, you can enter the following command
```
.\gradlew clean test
```

-> Access route (End Point) for currency conversion:
> /Transaction/convert/@value/@from/@to?UserId=@userId

-> Access route to list a user's transactions
> /Transaction?UserId?@userId

-> In case of failures a clear and easy to understand message will be displayed


1. Class diagram:
![class.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1622098256618/XmK2ooOGy.png)

2. Activity diagram:
![flow.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1622098244353/BiaXTjx0M.png)

-> Take a look into the regression:
![cover_regressao.PNG](https://cdn.hashnode.com/res/hashnode/image/upload/v1622097660058/UO78SsVwz.png)
