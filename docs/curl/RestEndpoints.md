### Version
    curl -X GET --location "http://localhost:8090/api/version" \
    -H "Accept: application/json"
### Bank Account
#### get one
    curl -X GET --location "http://localhost:8090/api/account/1" \
    -H "Accept: application/json" | json_pp
#### get all cards
    curl -X GET --location "http://localhost:8090/api/account/1/cards" \
    -H "Accept: application/json" | json_pp
#### create
    curl -X POST --location "http://localhost:8090/api/account" \
    -H "Content-Type: application/json" \
    -d "{
        \"customer\": \"Sidor Sidorov\"
    }" | json_pp
#### update
    curl -X PUT --location "http://localhost:8090/api/account" \
    -H "Content-Type: application/json" \
    -d "{
        \"id\": 1,
        \"customer\": \"Ivan Ivanoff\"
    }" | json_pp
#### delete
    curl -X DELETE --location "http://localhost:8090/api/account/1" \
    -H "Content-Type: application/json" | json_pp
#### get all
    curl -X GET --location "http://localhost:8090/api/accounts" \
    -H "Accept: application/json" | json_pp
#### get all by filter
##### case 1
     curl -X POST --location "http://localhost:8090/api/accounts" \
    -H "Content-Type: application/json" \
    -d "{
          \"numberPattern\": \"BY 99T6\",
          \"customerPattern\": \"an ov\"
        }" | json_pp
##### case 2    
    curl -X POST --location "http://localhost:8090/api/accounts" \
    -H "Content-Type: application/json" \
    -d "{
          \"numberPattern\": \"BY\"
        }" | json_pp
##### case 3  
    curl -X POST --location "http://localhost:8090/api/accounts" \
    -H "Content-Type: application/json" \
    -d "{
          \"customerPattern\": \"ov\"
        }" | json_pp
### Credit Card
#### get one
    curl -X GET --location "http://localhost:8090/api/card/1" \
    -H "Accept: application/json" | json_pp
#### create
    curl -X POST --location "http://localhost:8090/api/card" \
    -H "Content-Type: application/json" \
    -d "1" | json_pp
#### delete
    curl -X DELETE --location "http://localhost:8090/api/card/1" \
    -H "Accept: application/json" | json_pp
#### deposit money
    curl -X POST --location "http://localhost:8090/api/card/deposit" \
    -H "Content-Type: application/json" \
    -d "{
          \"targetCardNumber\": \"4000003394112581\",
          \"valueSumOfMoney\": \"1000,55\",
          \"locale\": \"ru\"
        }" | json_pp
#### transfer money
    curl -X POST --location "http://localhost:8090/api/card/transfer" \
    -H "Content-Type: application/json" \
    -d "{
          \"sourceCardNumber\": \"4000003394112581\",
          \"targetCardNumber\": \"4000002538269224\",
          \"valueSumOfMoney\": \"1000,6\",
          \"locale\": \"ru\"
        }" | json_pp
#### get all
    curl -X GET --location "http://localhost:8090/api/cards" \
    -H "Accept: application/json" | json_pp
#### get all by filter
##### case 1
    curl -X POST --location "http://localhost:8090/api/cards" \
    -H "Content-Type: application/json" \
    -d "{
          \"valueFromDate\": \"06/2022\",
          \"valueToDate\": \"08/2023\"
        }" | json_pp
##### case 2 
    curl -X POST --location "http://localhost:8090/api/cards" \
    -H "Content-Type: application/json" \
    -d "{
          \"valueFromDate\": \"07/2023\"
        }" | json_pp
##### case 3
    curl -X POST --location "http://localhost:8090/api/cards" \
    -H "Content-Type: application/json" \
    -d "{
          \"valueToDate\": \"07/2023\"
        }" | json_pp