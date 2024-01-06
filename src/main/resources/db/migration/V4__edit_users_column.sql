ALTER TABLE users
    ADD COLUMN IF NOT EXISTS telegram_user_id bigint;

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS phone_number varchar(20);

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS is_active boolean;

ALTER TABLE users
DROP COLUMN IF EXISTS email;

ALTER TABLE users
DROP COLUMN IF EXISTS password;

ALTER TABLE users
DROP COLUMN IF EXISTS date_of_birth;

-- Добавление новых индексов, если необходимо
CREATE INDEX IF NOT EXISTS idx_telegram_user_id ON users(telegram_user_id);
-- ... (добавьте другие индексы по необходимости)

-- Комментарии к таблице и колонкам
COMMENT ON TABLE users IS 'Таблица пользователей';
COMMENT ON COLUMN users.id IS 'Идентификатор пользователя';
COMMENT ON COLUMN users.telegram_user_id IS 'Идентификатор пользователя в Telegram';
COMMENT ON COLUMN users.phone_number IS 'Номер телефона пользователя';
COMMENT ON COLUMN users.first_name IS 'Имя пользователя';
COMMENT ON COLUMN users.last_name IS 'Фамилия пользователя';
COMMENT ON COLUMN users.create_date_time IS 'Дата создания пользователя';
COMMENT ON COLUMN users.is_active IS 'Флаг активности пользователя';
