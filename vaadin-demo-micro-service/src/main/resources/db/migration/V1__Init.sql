
create table accounts (id bigint generated by default as identity, oid varchar(30) not null, created_by varchar(255), created_date date, last_mod_by varchar(255), last_mod_date date, mandant bigint, primary key (id));
create table adresse_externe (id bigint generated by default as identity, oid varchar(30) not null, adr_hausnummer varchar(70), adr_plz integer, adr_stadt varchar(20), adr_str varchar(70), mandant bigint, primary key (id));
create table adresse_interne (id bigint generated by default as identity, oid varchar(30) not null, adr_hausnummer varchar(70), adr_strasse varchar(70), mandant bigint, primary key (id));
create table adresse_reference (id bigint generated by default as identity, oid varchar(30) not null, mandant bigint, adr_externe_id bigint, adr_interne_id bigint, primary key (id));
create table authoritys (id bigint generated by default as identity, oid varchar(30) not null, created_by varchar(255), created_date date, last_mod_by varchar(255), last_mod_date date, auth_authority varchar(255), mandant bigint, primary key (id));
create table authoritys_permissions (authority_id bigint, permission_id bigint, primary key (authority_id, permission_id));
create table buerger (id bigint generated by default as identity, oid varchar(30) not null, buer_geburtsdatum date, buer_nachname varchar(70) not null, buer_vorname varchar(70), mandant bigint, sachbearbeiter bigint, primary key (id));
create table buerger_kinder (buerger bigint not null, kinder bigint not null, primary key (buerger, kinder));
create table buerger_pass (buerger bigint not null, pass bigint not null, primary key (buerger, pass));
create table buerger_staatsangehoerigkeit_references (buerger bigint not null, staatsangehoerigkeit_references bigint not null, primary key (buerger, staatsangehoerigkeit_references));
create table buerger_wohnungen (buerger bigint not null, wohnungen bigint not null, primary key (buerger, wohnungen));
create table company_base_infos (id bigint generated by default as identity, oid varchar(30) not null, created_by varchar(255), created_date date, last_mod_by varchar(255), last_mod_date date, comp_adress varchar(255), comp_name varchar(255), mandant bigint, primary key (id));
create table company_base_infos_accounts (company_base_infos bigint not null, accounts bigint not null, primary key (company_base_infos, accounts));
create table mandant (id bigint generated by default as identity, oid varchar(30) not null, primary key (id));
create table pass (id bigint generated by default as identity, oid varchar(30) not null, pass_typ varchar(10), pass_augenfarbe varchar(70), pass_austellungsdatum date, pass_behoerde varchar(70), pass_groesse varchar(20), pass_gueltig_bis date, pass_kode varchar(10), pass_passnummer varchar(70), mandant bigint, staatsangehoerigkeit_reference bigint, primary key (id));
create table permissions (id bigint generated by default as identity, oid varchar(30) not null, created_by varchar(255), created_date date, last_mod_by varchar(255), last_mod_date date, perm_permission varchar(255), mandant bigint, primary key (id));
create table revinfo (rev integer generated by default as identity, revtstmp bigint, primary key (rev));
create table sachbearbeiter (id bigint generated by default as identity, oid varchar(30) not null, sach_firstname varchar(50), sach_lastname varchar(50) not null, mandant bigint, primary key (id));
create table sachbearbeiter_aud (id bigint not null, rev integer not null, revtype tinyint, sach_firstname varchar(50), sach_lastname varchar(50), primary key (id, rev));
create table staatsangehoerigkeit_reference (id bigint generated by default as identity, ref_oid varchar(100) not null, mandant bigint, primary key (id));
create table users (id bigint generated by default as identity, user_birthdate date, created_by varchar(255), created_date date, user_email varchar(255), user_enabled boolean, user_forname varchar(255), last_mod_by varchar(255), last_mod_date date, oid varchar(30) not null, user_password varchar(255), user_surname varchar(255), user_username varchar(255) not null, mandant bigint, primary key (id));
create table users_accounts (users bigint not null, accounts bigint not null, primary key (users, accounts));
create table users_authoritys (user_id bigint, authority_id bigint, primary key (authority_id, user_id));
create table wohnungen (id bigint generated by default as identity, oid varchar(30) not null, wohn_ausrichtung varchar(20), wohn_stock varchar(255), mandant bigint, adresse_id bigint, primary key (id));
create table wohnungen_aud (id bigint not null, rev integer not null, revtype tinyint, wohn_ausrichtung varchar(20), wohn_stock varchar(255), primary key (id, rev));
alter table accounts add constraint UK_OID_ON_ACCOUNTS  unique (oid);
alter table adresse_externe add constraint UK_OID_ON_adresse_externe  unique (oid);
alter table adresse_interne add constraint UK_OID_ON_adresse_interne  unique (oid);
alter table adresse_reference add constraint UK_OID_ON_adresse_reference  unique (oid);
alter table authoritys add constraint UK_OID_ON_authoritys  unique (oid);
alter table buerger add constraint UK_OID_ON_buerger  unique (oid);
alter table buerger_kinder add constraint UK_KINDER_ON_buerger_kinder  unique (kinder);
alter table buerger_pass add constraint UK_PASS_ON_buerger_pass  unique (pass);
alter table buerger_staatsangehoerigkeit_references add constraint UK_OID_ON_buerger_staatsangehoerigkeit_references  unique (staatsangehoerigkeit_references);
alter table buerger_wohnungen add constraint UK_wohnungen_ON_buerger_wohnungen  unique (wohnungen);
alter table company_base_infos add constraint UK_OID_ON_company_base_infos  unique (oid);
alter table company_base_infos_accounts add constraint UK_accounts_ON_company_base_infos_accounts  unique (accounts);
alter table mandant add constraint UK_OID_ON_mandant  unique (oid);
alter table pass add constraint UK_OID_ON_pass  unique (oid);
alter table permissions add constraint UK_OID_ON_permissions  unique (oid);
alter table sachbearbeiter add constraint UK_OID_ON_sachbearbeiter  unique (oid);
alter table users add constraint UK_OID_ON_users  unique (oid);
alter table users_accounts add constraint UK_accounts_ON_users_accounts  unique (accounts);
alter table wohnungen add constraint UK_OID_ON_wohnungen  unique (oid);
alter table accounts add constraint FK_accounts_TO_mandant foreign key (mandant) references mandant;
alter table adresse_externe add constraint FK_adresse_externe_TO_mandant foreign key (mandant) references mandant;
alter table adresse_interne add constraint FK_adresse_interne_TO_mandant foreign key (mandant) references mandant;
alter table adresse_reference add constraint FK_adresse_reference_TO_mandant foreign key (mandant) references mandant;
alter table adresse_reference add constraint FK_adresse_reference_TO_adresse_externe foreign key (adr_externe_id) references adresse_externe;
alter table adresse_reference add constraint FK_adresse_reference_TO_adresse_interne foreign key (adr_interne_id) references adresse_interne;
alter table authoritys add constraint FK_authoritys_TO_mandant foreign key (mandant) references mandant;
alter table authoritys_permissions add constraint FK_authoritys_permissions_TO_authoritys foreign key (authority_id) references authoritys;
alter table authoritys_permissions add constraint FK_authoritys_permissions_TO_permissions foreign key (permission_id) references permissions;
alter table buerger add constraint FK_buerger_TO_mandant foreign key (mandant) references mandant;
alter table buerger add constraint FK_buerger_TO_users foreign key (sachbearbeiter) references users;
alter table buerger_kinder add constraint FK_buerger_kinder_TO_kinder foreign key (kinder) references buerger;
alter table buerger_kinder add constraint FK_buerger_kinder_TO_buerger foreign key (buerger) references buerger;
alter table buerger_pass add constraint FK_buerger_pass_TO_pass foreign key (pass) references pass;
alter table buerger_pass add constraint FK_buerger_pass_TO_buerger foreign key (buerger) references buerger;
alter table buerger_staatsangehoerigkeit_references add constraint FK_buerger_staatsangehoerigkeit_references_TO_staatsangehoerigkeit_reference foreign key (staatsangehoerigkeit_references) references staatsangehoerigkeit_reference;
alter table buerger_staatsangehoerigkeit_references add constraint FK_buerger_staatsangehoerigkeit_references_TO_buerger foreign key (buerger) references buerger;
alter table buerger_wohnungen add constraint FK_buerger_wohnungen_TO_wohnungen foreign key (wohnungen) references wohnungen;
alter table buerger_wohnungen add constraint FK_buerger_wohnungen_TO_buerger foreign key (buerger) references buerger;
alter table company_base_infos add constraint FK_company_base_infos_TO_mandant foreign key (mandant) references mandant;
alter table company_base_infos_accounts add constraint FK_company_base_infos_accounts_TO_accounts foreign key (accounts) references accounts;
alter table company_base_infos_accounts add constraint FK_company_base_infos_accounts_TO_company_base_infos foreign key (company_base_infos) references company_base_infos;
alter table pass add constraint FK_pass_TO_mandant foreign key (mandant) references mandant;
alter table pass add constraint FK_pass_TO_staatsangehoerigkeit_reference foreign key (staatsangehoerigkeit_reference) references staatsangehoerigkeit_reference;
alter table permissions add constraint FK_permissions_TO_mandant foreign key (mandant) references mandant;
alter table sachbearbeiter add constraint FK_sachbearbeiter_TO_mandant foreign key (mandant) references mandant;
alter table sachbearbeiter_aud add constraint FK_sachbearbeiter_aud_TO_revinfo foreign key (rev) references revinfo;
alter table staatsangehoerigkeit_reference add constraint FK_staatsangehoerigkeit_reference_TO_mandant foreign key (mandant) references mandant;
alter table users add constraint FK_users_TO_mandant foreign key (mandant) references mandant;
alter table users_accounts add constraint FK_users_accounts_TO_accounts foreign key (accounts) references accounts;
alter table users_accounts add constraint FK_users_accounts_TO_users foreign key (users) references users;
alter table users_authoritys add constraint FK_users_authoritys_TO_users foreign key (user_id) references users;
alter table users_authoritys add constraint FK_users_authoritys_TO_authoritys foreign key (authority_id) references authoritys;
alter table wohnungen add constraint FK_wohnungen_TO_mandant foreign key (mandant) references mandant;
alter table wohnungen add constraint FK_wohnungen_TO_adresse_reference foreign key (adresse_id) references adresse_reference;
alter table wohnungen_aud add constraint FK_wohnungen_TO_revinfo foreign key (rev) references revinfo;

