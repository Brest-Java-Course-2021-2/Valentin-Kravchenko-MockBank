# Software Requirements Specification
## 1. Purpose
The purpose of this project is to build a simple web-application to work with *bank accounts and linked them credit cards*.  
## 2. Scope
The application allows users to:  
  * View a list of all bank accounts
  * Create a new bank account
  * Edit the selected bank account
  * Filter bank accounts by account number and customer full name 
  * Add a new credit card to the selected bank account
  * View a list of all credit cards
  * Filter credit cards by expiration date
  * Deposit money to the selected credit card
  * Transfer money to the selected credit card
## 3. Overall description
### 3.1 Bank Account Management Functions
### 3.1.1 View and Filter a List of Bank Accounts:
#### Basic Action: 
  * The user selects the **Accounts** tab
  * The system handles the request and displays a list of all bank accounts sorted by customer registration date 
    ![](/docs/jpeg/slide_1.jpeg)
  * The list of bank accounts includes the following columns:
    * International Bank Account Number
    * Customer Full Name
    * Date of the Customer Registration in the bank system
    * Total Number of Credit Cards linked with the bank account
    * Links to edit and remove an account, and a link to add a new credit card to the account
##### Filtering Action:
  * The user enters a search pattern into each input field or one of them
  * Valid format of the account number search pattern is FirstNumberPattern(required, up to 17 characters)\<space\>\SecondNumberPattern(optional, up to 17 characters).
    For example, 'BY', 'BY 99T6'
  * Allowed characters for the \[FirstNumberPattern\] and [SecondNumberPattern\] are \[A-Z0-9\]
  * Valid format of the customer search pattern is FirstNamePattern(up to 63 characters)\<space\>LastNamePattern(up to 64 characters).
    For example, 'Iv Iva', 'an ov', 'ov', 'Iv'
  * Allowed characters for the \[FirstNamePattern\] and [LastNamePattern\] are \[A-Za-z\]
  * Both or one of the patterns should be specified 
  * The user clicks the **APPLY** button
  * The system validates the given search patterns
  * In the case, the search patterns are valid, the system handles the request and displays a list of bank accounts matching
    the search patterns
    ![](/docs/jpeg/slide_2.jpeg)
  * In the case, there are no bank accounts matching the search patterns, the system displays an info message  
    "No data was found matching the given search pattern(s)"
    ![](/docs/jpeg/slide_3.jpeg)
  * In the case, the search patterns are invalid, the system displays warnings 
    "Number search pattern is incorrect!" and/or "Customer search pattern is incorrect!"
    ![](/docs/jpeg/slide_4.jpeg)
  * In the case, the user clicks the **RESET** button to call off the filter, the system displays a list of all bank accounts
  * The search pattern for the account number is entered in the field located above the Number column, and for the client 
    it is entered in the field located above the Client column
### 3.1.2 Create a New Bank Account
##### Basic Action:
  * The user clicks the **CREATE ACCOUNT** button
  * The system redirects the user to the page with a form for an account creating
    ![](/docs/jpeg/slide_5.jpeg)
##### Creating a bank account:
  * The system automatically sets a registration date corresponding to the current date
  * The user enters the customer full name
  * Valid customer full name format is \[FirstName\]{up to 63 characters}\<space\>\[LastName\]{up to 64 characters}
  (for example, Ivan Ivanov), \[FirstName\] and [LastName\] must start with a capital letter
  * Allowed characters in the \[FirstName\] and [LastName\] are \[A-Za-z\]
  * In the case, user clicks the **CREATE** button:
    * The system validates the customer full name
    * In the case, the customer full name is valid:
      * The system automatically generates a new international bank account number
      * The system inserts the above data in the database
      * The system redirect the user to the 'Accounts' page
      * The system displays an info message "New bank account {account number is indicated here} created successfully"
      * The system displays an updated list of all bank accounts
      ![](/docs/jpeg/slide_6.jpeg)
    * In the case, the customer full name is invalid, the system displays a warning "Customer full name is incorrect!"
      ![](/docs/jpeg/slide_7.jpeg)
##### Cancelling Create:
  * The user clicks the **CANCEL** button
  * The system redirects the user to the 'Accounts' page without creating a new bank account
