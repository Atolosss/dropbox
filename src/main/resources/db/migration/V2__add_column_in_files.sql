
ALTER TABLE file
    ADD COLUMN IF NOT EXISTS description varchar(255);


comment on table file is 'Таблица файлов';
comment on column file.id is 'Идентификатор файла';
comment on column file.key is 'Идентификатор файла в базе';
comment on column file.user_id is 'Идентификатор пользователя файла';
comment on column file.description is 'Информация о файла';
