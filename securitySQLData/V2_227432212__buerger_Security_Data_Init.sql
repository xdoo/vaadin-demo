-- Roles
insert into _authorities (oid, auth_authority) values (227432212000000001,'buerger_ADMIN');
insert into _authorities (oid, auth_authority) values (227432212000000002,'buerger_READ_ONLY_USER');

insert into _users_authorities (authority_oid, user_oid) values (227432212000000001, 1);
insert into _users_authorities (authority_oid, user_oid) values (227432212000000001, 2);
insert into _users_authorities (authority_oid, user_oid) values (227432212000000001, 5);

insert into _users_authorities (authority_oid, user_oid) values (227432212000000002, 3);
insert into _users_authorities (authority_oid, user_oid) values (227432212000000002, 4);

-- Permissions for entity: Buerger
insert into _permissions (oid,perm_permission) values ('227432212100000000','buerger_READ_Buerger');
insert into _permissions (oid,perm_permission) values ('227432212200000000','buerger_WRITE_Buerger');
insert into _permissions (oid,perm_permission) values ('227432212300000000','buerger_DELETE_Buerger');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000002','227432212100000000');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212100000000');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212200000000');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212300000000');

-- Permissions for entity: Adresse
insert into _permissions (oid,perm_permission) values ('227432212100000001','buerger_READ_Adresse');
insert into _permissions (oid,perm_permission) values ('227432212200000001','buerger_WRITE_Adresse');
insert into _permissions (oid,perm_permission) values ('227432212300000001','buerger_DELETE_Adresse');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000002','227432212100000001');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212100000001');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212200000001');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212300000001');

-- Permissions for entity: Pass
insert into _permissions (oid,perm_permission) values ('227432212100000002','buerger_READ_Pass');
insert into _permissions (oid,perm_permission) values ('227432212200000002','buerger_WRITE_Pass');
insert into _permissions (oid,perm_permission) values ('227432212300000002','buerger_DELETE_Pass');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000002','227432212100000002');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212100000002');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212200000002');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212300000002');

-- Permissions for entity: Sachbearbeiter
insert into _permissions (oid,perm_permission) values ('227432212100000003','buerger_READ_Sachbearbeiter');
insert into _permissions (oid,perm_permission) values ('227432212200000003','buerger_WRITE_Sachbearbeiter');
insert into _permissions (oid,perm_permission) values ('227432212300000003','buerger_DELETE_Sachbearbeiter');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000002','227432212100000003');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212100000003');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212200000003');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212300000003');

-- Permissions for entity: Wohnung
insert into _permissions (oid,perm_permission) values ('227432212100000004','buerger_READ_Wohnung');
insert into _permissions (oid,perm_permission) values ('227432212200000004','buerger_WRITE_Wohnung');
insert into _permissions (oid,perm_permission) values ('227432212300000004','buerger_DELETE_Wohnung');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000002','227432212100000004');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212100000004');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212200000004');
insert into _authorities_permissions (authority_oid, permission_oid) values ('227432212000000001','227432212300000004');

