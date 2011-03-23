--
-- Table structure for table mytasks_mytask
--
DROP TABLE IF EXISTS mytasks_mytask;
CREATE TABLE mytasks_mytask (
	id_mytask int(11) NOT NULL default '0',
	name varchar(50) NOT NULL default '',
	description varchar(50) NOT NULL default '',
	date_mytask DATE NOT NULL,
	is_done SMALLINT NOT NULL default '0',
	PRIMARY KEY (id_mytask)
);

--
-- Table structure for table mytasks_mytasks_parameter
--
DROP TABLE IF EXISTS mytasks_mytasks_parameter;
CREATE TABLE mytasks_mytasks_parameter (
	parameter_key varchar(100) NOT NULL,
	parameter_value varchar(100) NOT NULL,
	PRIMARY KEY (parameter_key)
);

--
-- Table structure for table mytasks_user_mytask
--
DROP TABLE IF EXISTS mytasks_user_mytask;
CREATE TABLE mytasks_user_mytask (
	user_guid varchar(50) NOT NULL default '',
	id_mytask int(11) NOT NULL default '0',
	PRIMARY KEY (user_guid, id_mytask)
);
