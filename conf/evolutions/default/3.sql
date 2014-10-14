#Date schema

# --- !Ups

ALTER TABLE task ADD fecha Date;

# --- !Downs

ALTER TABLE task DROP fecha;