### 3.1.3 Edit a Bank Account:
##### Basic Action:
  * The user clicks the **Edit** link in the 'Actions' column of the selected bank account
  * The system redirects the user to the page with a form for an account editing
    ![](/docs/jpeg/slide_8.jpeg)
##### Editing a bank account:
  * The user changes the customer full name
  * The user clicks the **SAVE** button:
  * The system validates the customer full name
    * In the case, the customer full name is valid:
      * The system updates this data in the database
      * The system redirects the user to the Accounts page
      * The system displays an info message "Bank account {account number is indicated here} updated successfully"
      * The system displays a list of all bank accounts with updated data
        ![](/docs/jpeg/slide_9.jpeg)
    * In the case, the customer full name is invalid, the system displays a warning "Customer full name is incorrect!"
##### Cancelling Edit:    
  * The user clicks the **CANCEL** button 
  * The system redirects the user to the 'Accounts' page without updating the selected bank account 
### 3.1.4 Remove a Bank Account:
##### Basic Action:
  * The user clicks the **Remove** link in the 'Actions' column of the selected bank account
  * The system displays dialog box to confirm account deletion
  * The dialog box indicates the number of the removed bank account
    ![](/docs/jpeg/slide_10.jpeg)
##### Confirm Deletion:
  * The user clicks the **Remove** button in the dialog box
  * The system checks the removed bank account for linked credit cards
  * In the case, the removed account is linked to credit cards, the system displays an error message
    "Credit cards {card numbers are indicated here} are linked to bank account {account number is indicated here}.
    It can't be removed!"
    ![](/docs/jpeg/slide_11.jpeg)
  * In the case, the removed account isn't linked with credit cards:
    * The system removes the selected bank account from the database
    * The system displays an info message "Bank account {account number is indicated here} removed successfully"
    * The system displays an updated list of all bank accounts
      ![](/docs/jpeg/slide_12.jpeg)
      ![](/docs/jpeg/slide_13.jpeg)
##### Cancelling Deletion:
  * The user clicks the **Cancel** button in the dialog box
  * The system closes dialog box without removing the selected bank account
### 3.1.5 Add a New Credit Card to the Bank Account:
##### Basic Action:
  * The user clicks the **Add Card** link in the 'Actions' column of the selected bank account
  * The system automatically generates a new credit card number
  * The system automatically sets a zero balance for the new credit card
  * The system inserts this data in the database
  * The system displays an info message "New credit card {card number is indicated here} successfully 
    linked to bank account {account number is indicated here}"
  * The system displays a list of all bank accounts with an updated 'Cards' column for the selected bank account  
### 3.2 Credit Card Management Functions
#### 3.2.1 View and Filter a List of Credit Cards:
##### Basic Action:
  * The user selects the **Cards** tab
  * The system handles request and displays a list of all credit cards sorted by expiration date
    ![](/docs/jpeg/slide_14.jpeg)
  * The list of bank accounts includes the following columns:
    * International Bank Account Number 
    * Credit Card Number
    * Credit Card Expiration Date 
    * Credit Card Balance
    * Link to remove a credit card, and links to deposit and transfer money
##### Filtering Action:
  * The user enters the required date into each input field or one of them
  * Valid date format is Month(required, 2 digits)/Year(required, 4 digits). For example, '06/2021'
  * The user clicks the **APPLY** button
  * The system validates the given dates
  * In the case, the given dates are valid, the system displays a list of credit cards to following rules:
    * In the case, the date is specified only for "Expiry from" input field, the system displays a list of credit cards 
      in which the expiration date starts at the specified date
      ![](/docs/jpeg/slide_15.jpeg)
    * In the case, the date is specified only for "Expiry to" input field, the system displays a list of credit cards
      in which the expiration date ends on the specified date
      ![](/docs/jpeg/slide_16.jpeg)
    * Otherwise, the system displays a list of credit cards with expiration dates in the specified range
      ![](/docs/jpeg/slide_17.jpeg)
  * In the case, there are no credit cards matching the given date range, the system displays an info message  
    "No data was found matching the date range"
    ![](/docs/jpeg/slide_18.jpeg) 
  * In the case, the given dates are invalid, the system displays a warning "Range start/end date format is incorrect!"
    ![](/docs/jpeg/slide_19.jpeg)
  * In the case, the user clicks the **RESET** button to call off the filter, the system displays a list of all credit cards
