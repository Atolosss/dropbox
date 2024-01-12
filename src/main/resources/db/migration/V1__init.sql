CREATE TABLE IF NOT EXISTS users
(
    id               bigserial,
    telegram_user_id            varchar(100),
    first_name       varchar(255),
    last_name        varchar(255),
    create_date_time timestamp    NOT NULL,
    constraint users_pk primary key (id)
);

comment on table users is 'Таблица пользователей';
comment on column users.id is 'Идентификатор пользователя';
comment on column users.telegram_user_id is 'Идентификатор пользрвателя в телеграм';
comment on column users.first_name is 'Имя пользователя';
comment on column users.last_name is 'Фамилия пользователя';
comment on column users.create_date_time is 'Дата создания пользователя';

CREATE TABLE IF NOT EXISTS file
(
    id               bigserial,
    key varchar(100),
    user_id          bigint,
    constraint file_pk primary key (id),
    constraint file_user_id_fk foreign key (user_id) references users (id)
    );

create index if not exists file_user_id_without_nulls_idx
    on file (user_id) where user_id is not null;

comment on table file is 'Таблица файлов';
comment on column file.id is 'Идентификатор файла';
comment on column file.key is 'Ключ к файлу в MongoDb';
comment on column file.user_id is 'Идентификатор пользователя файла';
