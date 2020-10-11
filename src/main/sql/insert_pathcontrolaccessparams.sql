/********** OBSERVABLE **********/

-- GET /observable
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observable)+(/|\s|\?\w*|.{0})', 'NONE OBSERVABLE OBSERVER');

-- GET /observable/{id}
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observable)+(/\d+)+(/|\s|\?\w*|.{0})', 'NONE OBSERVABLE OBSERVER');

-- GET /observable/{id}/observer
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observable)+(/\d+)+(/observer)+(\s|.{0})', 'OBSERVABLE');

-- GET /observable/{id}/observer/{observerID}
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observable)+(/\d+)+(/observer)+(/\d+)+(/|\s|\?\w*|.{0})', 'OBSERVABLE');

-- GET /observable/{id}/location
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observable)+(/\d+)+(/location)+(/?)', 'OBSERVABLE');

-- GET /observable/{id}/schedule
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observable)+(/\d+)+(/schedule)+(/?)', 'OBSERVABLE');

-- GET /observable/{id}/schedule/{scheduleId}
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observable)+(/\d+)+(/schedule)+(/\d+)+(/?)', 'OBSERVABLE');

-- POST /observable/{id}/location
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('POST', '(/?)+(observable)+(/\d+)+(/location)+(/?)', 'OBSERVABLE');

-- POST /observable/{id}/schedule
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('POST', '(/?)+(observable)+(/\d+)+(/schedule)+(/?)', 'OBSERVABLE');

-- POST /observable
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('POST', '(/?)+(observable)+(/|\s|\?\w*|.{0})', 'NONE');

-- POST /observable/{id}/unavailable
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('POST', '(/?)+(observable)+(/\d+)+(/unavailable)+(/?)', 'OBSERVABLE');

-- POST /observable/{id}/available
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('POST', '(/?)+(observable)+(/\d+)+(/available)+(/?)', 'OBSERVABLE');

-- POST /observable/{id}/entered-location
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('POST', '(/?)+(observable)+(/\d+)+(/entered-location)+(/?)', 'OBSERVABLE');

-- PUT /observable/{id}/schedule/{scheduleId}
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('PUT', '(/?)+(observable)+(/\d+)+(/schedule)+(/\d+)+(/?)+(\?type=.+)', 'OBSERVABLE');

-- DELETE /observable/{id}/schedule
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('DELETE', '(/?)+(observable)+(/\d+)+(/schedule)+(/?)', 'OBSERVABLE');

-- DELETE /observable/{id}/schedule/{scheduleId}
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('DELETE', '(/?)+(observable)+(/\d+)+(/schedule)+(/\d+)+(\?type=.+)', 'OBSERVABLE');




/********** OBSERVER **********/

-- GET /observer
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observer)+(/|\s|\?\w*|.{0})', 'NONE OBSERVABLE OBSERVER');

-- GET /observer/{id}
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observer)+(/\d+)+(/|\s|\?\w*|.{0})', 'NONE OBSERVABLE OBSERVER');

-- GET /observer/{id}/observable
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observer)+(/\d+)+(/observable)+(\s|.{0})', 'OBSERVER');

-- GET /observer/{id}/observable
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observer)+(/\d+)+(/observable)+(\?subscribed=(true|false))', 'OBSERVER');

-- GET /observer/{id}/observable/{observableId}
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observer)+(/\d+)+(/observable)+(/\d+)+(/|\s|\?\w*|.{0})', 'OBSERVER');

-- GET /observer/{id}/observable/{observableId}/location
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observer)+(/\d+)+(/observable)+(/\d+)+(/location)+(/?)', 'OBSERVER');

-- GET /observer/{id}/observable/{observableId}/schedule
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observer)+(/\d+)+(/observable)+(/\d+)+(/schedule)+(/?)', 'OBSERVER');

