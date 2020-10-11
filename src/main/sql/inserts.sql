-- Inserts for testing

INSERT INTO OBSERVER (number, email, name, avatar_url)
VALUES (40978, '40978@mail.com', 'serge', 'https://www.gravatar.com/avatar/abe21319e5f8a37f463819580dc03fcd?s=128&r=g&d=mm'),
	   (39643, '39643@mail.com', 'neves', 'https://www.gravatar.com/avatar/2bfaf8dffa6f316ca3bc21909f2d06fe?s=128&r=g&d=mm');

INSERT INTO OBSERVABLE (number, email, name, avatar_url)
VALUES (10000, 'pedrofelix@cc.isel.ipl.pt', 'Pedro Félix', 'https://www.gravatar.com/avatar/1b9f3d52b55233d9b4dec767444b6d77?s=128&r=g&d=mm'),
	   (20000, 'lfalcao@cc.isel.ipl.pt', 'Luis Falcão', 'https://www.gravatar.com/avatar/c41bb7ccb8edc8752faf8853d00cc4a7?s=128&r=g&d=mm'),
	   (30000, 'fsousa@cc.isel.ipl.pt', 'Fernando Sousa', 'https://www.gravatar.com/avatar/0170295d41842830c3babaf506be9737?s=128&r=g&d=mm'),
	   (40000, 'palbp@cc.isel.ipl.pt', 'Paulo Pereira', 'https://www.gravatar.com/avatar/8c77d4c939023b0c767bd5e490b32ac2?s=128&r=g&d=mm');

INSERT INTO VISIBILITY (fk_Observer, fk_Observable)
VALUES (1,2),
	   (2,1),
       (2,2),
	   (2,3),
	   (2,4);

INSERT INTO WeekdaySubscription (dayOfWeek, startNotificableHour, startNotificableMinute, endNotificableHour, endNotificableMinute, fk_Observer, fk_Observable)
VALUES (0, 16, 0, 22, 30, 2, 1),
	   (3, 12, 30, 18, 30, 1, 2);

INSERT INTO DefaultSubscription(fk_Observer, fk_Observable) VALUES (2, 1);

INSERT INTO Location (latitude, longitude, isValid, fk_Observable) 
VALUES (18.0, 18.0, true, 1);

INSERT INTO ScheduleDayOfWeek(dayOfWeek, startAvailabilityHour, startAvailabilityMinute, endAvailabilityHour, endAvailabilityMinute, latitude, longitude, hasLocation, fk_Observable)
VALUES (0, 18, 0, 23, 0, 38.7567556, -9.1166672, true, 1),
       (0, 18, 0, 23, 0, 38.7567556, -9.1166672, true, 2),
       (0, 18, 0, 23, 0, 38.7567556, -9.1166672, true, 3),
       (0, 18, 0, 23, 0, 38.7567556, -9.1166672, true, 4);