insert into MANDANT (ID,OID) values ('2','2');

insert  into users ( ID,user_email, user_enabled, oid, user_password, user_username,mandant) values (2,'hans@test.de',TRUE,'oid1','test','hans',2);									

insert into authoritys (id, mandant, oid, auth_authority) values (2, 2, 2, 'ADMIN');

insert  into permissions (id,  mandant,perm_permission,oid) values ('1000','2','PERM_newAdresse','1000');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1001','2','PERM_queryAdresse','1001');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1002','2','PERM_copyAdresse','1002');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1003','2','PERM_readAdresse','1003');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1004','2','PERM_updateAdresse','1004');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1005','2','PERM_saveAdresse','1005');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1006','2','PERM_deleteAdresse','1006');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1007','2','PERM_queryAccount','1007');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1008','2','PERM_newAccount','1008');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1009','2','PERM_readAccount','1009');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1010','2','PERM_updateAccount','1010');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1011','2','PERM_saveAccount','1011');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1012','2','PERM_deleteAccount','1012');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1013','2','PERM_newAuthority','1013');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1014','2','PERM_queryAuthority','1014');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1015','2','PERM_readAuthority','1015');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1016','2','PERM_updateAuthority','1016');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1017','2','PERM_saveAuthority','1017');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1018','2','PERM_deleteAuthority','1018');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1019','2','PERM_copyAuthority','1019');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1020','2','PERM_queryAuthorityPermission','1020');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1021','2','PERM_readByAuthorityAuthorityPermission','1021');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1022','2','PERM_readAuthorityPermission','1022');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1023','2','PERM_saveAuthorityPermission','1023');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1024','2','PERM_deleteAuthorityPermission','1024');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1025','2','PERM_queryBuerger','1025');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1026','2','PERM_newBuerger','1026');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1027','2','PERM_copyBuerger','1027');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1028','2','PERM_readBuerger','1028');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1029','2','PERM_updateBuerger','1029');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1030','2','PERM_saveBuerger','1030');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1031','2','PERM_deleteBuerger','1031');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1032','2','PERM_readBuergerWohnungen','1032');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1033','2','PERM_readBuergerKinder','1033');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1034','2','PERM_createKindBuerger','1034');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1035','2','PERM_addKindBuerger','1035');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1036','2','PERM_addWohnungBuerger','1036');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1037','2','PERM_createWhonungBuerger','1037');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1038','2','PERM_readBuergerStaatsangehoerigkeiten','1038');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1039','2','PERM_addStaatangehoerigkeitBuerger','1039');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1040','2','PERM_addPassBuerger','1040');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1041','2','PERM_readBuergerPass','1041');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1042','2','PERM_queryCompanyBaseInfo','1042');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1043','2','PERM_newCompanyBaseInfo','1043');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1044','2','PERM_readCompanyBaseInfo','1044');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1045','2','PERM_updateCompanyBaseInfo','1045');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1046','2','PERM_saveCompanyBaseInfo','1046');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1047','2','PERM_deleteCompanyBaseInfo','1047');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1048','2','PERM_queryMandant','1048');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1049','2','PERM_newMandant','1049');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1050','2','PERM_copyMandant','1050');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1051','2','PERM_readMandant','1051');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1052','2','PERM_updateMandant','1052');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1053','2','PERM_saveMandant','1053');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1054','2','PERM_deleteMandant','1054');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1055','2','PERM_queryPass','1055');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1056','2','PERM_newPass','1056');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1057','2','PERM_copyPass','1057');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1058','2','PERM_readPass','1058');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1059','2','PERM_updatePass','1059');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1060','2','PERM_savePass','1060');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1061','2','PERM_deletePass','1061');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1062','2','PERM_addStaatangehoerigkeitPass','1062');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1063','2','PERM_readPassStaatsangehoerigkeiten','1063');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1064','2','PERM_queryPermission','1064');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1065','2','PERM_readPermission','1065');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1066','2','PERM_savePermission','1066');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1067','2','PERM_deletePermission','1067');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1068','2','PERM_getPrincipal','1068');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1069','2','PERM_queryStaatsangehoerigkeit','1069');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1070','2','PERM_createStaatsangehoerigkeit','1070');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1071','2','PERM_readStaatsangehoerigkeit','1071');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1072','2','PERM_deleteStaatsangehoerigkeit','1072');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1073','2','PERM_queryUserAuthority','1073');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1074','2','PERM_readByUsernameUserAuthority','1074');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1075','2','PERM_readUserAuthority','1075');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1076','2','PERM_saveUserAuthority','1076');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1077','2','PERM_deleteUserAuthority','1077');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1078','2','PERM_queryUser','1078');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1079','2','PERM_newUser','1079');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1080','2','PERM_readUser','1080');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1081','2','PERM_readUsername','1081');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1082','2','PERM_updateUser','1082');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1083','2','PERM_saveUser','1083');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1084','2','PERM_deleteUser','1084');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1085','2','PERM_addMandantUser','1085');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1086','2','PERM_copyUser','1086');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1087','2','PERM_queryWohnung','1087');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1088','2','PERM_newWohnung','1088');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1089','2','PERM_copyWohnung','1089');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1090','2','PERM_readWohnung','1090');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1091','2','PERM_updateWohnung','1091');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1092','2','PERM_saveWohnung','1092');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1093','2','PERM_deleteWohnung','1093');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1094','2','PERM_readWohnungAdresse','1094');										
insert  into permissions (id,  mandant,perm_permission,oid) values ('1095','2','PERM_addAdresseWohnung','1095');										
								
							
								

insert into authoritys_permissions (authority_id, permission_id) values ('2','1000');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1001');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1002');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1003');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1004');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1005');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1006');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1007');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1008');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1009');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1010');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1011');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1012');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1013');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1014');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1015');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1016');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1017');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1018');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1019');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1020');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1021');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1022');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1023');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1024');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1025');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1026');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1027');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1028');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1029');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1030');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1031');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1032');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1033');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1034');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1035');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1036');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1037');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1038');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1039');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1040');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1041');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1042');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1043');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1044');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1045');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1046');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1047');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1048');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1049');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1050');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1051');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1052');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1053');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1054');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1055');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1056');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1057');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1058');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1059');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1060');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1061');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1062');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1063');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1064');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1065');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1066');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1067');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1068');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1069');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1070');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1071');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1072');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1073');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1074');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1075');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1076');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1077');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1078');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1079');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1080');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1081');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1082');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1083');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1084');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1085');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1086');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1087');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1088');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1089');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1090');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1091');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1092');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1093');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1094');					
insert into authoritys_permissions (authority_id, permission_id) values ('2','1095');					
				
				
insert into users_authoritys (authority_id, user_id) values (2, 2)