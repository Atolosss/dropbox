
ALTER TABLE users
DROP COLUMN IF EXISTS phone_number;

ALTER TABLE users
    ADD COLUMN keys varchar(100) ARRAY;



-- Комментарии к таблице и колонкам
COMMENT ON TABLE users IS 'Таблица пользователей';
COMMENT ON COLUMN users.id IS 'Идентификатор пользователя';
COMMENT ON COLUMN users.telegram_user_id IS 'Идентификатор пользователя в Telegram';
COMMENT ON COLUMN users.first_name IS 'Имя пользователя';
COMMENT ON COLUMN users.last_name IS 'Фамилия пользователя';
COMMENT ON COLUMN users.create_date_time IS 'Дата создания пользователя';
COMMENT ON COLUMN users.keys IS 'Список ключей для файлов а MongoDb';
