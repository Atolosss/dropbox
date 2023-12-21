CREATE TABLE IF NOT EXISTS users
(
    id               bigserial,
    email            varchar(100) NOT NULL,
    password         varchar(255),
    first_name       varchar(255),
    last_name        varchar(255),
    date_of_birth    date,
    create_date_time timestamp    NOT NULL,
    update_date_time timestamp,
    constraint users_pk primary key (id)
);

comment on table users is 'Таблица пользователей';
comment on column users.id is 'Идентификатор пользователя';
comment on column users.email is 'Название почты пользователя';
comment on column users.password is 'Пароль пользователя';
comment on column users.first_name is 'Имя пользователя';
comment on column users.last_name is 'Фамилия пользователя';
comment on column users.date_of_birth is 'Дата рождения пользователя';
comment on column users.create_date_time is 'Дата создания пользователя';
comment on column users.update_date_time is 'Дата обновления пользователя';

CREATE TABLE IF NOT EXISTS file
(
    id               bigserial,
    name             varchar(255) NOT NULL,
    url              varchar(255) NOT NULL,
    file_type        varchar(50),
    create_date_time timestamp    NOT NULL,
    update_date_time timestamp,
    user_id          bigint,
    constraint file_pk primary key (id),
    constraint file_user_id_fk foreign key (user_id) references users (id)
);

create index if not exists file_user_id_without_nulls_idx
    on file (user_id) where user_id is not null;

comment on table file is 'Таблица файлов';
comment on column file.id is 'Идентификатор файла';
comment on column file.name is 'Имя файла';
comment on column file.url is 'Адрес хранения файла';
comment on column file.create_date_time is 'Дата создания файла';
comment on column file.update_date_time is 'Дата обновления файла';
comment on column file.user_id is 'Идентификатор пользователя файла';
