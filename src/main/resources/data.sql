DROP TABLE IF EXISTS CalculationResult;

CREATE TABLE CalculationResult (
  hash INT NOT NULL PRIMARY KEY,
  script VARCHAR(250) NOT NULL,
  params VARCHAR(250) NOT NULL,
  result VARCHAR(250) DEFAULT NULL
);

INSERT INTO CalculationResult (hash, script, params, result) VALUES
  (123, 'script1', 'params', '0'),
  (1234, 'script2', 'params2', '1'),
  (12345, 'script3', 'params3', '2');