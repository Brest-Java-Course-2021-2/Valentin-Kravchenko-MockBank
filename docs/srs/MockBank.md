# Software Requirements Specification
## 1. Purpose
The purpose of this project is to build a simple web-application to work with *bank accounts and linked them credit cards*.  
## 2. Scope
The application allows users to:  
* View and edit the list of bank accounts
* Filter the list of accounts by account number and customer name  
* Add a new credit card to the selected bank account
* View and edit the list of credit cards
* Filter the list of cards by expiration date
* Deposit money to the selected credit card
* Transfer money to the specified credit card
## 3. Overall description
### 3.1 Bank account requirements
**Users of the system should have the following management functions:**  
#### 3.1.1 View, filter, and edit the list of bank accounts:
##### Basic Action: 
  * The user selects the **Accounts** tab
  * The system generates and displays a list of all bank accounts
    <img alt="Slide 1" height="60%" src="https://user-images.githubusercontent.com/75541561/140600577-f2016467-99f4-443d-beb7-78f78252ad95.PNG" width="60%"/>
  * The list of bank accounts includes the following columns:
    * International bank account number
    * Customer full name
    * Date of the customer registration in the bank system
    * Total number of credit cards linked with the account
    * Links to edit, remove the account, and add a new credit card to the account
##### Action to Filter the List of Bank Accounts:
  * The user enters a search pattern into each input field or one of them 
  * The user clicks the **APPLY**
  * The system validates the given search patterns
  * In the case, the search patterns are valid, the system generates and displays a list of bank accounts matching
    the search patterns  
    <img alt="Slide 2" height="60%" src="https://user-images.githubusercontent.com/75541561/140600578-0840936a-c039-4991-a7e5-3afaca79d9c6.PNG" width="60%"/>
  * In the case, there are no bank accounts matching the search patterns, the system generates an info message  
    "No data found matching the given filter criteria"
  * In the case, the search patterns are invalid, the system generates error message "Filter criteria are incorrect"
  * To cancel the filter, the user clicks the **RESET** and the system generates and 
    displays a list of all bank accounts
  * The search pattern for the account number is entered in the field located above the Number column, and for the client 
    it is entered in the field located above the Client column
##### Action to Edit a Bank Account:
  * The user clicks the **Edit** in the line of the selected bank account
  * The system redirects the user to the page containing an account editing form
    <img alt="Slide 3" height="60%" src="https://user-images.githubusercontent.com/75541561/140600580-1fd3248f-b489-4005-bde0-065a59f5bafc.PNG" width="60%"/>
  * The user changes the customer full name
  * In the case, user clicks the **SAVE**:
    * The system validates the given data
    * In the case, the given data is valid:
      * The system updates this data in the database
      * In the case, an error occurred while updating data, the system generates an error message "There was error updating account"  
      * In the case, the update was successful:
        * The system redirect the user to the accounts page
        * The system generates an info message "Account {account number is presented here} updated successfully"
        * The system generates and displays a list of all bank accounts with updated data
    * In the case, the given data is invalid, the system generates an error message "The given data is incorrect"
  * In the case, user clicks the **CANCEL**, the system doesn't update the selected bank account and 
    redirects the user to the accounts page
##### Action to Remove a Bank Account:
  * The user clicks the **Remove** in the line of the selected bank account
  * The system checks the removed account for linked credit cards 
  * In the case, the removed account is linked to credit cards, the system generates an error message 
    "Account {account number is presented here} cannot be deleted" and displays a list of linked credit cards with one
  * In the case, the removed account isn't linked with credit cards:
    * The system redirect the user to the page to confirm account deletion   
      <img alt="Slide 4" height="60%" src="https://user-images.githubusercontent.com/75541561/140600581-419e6f4c-dd3b-42b4-87b4-5f758163f433.PNG" width="60%"/>
    * If user confirms account deletion: 
      * The system removes it from the database
      * In the case, an error occurred while deleting data, the system generates an error message "There was error removing account"
      * In the case, the deletion was successful:
        * The system generates and displays an updated list of all bank accounts
        * The system generates an info message "Account {account number is presented here} removed successfully"
    * If user doesn't confirm the account deletion, the system doesn't remove the selected account
##### Action to Add a New Credit Card to the Bank Account:
  * The user clicks the **Add Card** in the line of the selected bank account
  * The system automatically generates a new credit card number
  * The system automatically sets the new credit card balance to zero
  * The system inserts this data in the database
  * In the case, an error occurred while inserting data, the system generates an error message "Failed to add a new credit card"
  * In the case, the insertion was successful:
    * The system generates an info message "New credit card successfully added"
    * The system generates and displays a list of all bank accounts with an added credit card  
