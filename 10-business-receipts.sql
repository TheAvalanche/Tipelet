CREATE TABLE BUSINESSRECEIPT (ID BIGINT NOT NULL, NUMBER VARCHAR(255), PAID BOOLEAN, DATE TIMESTAMP, PROVIDERADDRESS VARCHAR(255), PROVIDERLEGALADDRESS VARCHAR(255), PROVIDERBANKNAME VARCHAR(255), PROVIDERBANKNUM VARCHAR(255), PROVIDERNAME VARCHAR(255), PROVIDERREGNUM VARCHAR(255), RECEIVERLEGALADDRESS VARCHAR(255), RECEIVERBANKNAME VARCHAR(255), RECEIVERBANKNUM VARCHAR(255), RECEIVERNAME VARCHAR(255), RECEIVERREGNUM VARCHAR(255), STORE_ID BIGINT, USER_ID BIGINT, PRIMARY KEY (ID));
CREATE TABLE RECEIPTITEM (ID BIGINT NOT NULL, COUNT INTEGER, NAME VARCHAR(255), PRICE FLOAT, PARENT_ID BIGINT, PRIMARY KEY (ID));
ALTER TABLE BUSINESSRECEIPT ADD CONSTRAINT FK_BUSINESSRECEIPT_USER_ID FOREIGN KEY (USER_ID) REFERENCES telepit_user (ID);
ALTER TABLE BUSINESSRECEIPT ADD CONSTRAINT FK_BUSINESSRECEIPT_STORE_ID FOREIGN KEY (STORE_ID) REFERENCES STORE (ID);
ALTER TABLE RECEIPTITEM ADD CONSTRAINT FK_RECEIPTITEM_PARENT_ID FOREIGN KEY (PARENT_ID) REFERENCES BUSINESSRECEIPT (ID);

CREATE SEQUENCE receiptitem_seq START WITH 1;
CREATE SEQUENCE businessreceipt_seq START WITH 1;

ALTER TABLE STORE ADD COLUMN LEGALNAME VARCHAR(255);
ALTER TABLE STORE ADD COLUMN LEGALREGNUM VARCHAR(255);
ALTER TABLE STORE ADD COLUMN LEGALBANKNAME VARCHAR(255);
ALTER TABLE STORE ADD COLUMN LEGALBANKNUM VARCHAR(255);
ALTER TABLE STORE ADD COLUMN LEGALADDRESS VARCHAR(255);