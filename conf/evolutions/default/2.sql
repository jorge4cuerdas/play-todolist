#Users schema

# --- !Ups

CREATE TABLE usr(
	nombre varchar(255) NOT NULL,
	constraint pk primary key(nombre)
);

ALTER TABLE task add usuario varchar(255);
ALTER TABLE task add constraint fk foreign key (usuario) references usr(nombre);

insert into usr (nombre) values ('Anonimin');
insert into usr (nombre) values ('Jorge');
insert into usr (nombre) values ('Payasin');

# --- !Downs

alter table task drop constraint fk;
alter table task drop usuario;
drop table usr;