#### 3.1.2 Create a New Bank Account
* Basic Action:
  * The user clicks the **CREATE ACCOUNT**
  * The system redirects the user to the page containing an account creating form
    <img alt="Slide 5" height="60%" src="https://user-images.githubusercontent.com/75541561/140600582-c793dbdf-a448-4ecd-ac4f-709bb77a9350.PNG" width="60%"/>
  * The system automatically sets a registration date corresponding to the current date
  * The user enters the customer first and last name
  * In the case, user clicks the **CREATE**:
    * The system validates the given data
    * In the case, the given data is valid:
      * The system automatically generates a new bank account number  
      * The system inserts the above data in the database
      * In the case, an error occurred while inserting data, the system generates an error message "Failed to create a new bank account"
      * In the case, the insertion was successful:
        * The system redirect the user to the accounts page
        * The system generates an info message "New account created successfully"
        * The system generates and displays an updated list of all bank accounts
    * In the case, the given data is invalid, the system generates an error message "The given data is incorrect" 
  * In the case, user clicks the **CANCEL**, the system doesn't create the account and redirects the user   
    to the accounts page
### 3.2 Credit card requirements
**Users of the system should have the following management functions:**
#### 3.2.1 View, filter, and edit the list of credit cards:
##### Basic Action:
* The user selects the **Cards** tab
    * The system generates and displays a list of all credit cards
      <img alt="Slide 6" height="60%" src="https://user-images.githubusercontent.com/75541561/140600583-ef280d75-4fa2-45ab-b786-1d0505773421.PNG" width="60%"/>
    * The list of bank accounts includes the following columns:
        * Bank account number linked with the credit card 
        * Credit card number
        * Credit card expiration date 
        * Credit card balance
        * Links to remove the card, deposit and transfer money
##### Action to Filter the List of Credit Cards:
  * The user enters the required date into each input field or one of them
  * The user clicks the **APPLY**
  * The system validates the given dates
  * In the case, the given dates are valid, the system generates and displays a list of credit cards to 
  following rules:
    * In the case, the date is specified only for "Expiry from", the system displays all the credit cards in which
      the expiration date begins at the specified date
    * In the case, the date is specified only for "Expiry to", the system displays all the credit cards 
      that expire on the specified date
    * Otherwise, the system displays all the credit cards with expiration dates in the specified range
  * In the case, the given dates are invalid, the system generates error message "The filter criteria are incorrect"
  * To cancel the filter, the user clicks the **RESET** and the system generates and displays a list of all 
    credit cards
##### Action to Remove a Credit Card:
  * The user clicks the **Remove** in the line of the selected credit card
  * The system checks the current balance of the removed card
  * In the case, the current balance of the removed card is greater than zero, the system generates an error message  
  "The credit card {card number is presented here} cannot be deleted"
  * In the case, the current balance of the removed card is zero:
      * The system asks the user to confirm the card deletion
      * If user confirms the card deletion:
        * The system removes it from the database
        * In the case, an error occurred while deleting data, the system generates an error message "There was error
          removing card"
        * In the case, the deletion was successful:
          * The system generates and displays an updated list of all credit cards
          * The system generates an info message "Card {card number is presented here} removed successfully"
  * If user doesn't confirm the card deletion, the system doesn't remove the selected card and redirect the user 
    to the cards page 
##### Action to Deposit Money:
  * The user clicks the **Deposit** in the line of the selected bank account
  * The system redirects the user to the page containing a money deposit form
    <img alt="Slide 7" height="60%" src="https://user-images.githubusercontent.com/75541561/140600584-e0507df6-49c2-46c9-aa77-2bfb575ce189.PNG" width="60%"/>
  * The user enters sum of money what will be deposited to the card balance
  * The user clicks the **ACCEPT**
  * The system validates the given sum of money
  * In the case, the given sum of money is correct:
    * The system increases the credit card balance by the given amount of money
    * The system updates this balance in the database
    * In the case, an error occurred while updating data, the system generates an error message "Failed to deposit money"
      * In the case, the update was successful:
        * The system redirect the user to the cards page
        * The system generates and displays a list of all credit cards with updated balance
        * The system generates an info message "The transaction was successful"
    * In the case, the given amount of money is incorrect, the system generates an error message "The given amount of money is incorrect"
  * In the case, user clicks the **CANCEL**, the system doesn't deposit money to the selected credit card and
  redirects the user to the cards page
##### Action to Transfer Money:
  * The user clicks the **Transfer** in the line of the selected bank account
  * The system redirects the user to the page containing a money transfer form
    <img alt="Slide 8" height="60%" src="https://user-images.githubusercontent.com/75541561/140600585-c58a614c-8933-44eb-8e91-c22a8f977b14.PNG" width="60%"/>
  * The user enters credit card number to transfer money 
  * The user enters sum of money what will be transferred to the specified credit card
  * The user clicks the **ACCEPT**
  * The system validates the credit card number and the given sum of money
  * In the case, the given data is correct:
    * The system checks the source credit card balance for the possibility of debiting money 
    * In the case, the check was successful:
      * The system decreases the source credit card balance by the given sum of money
      * The system increases the target credit card balance by the given sum of money
      * The system updates these balances in the database
      * In the case, an error occurred while updating data:  
        * The system generates an error message "Failed to transfer money"
        * The system rolls back updates
      * In the case, the update was successful:
          * The system redirect the user to the cards page
          * The system generates and displays a list of all credit cards with updated balances
          * The system generates an info message "The transfer was successful"
    * In the case, the check failed, the system generates an error message "The transfer isn't possible" 
  * In the case, the given data is incorrect, the system generates an error message "The given data is incorrect"
  * In the case, user clicks the **CANCEL**, the system doesn't transfer money to the specified credit card and
    redirects the user to the cards page  