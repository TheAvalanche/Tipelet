ALTER TABLE SERVICEGOOD ADD COLUMN WITHBILL BOOLEAN;
UPDATE SERVICEGOOD SET WITHBILL = TRUE;