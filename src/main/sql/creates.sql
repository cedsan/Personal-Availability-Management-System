-- DDL

CREATE DATABASE "SGDL";

DROP TABLE IF EXISTS OBSERVER;
CREATE TABLE IF NOT EXISTS OBSERVER (
	id SERIAL PRIMARY KEY,
	number INTEGER NOT NULL UNIQUE,
	email VARCHAR(200) NOT NULL UNIQUE,
	name VARCHAR(200) NOT NULL,
	avatar_url VARCHAR(500) NOT NULL UNIQUE,
	registration_token VARCHAR(500) NULL UNIQUE
);

DROP TABLE IF EXISTS OBSERVABLE;
CREATE TABLE IF NOT EXISTS OBSERVABLE (
	id SERIAL PRIMARY KEY,
	number INTEGER NOT NULL UNIQUE,
	email VARCHAR(200) NOT NULL UNIQUE,
	name VARCHAR(200) NOT NULL,
	avatar_url VARCHAR(500) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS LOCATION;
CREATE TABLE IF NOT EXISTS LOCATION (
	id SERIAL PRIMARY KEY,
	latitude DOUBLE PRECISION NOT NULL,
	longitude DOUBLE PRECISION NOT NULL,
	isValid BOOLEAN DEFAULT FALSE NOT NULL,
	lastUpdateDateTime TIMESTAMP DEFAULT current_timestamp, 
	fk_Observable INTEGER references OBSERVABLE(id)
);

DROP TABLE IF EXISTS ScheduleDayOfWeek;
CREATE TABLE IF NOT EXISTS ScheduleDayOfWeek (
	id SERIAL PRIMARY KEY,
	dayOfWeek SMALLINT NOT NULL CHECK (dayOfWeek > -1 AND dayOfWeek < 10),
	startAvailabilityHour SMALLINT NOT NULL CHECK (startAvailabilityHour > -1 AND startAvailabilityHour < 24),
	startAvailabilityMinute SMALLINT NOT NULL CHECK (startAvailabilityMinute > -1 AND startAvailabilityMinute < 60),
	endAvailabilityHour SMALLINT NOT NULL CHECK (endAvailabilityHour > -1 AND endAvailabilityHour < 24),
	endAvailabilityMinute SMALLINT NOT NULL CHECK (endAvailabilityMinute > -1 AND endAvailabilityMinute < 60),
    latitude DOUBLE PRECISION NULL,
    longitude DOUBLE PRECISION NULL,
    hasLocation BOOLEAN DEFAULT FALSE,
	fk_Observable INTEGER references OBSERVABLE(id)
);


-- Who sees whom
DROP TABLE IF EXISTS Visibility;
CREATE TABLE IF NOT EXISTS Visibility (
	id SERIAL PRIMARY KEY,
	fk_Observer INTEGER references OBSERVER(id),
	fk_Observable INTEGER references OBSERVABLE(id)
);

DROP TABLE IF EXISTS DefaultSubscription;
CREATE TABLE IF NOT EXISTS DefaultSubscription (
	id SERIAL PRIMARY KEY,
	fk_Observer INTEGER references OBSERVER(id),
	fk_Observable INTEGER references OBSERVABLE(id)
);

DROP TABLE IF EXISTS WeekdaySubscription;
CREATE TABLE IF NOT EXISTS WeekdaySubscription (
	id SERIAL PRIMARY KEY,
	dayOfWeek SMALLINT NOT NULL CHECK (dayOfWeek > -1 AND dayOfWeek < 10),
	startNotificableHour SMALLINT NOT NULL CHECK (startNotificableHour > -1 AND startNotificableHour < 24),
	startNotificableMinute SMALLINT NOT NULL CHECK (startNotificableMinute > -1 AND startNotificableMinute < 60),
	endNotificableHour SMALLINT NOT NULL CHECK (endNotificableHour > -1 AND endNotificableHour < 24),
	endNotificableMinute SMALLINT NOT NULL CHECK (endNotificableMinute > -1 AND endNotificableMinute < 60),
	fk_Observer INTEGER references OBSERVER(id),
    fk_Observable INTEGER references OBSERVABLE(id)
);

-- Always that a new field can be restricted this
-- table it's where it should be added
DROP TABLE IF EXISTS VisibilityRestriction;
CREATE TABLE IF NOT EXISTS VisibilityRestriction (
	id SERIAL PRIMARY KEY,
    canSeeWhenAvailable BOOLEAN NOT NULL DEFAULT TRUE,
    canSeeSchedule BOOLEAN NOT NULL DEFAULT TRUE,
    canSeeLocation BOOLEAN NOT NULL DEFAULT TRUE,
	fk_Observer INTEGER references OBSERVER(id),
    fk_Observable INTEGER references OBSERVABLE(id)
);

-- observer_or_observable indicates weather the id is from an Observer (0) OR OBSERVABLE (1)
CREATE TABLE User_Role
  (
    user_id INTEGER NOT NULL,
    role VARCHAR CHECK (role = 'OBSERVER' OR role = 'OBSERVABLE' OR role = 'ADMIN' OR role = 'NONE'),
    observer_or_observable INTEGER CHECK (observer_or_observable = 0 OR observer_or_observable = 1),
    PRIMARY KEY(user_id, observer_or_observable)
  );

  -- This table defines the parameters in order
  -- to allow a given path to have access control according to roles that have access
  -- OBS: the accessible_roles column value can have all the roles space separated
  CREATE TABLE PathControlAccessParams
  (
    method VARCHAR CHECK (method = 'GET' or method = 'PUT' or method = 'POST' or method = 'DELETE'),
    regex VARCHAR NOT NULL,
    accessible_roles VARCHAR NOT NULL
  );

  -- This table purpose is to store information related
  -- to CURRENT availability of Observable
  CREATE TABLE AvailabilityDetails
  (
    isAvailable BOOLEAN DEFAULT FALSE NOT NULL,
    fk_Observable INTEGER references OBSERVABLE(id),
    PRIMARY KEY(fk_Observable)
  );

  DROP TABLE IF EXISTS VisibilityRestriction;
  CREATE TABLE IF NOT EXISTS VisibilityRestriction (
  	id SERIAL PRIMARY KEY,
    canSeeWhenAvailable BOOLEAN NOT NULL DEFAULT TRUE,
    canSeeSchedule BOOLEAN NOT NULL DEFAULT TRUE,
    canSeeLocation BOOLEAN NOT NULL DEFAULT TRUE,
  	fk_Observer INTEGER references OBSERVER(id),
    fk_Observable INTEGER references OBSERVABLE(id)
  );

  DROP TABLE IF EXISTS GlobalVisibilityRestriction;
  CREATE TABLE IF NOT EXISTS GlobalVisibilityRestriction (
    id SERIAL PRIMARY KEY,
    canSeeWhenAvailable BOOLEAN NOT NULL DEFAULT TRUE,
    canSeeSchedule BOOLEAN NOT NULL DEFAULT TRUE,
    canSeeLocation BOOLEAN NOT NULL DEFAULT TRUE,
    fk_Observable INTEGER references OBSERVABLE(id)
  );

-- END


