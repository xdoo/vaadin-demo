insert into _users (oid, user_email, user_enabled, user_username, user_password, mandant) values (1,'admin@admin.muenchen.de',TRUE,'m01_admin1','admin1','m1');
insert into _users (oid, user_email, user_enabled, user_username, user_password, mandant) values (2,'admin@admin.muenchen.de',TRUE,'m02_admin2','admin2','m2');

insert into _users (oid, user_email, user_enabled, user_username, user_password, mandant) values (3,'admin@admin.muenchen.de',TRUE,'m01_read1','read1','m1');
insert into _users (oid, user_email, user_enabled, user_username, user_password, mandant) values (4,'admin@admin.muenchen.de',TRUE,'m02_read2','read2','m2');

insert into _users (oid, user_email, user_enabled, user_username, user_password, mandant) values (5,'admin@admin.muenchen.de',TRUE,'m02_hans','test','m1');


insert into _authorities (oid, auth_authority) values (1,'ADMIN');
insert into _users_authorities (authority_oid, user_oid) values (1, 1);
insert into _users_authorities (authority_oid, user_oid) values (1, 2);
insert into _users_authorities (authority_oid, user_oid) values (1, 5);

insert into _authorities (oid, auth_authority) values (2,'READ_ONLY_USER');
insert into _users_authorities (authority_oid, user_oid) values (2, 3);
insert into _users_authorities (authority_oid, user_oid) values (2, 4);

insert into _authorities (oid, auth_authority) values (100,'Fachanalyst');
insert into _users_authorities (authority_oid, user_oid) values (100, 1);

insert into _authorities (oid, auth_authority) values (101,'name');
insert into _users_authorities (authority_oid, user_oid) values (101, 1);

insert into _permissions (oid,perm_permission) values ('1','ROLE_READ_SEC_User');
insert into _permissions (oid,perm_permission) values ('2','ROLE_WRITE_SEC_User');
insert into _permissions (oid,perm_permission) values ('3','ROLE_DELETE_SEC_User');
insert into _authorities_permissions (authority_oid, permission_oid) values ('1','1');
insert into _authorities_permissions (authority_oid, permission_oid) values ('1','2');
insert into _authorities_permissions (authority_oid, permission_oid) values ('1','3');

insert into _permissions (oid,perm_permission) values ('4','ROLE_READ_SEC_Authority');
insert into _permissions (oid,perm_permission) values ('5','ROLE_WRITE_SEC_Authority');
insert into _permissions (oid,perm_permission) values ('6','ROLE_DELETE_SEC_Authority');
insert into _authorities_permissions (authority_oid, permission_oid) values ('1','4');
insert into _authorities_permissions (authority_oid, permission_oid) values ('1','5');
insert into _authorities_permissions (authority_oid, permission_oid) values ('1','6');

insert into _permissions (oid,perm_permission) values ('7','ROLE_READ_SEC_Permission');
insert into _permissions (oid,perm_permission) values ('8','ROLE_WRITE_SEC_Permission');
insert into _permissions (oid,perm_permission) values ('9','ROLE_DELETE_SEC_Permission');
insert into _authorities_permissions (authority_oid, permission_oid) values ('1','7');
insert into _authorities_permissions (authority_oid, permission_oid) values ('1','8');
insert into _authorities_permissions (authority_oid, permission_oid) values ('1','9');