insert into users (oid, user_email, user_enabled, user_username, user_password, mandant) values (1,'admin@admin.muenchen.de',TRUE,'admin1','admin1','m1');
insert into users (oid, user_email, user_enabled, user_username, user_password, mandant) values (2,'admin@admin.muenchen.de',TRUE,'admin2','admin2','m2');

insert into users (oid, user_email, user_enabled, user_username, user_password, mandant) values (3,'admin@admin.muenchen.de',TRUE,'read1','read1','m1');
insert into users (oid, user_email, user_enabled, user_username, user_password, mandant) values (4,'admin@admin.muenchen.de',TRUE,'read2','read2','m2');

insert into users (oid, user_email, user_enabled, user_username, user_password, mandant) values (5,'admin@admin.muenchen.de',TRUE,'hans','test','m1');

insert into authorities (oid, auth_authority) values (1,'ADMIN');
insert into authorities (oid, auth_authority) values (2,'READ_ONLY_USER');

insert into users_authorities (authority_oid, user_oid) values (1, 1);
insert into users_authorities (authority_oid, user_oid) values (1, 2);

insert into users_authorities (authority_oid, user_oid) values (2, 3);
insert into users_authorities (authority_oid, user_oid) values (2, 4);

insert into users_authorities (authority_oid, user_oid) values (1, 5);

insert into authorities (oid, auth_authority) values (100,'Tester');
insert into users_authorities (authority_oid, user_oid) values (100, 1);

insert into authorities (oid, auth_authority) values (101,'name');
insert into users_authorities (authority_oid, user_oid) values (101, 1);

insert into permissions (oid,perm_permission) values ('100000','ROLE_READ_Buerger');
insert into permissions (oid,perm_permission) values ('200000','ROLE_WRITE_Buerger');
insert into permissions (oid,perm_permission) values ('300000','ROLE_DELETE_Buerger');
insert into authorities_permissions (authority_oid, permission_oid) values ('2','100000');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','100000');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','200000');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','300000');

insert into permissions (oid,perm_permission) values ('100001','ROLE_READ_Wohnung');
insert into permissions (oid,perm_permission) values ('200001','ROLE_WRITE_Wohnung');
insert into permissions (oid,perm_permission) values ('300001','ROLE_DELETE_Wohnung');
insert into authorities_permissions (authority_oid, permission_oid) values ('2','100001');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','100001');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','200001');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','300001');

insert into permissions (oid,perm_permission) values ('100002','ROLE_READ_Adresse');
insert into permissions (oid,perm_permission) values ('200002','ROLE_WRITE_Adresse');
insert into permissions (oid,perm_permission) values ('300002','ROLE_DELETE_Adresse');
insert into authorities_permissions (authority_oid, permission_oid) values ('2','100002');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','100002');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','200002');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','300002');

insert into permissions (oid,perm_permission) values ('100003','ROLE_READ_AdresseExtern');
insert into permissions (oid,perm_permission) values ('200003','ROLE_WRITE_AdresseExtern');
insert into permissions (oid,perm_permission) values ('300003','ROLE_DELETE_AdresseExtern');
insert into authorities_permissions (authority_oid, permission_oid) values ('2','100003');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','100003');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','200003');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','300003');

insert into permissions (oid,perm_permission) values ('100004','ROLE_READ_AdresseIntern');
insert into permissions (oid,perm_permission) values ('200004','ROLE_WRITE_AdresseIntern');
insert into permissions (oid,perm_permission) values ('300004','ROLE_DELETE_AdresseIntern');
insert into authorities_permissions (authority_oid, permission_oid) values ('2','100004');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','100004');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','200004');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','300004');

insert into permissions (oid,perm_permission) values ('100005','ROLE_READ_Staatsangehoerigkeit');
insert into permissions (oid,perm_permission) values ('200005','ROLE_WRITE_Staatsangehoerigkeit');
insert into permissions (oid,perm_permission) values ('300005','ROLE_DELETE_Staatsangehoerigkeit');
insert into authorities_permissions (authority_oid, permission_oid) values ('2','100005');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','100005');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','200005');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','300005');

insert into permissions (oid,perm_permission) values ('100006','ROLE_READ_Pass');
insert into permissions (oid,perm_permission) values ('200006','ROLE_WRITE_Pass');
insert into permissions (oid,perm_permission) values ('300006','ROLE_DELETE_Pass');
insert into authorities_permissions (authority_oid, permission_oid) values ('2','100006');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','100006');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','200006');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','300006');

insert into permissions (oid,perm_permission) values ('100007','ROLE_READ_Sachbearbeiter');
insert into permissions (oid,perm_permission) values ('200007','ROLE_WRITE_Sachbearbeiter');
insert into permissions (oid,perm_permission) values ('300007','ROLE_DELETE_Sachbearbeiter');
insert into authorities_permissions (authority_oid, permission_oid) values ('2','100007');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','100007');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','200007');
insert into authorities_permissions (authority_oid, permission_oid) values ('1','300007');

insert into permissions (oid,perm_permission) values ('900000','ROLE_BUSINESSACTION_BuergerAlsMailVerschicken');
insert into authorities_permissions (authority_oid, permission_oid) values (100,'900000');

insert into permissions (oid,perm_permission) values ('900001','ROLE_BUSINESSACTION_BuergerKombinieren');
insert into authorities_permissions (authority_oid, permission_oid) values (100,'900001');

insert into permissions (oid,perm_permission) values ('900002','ROLE_BUSINESSACTION_ListeWohnungAuswaehlen');
insert into authorities_permissions (authority_oid, permission_oid) values (100,'900002');

