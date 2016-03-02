	-- This file is for H2

	--**************************
	--Tables
	--**************************
	
	create table Buerger (
	oid varchar(36),
	vorname varchar(255) not null, 
	nachname varchar(255) not null, 
	geburtstag date not null, 
	augenfarbe varchar(255) not null, 
	telefonnummer bigint not null, 
	email varchar(255) not null, 
	lebendig boolean not null, 
	primary key (oid));
	
	create table Pass (
	oid varchar(36),
	passnummer bigint not null, 
	typ varchar(255) not null, 
	kode varchar(255) not null, 
	groesse bigint not null, 
	behoerde varchar(255) not null, 
	primary key (oid));
	
	create table Sachbearbeiter (
	oid varchar(36),
	telefon bigint not null, 
	fax bigint not null, 
	funktion varchar(255) not null, 
	organisationseinheit varchar(255) not null, 
	primary key (oid));
	
	create table Wohnung (
	oid varchar(36),
	stock varchar(255) not null, 
	ausrichtung varchar(255), 
	adresse_strasse varchar(255) not null, 
	adresse_hausnummer bigint not null, 
	adresse_plz bigint not null, 
	adresse_ort varchar(255) not null, 
	primary key (oid));
	
	--Buerger_BisherigeWohnsitze ValueObject Table
	create table buerger_bisherigeWohnsitze (
	buerger_oid varchar(36) not null, 
	order_index bigint,
	strasse varchar(255) not null,
	hausnummer bigint not null,
	plz bigint not null,
	ort varchar(255) not null,
	primary key ( buerger_oid) );
	
	alter table buerger_bisherigeWohnsitze 
	add constraint FK_buerger_bisherigeWohnsitze_TO_buerger 
	foreign key (buerger_oid) 
	references buerger;
	
	--Buerger_Kinder Relation Table
	create table Buerger_Kinder (
	buerger_oid varchar(36) not null, 
	kinder_oid varchar(36) not null, 
	order_index bigint,
	primary key ( buerger_oid, kinder_oid));
	
	alter table Buerger_Kinder 
	add constraint FK_buerger_kinder_TO_kinder 
	foreign key (kinder_oid) 
	references Buerger;
	
	alter table Buerger_Kinder 
	add constraint FK_buerger_kinder_TO_buerger 
	foreign key (buerger_oid) 
	references Buerger;
	
	
	--Buerger_Partner Relation Table
	create table Buerger_Partner (
	buerger_oid varchar(36) not null unique, 
	partner_oid varchar(36) not null unique, 
	primary key ( buerger_oid, partner_oid));
	
	alter table Buerger_Partner 
	add constraint FK_buerger_partner_TO_partner 
	foreign key (partner_oid) 
	references Buerger;
	
	alter table Buerger_Partner 
	add constraint FK_buerger_partner_TO_buerger 
	foreign key (buerger_oid) 
	references Buerger;
	
	
	--Buerger_Hauptwohnung Relation Table
	create table Buerger_Hauptwohnung (
	buerger_oid varchar(36) not null unique, 
	hauptwohnung_oid varchar(36) not null, 
	primary key ( buerger_oid, hauptwohnung_oid));
	
	alter table Buerger_Hauptwohnung 
	add constraint FK_buerger_hauptwohnung_TO_hauptwohnung 
	foreign key (hauptwohnung_oid) 
	references Wohnung;
	
	alter table Buerger_Hauptwohnung 
	add constraint FK_buerger_hauptwohnung_TO_buerger 
	foreign key (buerger_oid) 
	references Buerger;
	
	
	--Buerger_Sachbearbeiter Relation Table
	create table Buerger_Sachbearbeiter (
	buerger_oid varchar(36) not null, 
	sachbearbeiter_oid varchar(36) not null, 
	order_index bigint,
	primary key ( buerger_oid, sachbearbeiter_oid));
	
	alter table Buerger_Sachbearbeiter 
	add constraint FK_buerger_sachbearbeiter_TO_sachbearbeiter 
	foreign key (sachbearbeiter_oid) 
	references Sachbearbeiter;
	
	alter table Buerger_Sachbearbeiter 
	add constraint FK_buerger_sachbearbeiter_TO_buerger 
	foreign key (buerger_oid) 
	references Buerger;
	
	
	--Buerger_Pass Relation Table
	create table Buerger_Pass (
	buerger_oid varchar(36) not null, 
	pass_oid varchar(36) not null unique, 
	order_index bigint,
	primary key ( buerger_oid, pass_oid));
	
	alter table Buerger_Pass 
	add constraint FK_buerger_pass_TO_pass 
	foreign key (pass_oid) 
	references Pass;
	
	alter table Buerger_Pass 
	add constraint FK_buerger_pass_TO_buerger 
	foreign key (buerger_oid) 
	references Buerger;
	
	
	
	
