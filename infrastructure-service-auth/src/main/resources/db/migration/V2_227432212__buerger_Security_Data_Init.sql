-- Roles
insert into _authorities (oid, auth_authority) values ('00000000-0d8e-5714-0000-000000000001','buerger_ADMIN');
insert into _authorities (oid, auth_authority) values ('00000000-0d8e-5714-0000-000000000002','buerger_READ_ONLY_USER');

insert into _users_authorities (authority_oid, user_oid) values ('00000000-0d8e-5714-0000-000000000001', '00000000-0000-0000-0000-000000000001');
insert into _users_authorities (authority_oid, user_oid) values ('00000000-0d8e-5714-0000-000000000001', '00000000-0000-0000-0000-000000000003');
insert into _users_authorities (authority_oid, user_oid) values ('00000000-0d8e-5714-0000-000000000001', '00000000-0000-0000-0000-000000000004');

insert into _users_authorities (authority_oid, user_oid) values ('00000000-0d8e-5714-0000-000000000002', '00000000-0000-0000-0000-000000000002');

-- Permissions for entity: Buerger
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-000005f5e100','buerger_READ_Buerger');
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-00000bebc200','buerger_WRITE_Buerger');
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-000011e1a300','buerger_DELETE_Buerger');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000002','00000000-0d8e-5714-0000-000005f5e100');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-000005f5e100');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-00000bebc200');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-000011e1a300');

-- Permissions for entity: Pass
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-000005f5e101','buerger_READ_Pass');
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-00000bebc201','buerger_WRITE_Pass');
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-000011e1a301','buerger_DELETE_Pass');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000002','00000000-0d8e-5714-0000-000005f5e101');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-000005f5e101');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-00000bebc201');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-000011e1a301');

-- Permissions for entity: Sachbearbeiter
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-000005f5e102','buerger_READ_Sachbearbeiter');
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-00000bebc202','buerger_WRITE_Sachbearbeiter');
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-000011e1a302','buerger_DELETE_Sachbearbeiter');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000002','00000000-0d8e-5714-0000-000005f5e102');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-000005f5e102');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-00000bebc202');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-000011e1a302');

-- Permissions for entity: Wohnung
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-000005f5e103','buerger_READ_Wohnung');
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-00000bebc203','buerger_WRITE_Wohnung');
insert into _permissions (oid,perm_permission) values ('00000000-0d8e-5714-0000-000011e1a303','buerger_DELETE_Wohnung');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000002','00000000-0d8e-5714-0000-000005f5e103');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-000005f5e103');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-00000bebc203');
insert into _authorities_permissions (authority_oid, permission_oid) values ('00000000-0d8e-5714-0000-000000000001','00000000-0d8e-5714-0000-000011e1a303');

