ALTER TABLE BUSINESSRECEIPT ADD COLUMN RECEIVERPHONE VARCHAR(255);
ALTER TABLE BUSINESSRECEIPT ADD COLUMN RECEIVERMAIL VARCHAR(255);
ALTER TABLE telepit_user ADD COLUMN SERVICEWORKER BOOLEAN;
UPDATE telepit_user SET SERVICEWORKER = FALSE;