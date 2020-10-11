-- Triggers 

--DROP FUNCTION create_location_for_new_observable() CASCADE
CREATE FUNCTION create_location_for_new_observable() RETURNS TRIGGER AS '
BEGIN
    INSERT INTO LOCATION (latitude, longitude, fk_Observable)
    VALUES (0.0, 0.0, NEW.id);
    RETURN NULL;  
END;'
LANGUAGE 'plpgsql';

CREATE TRIGGER CREATE_DEFAULT_LOCATION_AFTER_OBSERVABLE_CREATED AFTER INSERT
ON OBSERVABLE
FOR EACH ROW
EXECUTE PROCEDURE create_location_for_new_observable();

--DROP FUNCTION create_visibilityRestriction_for_new_visibility() CASCADE
CREATE FUNCTION create_visibilityRestriction_for_new_visibility() RETURNS TRIGGER AS '
BEGIN
    INSERT INTO VisibilityRestriction (fk_Observer, fk_Observable)
    VALUES (NEW.fk_Observer, NEW.fk_Observable);
    RETURN NULL;
END;'
LANGUAGE 'plpgsql';

CREATE TRIGGER CREATE_VISIBILITYRESTRICTION_LOCATION_AFTER_VISIBILITY_CREATED AFTER INSERT
ON Visibility
FOR EACH ROW
EXECUTE PROCEDURE create_visibilityRestriction_for_new_visibility();


--DROP FUNCTION create_availabilityDetails_for_new_observable() CASCADE
CREATE FUNCTION create_availabilityDetails_for_new_observable() RETURNS TRIGGER AS '
BEGIN
    INSERT INTO AvailabilityDetails (fk_Observable)
    VALUES (NEW.id);
    RETURN NULL;
END;'
LANGUAGE 'plpgsql';

CREATE TRIGGER CREATE_DEFAULT_AVAILABILITY_DETAILS_AFTER_OBSERVABLE_CREATED AFTER INSERT
ON OBSERVABLE
FOR EACH ROW
EXECUTE PROCEDURE create_availabilityDetails_for_new_observable();

--DROP FUNCTION create_GlobalvisibilityRestriction_for_new_Observable() CASCADE
CREATE FUNCTION create_GlobalvisibilityRestriction_for_new_Observable() RETURNS TRIGGER AS '
BEGIN
    INSERT INTO GlobalVisibilityRestriction(fk_Observable)
    VALUES (NEW.id);
    RETURN NULL;
END;'
LANGUAGE 'plpgsql';

CREATE TRIGGER CREATE_GLOBAL_VISIBILITY_RESTRICTION_AFTER_OBSERVABLE_CREATED AFTER INSERT
ON Observable
FOR EACH ROW
EXECUTE PROCEDURE create_GlobalvisibilityRestriction_for_new_Observable();

--DROP FUNCTION create_UserRole_for_new_Observable() CASCADE
CREATE FUNCTION create_UserRole_for_new_Observable() RETURNS TRIGGER AS '
BEGIN
    INSERT INTO User_Role(user_id, role, observer_or_observable)
    VALUES (NEW.id, ''OBSERVABLE'', 1);
    RETURN NULL;
END;'
LANGUAGE 'plpgsql';
CREATE TRIGGER CREATE_USER_ROLE_AFTER_OBSERVABLE_CREATED AFTER INSERT
ON Observable
FOR EACH ROW
EXECUTE PROCEDURE create_UserRole_for_new_Observable();

--DROP FUNCTION create_UserRole_for_new_Observer() CASCADE
CREATE FUNCTION create_UserRole_for_new_Observer() RETURNS TRIGGER AS '
BEGIN
    INSERT INTO User_Role(user_id, role, observer_or_observable)
    VALUES (NEW.id, ''OBSERVER'', 0);
    RETURN NULL;
END;'
LANGUAGE 'plpgsql';
CREATE TRIGGER CREATE_USER_ROLE_AFTER_OBSERVER_CREATED AFTER INSERT
ON OBSERVER
FOR EACH ROW
EXECUTE PROCEDURE create_UserRole_for_new_Observer();


