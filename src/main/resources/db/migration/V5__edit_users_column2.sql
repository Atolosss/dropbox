
ALTER TABLE users
DROP COLUMN IF EXISTS is_active;



-- Комментарии к таблице и колонкам
COMMENT ON TABLE users IS 'Таблица пользователей';
COMMENT ON COLUMN users.id IS 'Идентификатор пользователя';
COMMENT ON COLUMN users.telegram_user_id IS 'Идентификатор пользователя в Telegram';
COMMENT ON COLUMN users.phone_number IS 'Номер телефона пользователя';
COMMENT ON COLUMN users.first_name IS 'Имя пользователя';
COMMENT ON COLUMN users.last_name IS 'Фамилия пользователя';
COMMENT ON COLUMN users.create_date_time IS 'Дата создания пользователя';

