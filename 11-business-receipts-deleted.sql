ALTER TABLE BUSINESSRECEIPT ADD COLUMN DELETED BOOLEAN;
UPDATE BUSINESSRECEIPT SET DELETED = FALSE;