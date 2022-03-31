INSERT INTO bank_account (number, customer, registration_date)
    VALUES ('BY80F29S8416E1PXLF9VHCGM99T6', 'Ivan Ivanov', '2020-06-28');
INSERT INTO bank_account (number, customer, registration_date)
    VALUES ('BY152JZ04036M1X5F4FZO85DSQ06', 'Petr Petrov', '2020-11-30');
INSERT INTO bank_account (number, customer, registration_date)
    VALUES ('BY31URCR4656T7OBMLNTC6TQ99IK', 'Sergey Sergeev', '2019-04-03');

INSERT INTO credit_card (number, expiration_date, balance, account_id)
    VALUES ('4000003394112581', '2023-07-31', '2000.70', (SELECT ba.id FROM bank_account AS ba WHERE ba.number='BY80F29S8416E1PXLF9VHCGM99T6'));
INSERT INTO credit_card (number, expiration_date, balance, account_id)
    VALUES ('4000002538269224', '2023-08-31', '1500.00', (SELECT ba.id FROM bank_account AS ba WHERE ba.number='BY80F29S8416E1PXLF9VHCGM99T6'));
INSERT INTO credit_card (number, expiration_date, balance, account_id)
    VALUES ('4000003417089477', '2023-12-31', '3000.50', (SELECT ba.id FROM bank_account AS ba WHERE ba.number='BY152JZ04036M1X5F4FZO85DSQ06'));
INSERT INTO credit_card (number, expiration_date, balance, account_id)
    VALUES ('4000000091547903', '2022-05-31', '1055.00', (SELECT ba.id FROM bank_account AS ba WHERE ba.number='BY31URCR4656T7OBMLNTC6TQ99IK'));
INSERT INTO credit_card (number, expiration_date, balance, account_id)
    VALUES ('4000007688433504', '2022-06-30', '2340.30', (SELECT ba.id FROM bank_account AS ba WHERE ba.number='BY31URCR4656T7OBMLNTC6TQ99IK'));
INSERT INTO credit_card (number, expiration_date, account_id)
    VALUES ('4000007875097476', '2022-07-31', (SELECT ba.id FROM bank_account AS ba WHERE ba.number='BY31URCR4656T7OBMLNTC6TQ99IK'));