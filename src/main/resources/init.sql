--CREATE DATABASE afisha;

DROP SCHEMA IF EXISTS public;
DROP TABLE  IF EXISTS application.ticket;
DROP TABLE  IF EXISTS application.event;
DROP TABLE  IF EXISTS application.event_type;
DROP TABLE  IF EXISTS application.place;
DROP SCHEMA IF EXISTS application;

CREATE SCHEMA application;

CREATE TABLE application.event_type (id int primary key, name varchar(100));

INSERT INTO application.event_type values (1, 'museum'), (2, 'cinema'), (3, 'theater');

CREATE TABLE application.place (id int primary key, name varchar(100), address varchar(100), city varchar(100));

CREATE TABLE application.event (id int primary key,
								name varchar(100), 
								event_type_id int REFERENCES application.event_type(id), 
								event_date varchar(100), 
								place_id int REFERENCES application.place(id));

CREATE TABLE application.ticket (id int primary key,
								event_id int REFERENCES application.event(id),
								client_email varchar(100),
								price numeric(7, 2),
								is_selled bool default false);





