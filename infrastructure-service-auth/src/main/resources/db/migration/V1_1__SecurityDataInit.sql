insert into _users (oid, user_email, user_enabled, user_username, user_password, user_forname, user_surname)
values ('00000000-0000-0000-0000-000000000001','admin@admin.muenchen.de',TRUE,'admin','admin', 'Addi', 'Admin');

insert into _users (oid, user_email, user_enabled, user_username, user_password, user_forname, user_surname)
values ('00000000-0000-0000-0000-000000000002','admin@admin.muenchen.de',TRUE,'read','read', 'Reinhard', 'Ready');

insert into _users (oid, user_email, user_enabled, user_username, user_password, user_forname, user_surname)
values ('00000000-0000-0000-0000-000000000003','admin@admin.muenchen.de',TRUE,'hans','test','Thomas', 'Test');

insert into _users (oid, user_email, user_enabled, user_username, user_password, user_forname, user_surname)
values ('00000000-0000-0000-0000-000000000004','fabian.holtkoetter@muenchen.de',TRUE,'fabian.holtkoetter', NULL , 'Fabian', 'Holtkoetter');


insert into _authorities (oid, auth_authority) values ('00000000-0000-0000-0000-000000000001','ADMIN');
insert into _users_authorities (authority_oid, user_oid) values ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001');
insert into _users_authorities (authority_oid, user_oid) values ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003');
insert into _users_authorities (authority_oid, user_oid) values ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000004');

insert into _authorities (oid, auth_authority) values ('00000000-0000-0000-0000-000000000002','READ_ONLY_USER');
insert into _users_authorities (authority_oid, user_oid) values ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002');


insert into _permissions (oid,perm_permission) values ('00000000-0000-0000-0000-000000000001','ROLE_READ_SEC_User');
insert into _permissions (oid,perm_permission) values ('00000000-0000-0000-0000-000000000002','ROLE_WRITE_SEC_User');
insert into _permissions (oid,perm_permission) values ('00000000-0000-0000-0000-000000000003','ROLE_DELETE_SEC_User');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0000-0000-0000-000000000001','00000000-0000-0000-0000-000000000001');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0000-0000-0000-000000000001','00000000-0000-0000-0000-000000000002');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0000-0000-0000-000000000001','00000000-0000-0000-0000-000000000003');

insert into _permissions (oid,perm_permission) values ('00000000-0000-0000-0000-000000000004','ROLE_READ_SEC_Authority');
insert into _permissions (oid,perm_permission) values ('00000000-0000-0000-0000-000000000005','ROLE_WRITE_SEC_Authority');
insert into _permissions (oid,perm_permission) values ('00000000-0000-0000-0000-000000000006','ROLE_DELETE_SEC_Authority');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0000-0000-0000-000000000001','00000000-0000-0000-0000-000000000004');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0000-0000-0000-000000000001','00000000-0000-0000-0000-000000000005');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0000-0000-0000-000000000001','00000000-0000-0000-0000-000000000006');

insert into _permissions (oid,perm_permission) values ('00000000-0000-0000-0000-000000000007','ROLE_READ_SEC_Permission');
insert into _permissions (oid,perm_permission) values ('00000000-0000-0000-0000-000000000008','ROLE_WRITE_SEC_Permission');
insert into _permissions (oid,perm_permission) values ('00000000-0000-0000-0000-000000000009','ROLE_DELETE_SEC_Permission');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0000-0000-0000-000000000001','00000000-0000-0000-0000-000000000007');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0000-0000-0000-000000000001','00000000-0000-0000-0000-000000000008');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0000-0000-0000-000000000001','00000000-0000-0000-0000-000000000009');

