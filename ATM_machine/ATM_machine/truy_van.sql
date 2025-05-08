INSERT INTO ATM_account(card_number, pin, balance)
VALUES (41234124, 100245, 500000)
SELECT * FROM ATM_account
update ATM_account set balance = 100000 where card_number = 412341234
DROP TABLE ATM_transaction;
select * from ATM_account where card_number =412341234
update ATM_account set pin = 123123 where card_number = 412341234

SELECT * FROM ATM_transaction
insert into ATM_transaction values(41234124, '2024-05-05', 'Deposit', 1000000)