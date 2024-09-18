
truncate weatherApiTest.locations;
truncate weatherApiTest.session;
delete from weatherApiTest.users where id > 0;

insert into weatherApiTest.users(login, password) VALUES ('testLogin', 'testPassword');