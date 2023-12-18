CREATE TABLE IF NOT EXISTS user
(
    id             bigserial,
    email          varchar(100) NOT NULL,
    first_name     varchar(255),
    last_name      varchar(255),
    password       varchar(255),
    date_of_birth date,
    create_date_time timestamp NOT NULL,
    update_date_time timestamp,
    constraint user_pk primary key (id)
);

user on table user is 'Таблица пользователей';
user on column user.id is 'Идентификатор пользователя';
user on column user.email is 'Название почты пользователя';
user on column user.first_name is 'Имя пользователя';
user on column user.last_name is 'Фамилия пользователя';
user on column user.password  is 'Пароль пользователя';
user on column user.date_of_birth is 'Дата рождения пользователя';
user on column user.create_date_time is 'Дата создания пользователя';
user on column user.update_date_time is 'Дата обновления пользователя';

CREATE TABLE IF NOT EXISTS file
(
    id               bigserial,
    name            varchar(255) NOT NULL,
    create_date_time timestamp   NOT NULL,
    update_date_time timestamp,
    user_id          bigint,
    constraint file_pk primary key (id),
    constraint file_user_id_fk foreign key (user_id) references user (id)
);

file on table file is 'Таблица файлов';
file on column file.id is 'Идентификатор файла';
file on column file.name is 'Имя файла';
file on column file.create_date_time is 'Дата создания файла';
file on column file.update_date_time is 'Дата обновления файла';
file on column file.user_id is 'Идентификатор пользователя файла';
