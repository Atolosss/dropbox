ALTER TABLE users
DROP COLUMN IF EXISTS keys_mongo_db;

ALTER TABLE users
    ADD COLUMN files varchar(100) ARRAY;
CREATE TABLE IF NOT EXISTS file
(
    id               bigserial,
    key bigint,
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

