DROP TABLE IF EXISTS EXECUTION_RESULTS;

CREATE TABLE EXECUTION_RESULTS (
  hash INT NOT NULL PRIMARY KEY,
  script_name VARCHAR(250) NOT NULL,
  script VARCHAR(250) NOT NULL,
  params VARCHAR(250) NOT NULL,
  result VARCHAR(250) DEFAULT NULL
);

INSERT INTO EXECUTION_RESULTS (hash, script_name, script, params, result) VALUES
  (123, 'script1', 'script1', 'params', '0'),
  (1234, 'script2', 'script1', 'params2', '1'),
  (12345, 'script3', 'script1', 'params3', '2');