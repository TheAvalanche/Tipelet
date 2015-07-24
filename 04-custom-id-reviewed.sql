CREATE SEQUENCE stockgood_increment_id_seq START WITH 1;
ALTER TABLE stockgood ADD COLUMN incrementid BIGINT;

CREATE VIEW view_stockgood AS SELECT * FROM stockgood ORDER BY id;
CREATE RULE rule_stockgood AS ON UPDATE TO view_stockgood DO INSTEAD UPDATE stockgood SET incrementid = NEW.incrementid WHERE id = NEW.id;
UPDATE view_stockgood SET incrementid = nextval('stockgood_increment_id_seq');

DROP RULE rule_stockgood ON view_stockgood;
DROP VIEW view_stockgood;