-- GET /observer/{id}/observable/{observableId}/schedule/{scheduleId}?type=<scheduleType>
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observer)+(/\d+)+(/observable)+(/\d+)+(/schedule)+(/\d+)+(\?type=weekday)', 'OBSERVER');

-- GET /observer/{id}/observable/{observableId}/subscription
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observer)+(/\d+)+(/observable)+(/\d+)+(/subscription)+(/?)', 'OBSERVER');

-- GET /observer/{id}/observable/{observableId}/subscription
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observer)+(/\d+)+(/observable)+(/\d+)+(/subscription)+(\?type=.+)', 'OBSERVER');

-- GET /observer/{id}/observable/{observableId}/subscription/{subscriptionID}?type=<SubscriptionType>
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observer)+(/\d+)+(/observable)+(/\d+)+(/subscription)+(/\d+)+(\?type=.+)', 'OBSERVER');

-- POST /observer/{id}/observable/{observableId}/subscription
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('POST', '(/?)+(observer)+(/\d+)+(/observable)+(/\d+)+(/subscription)+(/?)', 'OBSERVER');

-- POST /observable
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('POST', '(/?)+(observer)+(/|\s|\?\w*|.{0})', 'NONE');

-- PUT /observer/{id}/observable/{observableId}/subscription
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('PUT', '(/?)+(observer)+(/\d+)+(/notification/token)+(/?)', 'OBSERVER');

-- DELETE /observer/{id}/observable/{observableId}/subscription/{subscriptionID}?type=<SubscriptionType>
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('DELETE', '(/?)+(observer)+(/\d+)+(/observable)+(/\d+)+(/subscription)+(/\d+)+(\?type=.+)', 'OBSERVER');

-- DELETE /observer/{id}/observable/{observableId}/subscription
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('DELETE', '(/?)+(observer)+(/\d+)+(/observable)+(/\d+)+(/subscription)+(/?)', 'OBSERVER');

-- PUT /observer/{id}/observable/{observableId}/subscription/{subscriptionID}?type=<SubscriptionType>
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('PUT', '(/?)+(observer)+(/\d+)+(/observable)+(/\d+)+(/subscription)+(/\d+)+(\?type=.+)', 'OBSERVER');


/********** OBSERVABLE VISIBILITY RESTRICTION **********/

-- GET /observable/{id}/observer/{observerID}/restriction
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observable)+(/\d+)+(/observer)+(/\d+)+(/restriction)+(/?)', 'OBSERVABLE');

-- GET /observable/global-vis-restriction-config
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(observable)+(/\d+)+(/global-vis-restriction-config)+(/?)', 'OBSERVABLE');

-- PUT /observable/{id}/observer/{observerID}/restriction
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('PUT', '(/?)+(observable)+(/\d+)+(/observer)+(/\d+)+(/restriction)+(/?)', 'OBSERVABLE');

-- PUT /observable/global-vis-restriction-config
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('PUT', '(/?)+(observable)+(/\d+)+(/global-vis-restriction-config)+(/?)', 'OBSERVABLE');


/********** USER_ **********/

-- GET /user
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('GET', '(/?)+(user)+(\?user=.+)', 'OBSERVABLE OBSERVER NONE');

-- POST /user/register
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('POST', '(/?)+(user)+(/register)+(/?)', 'NONE');


/********** VISIBILITY **********/

-- POST /visibility/assoc/{observerID}/{observableID}
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('POST', '(/?)+(visibility/assoc)+(/\d+)+(/\d+)+(/?)', 'NONE');

-- DELETE /visibility/assoc/{observerID}/{observableID}
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('DELETE', '(/?)+(visibility/assoc)+(/\d+)+(/\d+)+(/?)', 'NONE');

-- POST /visibility/assoc/request/{observerID}/{observableID}
INSERT INTO PathControlAccessParams(method, regex, accessible_roles)
VALUES ('POST', '(/?)+(visibility/assoc/request)+(/\d+)+(/\d+)+(/?)', 'NONE');

