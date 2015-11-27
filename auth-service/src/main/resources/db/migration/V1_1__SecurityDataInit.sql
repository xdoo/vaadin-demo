insert into _users (oid, user_email, user_enabled, user_username, user_password, mandant) values (1,'admin@admin.muenchen.de',TRUE,'admin1','admin1','m1');
insert into _users (oid, user_email, user_enabled, user_username, user_password, mandant) values (2,'admin@admin.muenchen.de',TRUE,'admin2','admin2','m2');

insert into _users (oid, user_email, user_enabled, user_username, user_password, mandant) values (3,'admin@admin.muenchen.de',TRUE,'read1','read1','m1');
insert into _users (oid, user_email, user_enabled, user_username, user_password, mandant) values (4,'admin@admin.muenchen.de',TRUE,'read2','read2','m2');

insert into _users (oid, user_email, user_enabled, user_username, user_password, mandant) values (5,'admin@admin.muenchen.de',TRUE,'hans','test','m1');

insert into _authorities (oid, auth_authority) values (1,'ADMIN');
insert into _authorities (oid, auth_authority) values (2,'READ_ONLY_USER');

insert into _users_authorities (authority_oid, user_oid) values (1, 1);
insert into _users_authorities (authority_oid, user_oid) values (1, 2);

insert into _users_authorities (authority_oid, user_oid) values (2, 3);
insert into _users_authorities (authority_oid, user_oid) values (2, 4);

insert into _users_authorities (authority_oid, user_oid) values (1, 5);

insert into _authorities (oid, auth_authority) values (100,'Tester');
insert into _users_authorities (authority_oid, user_oid) values (100, 1);

insert into _authorities (oid, auth_authority) values (101,'name');
insert into _users_authorities (authority_oid, user_oid) values (101, 1);