### 3.2.2 Remove a Credit Card:
##### Basic Action:
  * The user clicks the **Remove** link in the 'Actions' column of the selected credit card
  * The system displays dialog box to confirm card deletion
  * The dialog box indicates the number of the removed credit card
    ![](/docs/jpeg/slide_20.jpeg)
##### Confirm Deletion:
  * The user clicks the **Remove** button in the dialog box
  * The system checks the current balance of the removed card
  * In the case, the current balance of the removed card is greater than zero, the system displays an error message  
    "Credit card {card number is indicated here} balance is {card balance is indicated here}. It can't be removed!"
    ![](/docs/jpeg/slide_21.jpeg)
  * In the case, the current balance of the removed card is zero:
    * The system removes the selected credit card from the database
    * The system generates an info message "Credit card {card number is indicated here} removed successfully"
    * The system displays an updated list of all credit cards
    ![](/docs/jpeg/slide_22.jpeg)
##### Cancelling Deletion:
  * The user clicks the **Cancel** button in the dialog box
  * The system closes dialog box without removing the selected credit card 
### 3.2.3 Deposit Money:
##### Basic Action:
  * The user clicks the **Deposit** link in the 'Actions' column of the selected credit card
  * The system redirects the user to the page with a form for money deposit
    ![](/docs/jpeg/slide_23.jpeg)
##### Deposit Money:
  * The user enters sum of money what will be deposited to the card balance
  * Valid format of a sum of money is {up to 6 digits}\<,>{up to 2 digits} (for example, '1024', '1024,1', '0,45')
  * The user clicks the **ACCEPT** button
  * The system validates the given sum of money
  * In the case, the given sum of money is correct:
    * The system increases the credit card balance by the given amount of money
    * The system updates this balance in the database
    * The system redirect the user to the cards page
    * The system generates the info message "Deposit transaction for the credit card {card number is indicated here} was successful"
    * The system generates and displays a list of all credit cards with updated balance
      ![](/docs/jpeg/slide_24.jpeg)
  * In the case, the given amount of money is incorrect, the system displays a warning "Sum of money is incorrect!"
    ![](/docs/jpeg/slide_25.jpeg)
##### Cancelling Deposit Money: 
  * The user clicks the **CANCEL** button 
  * The system redirects the user to the 'Cards' page without performing a deposit transaction
### 3.2.4 Transfer Money:
##### Basic Action:
  * The user clicks the **Transfer** link in the 'Actions' column of the selected credit card
  * The system redirects the user to the page to the page with a form for money transfer
    ![](/docs/jpeg/slide_26.jpeg)
##### Transfer Money:  
  * The user enters target credit card number to transfer money 
  * The user enters sum of money what will be transferred to the specified credit card balance
  * Valid format of a credit card number is {16 digits}
  * Valid format of a sum of money is the same as format of a sum of money for a deposit money
  * The user clicks the **ACCEPT** button
  * The system validates the credit card number and the given sum of money
  * In the case, the given data is correct:
    * The system checks the source credit card balance for the possibility of debiting money 
    * In the case, the check was successful:
      * The system decreases the source credit card balance by the given sum of money
      * The system increases the target credit card balance by the given sum of money
      * The system updates these balances in the database
      * The system redirect the user to the cards page
      * The system generates and displays a list of all credit cards with updated balances
      * The system generates the info message "Transfer transaction between credit cards 
        {card numbers are indicated here} was successful"
        ![](/docs/jpeg/slide_27.jpeg)
    * In the case, the check failed, the system generates an error message 
      "The source credit card {card number is indicated here} doesn't contain enough money for the transfer!"
      ![](/docs/jpeg/slide_28.jpeg)
  * In the case, the given data is incorrect, the system generates displays warnings 
    "Sum of money is incorrect!" and/or "Target credit card number is invalid!"
    ![](/docs/jpeg/slide_29.jpeg)
##### Cancelling Transfer Money:
  * The user clicks the **CANCEL** button
  * The system redirects the user to the 'Cards' page without performing a transfer transaction  