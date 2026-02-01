-- База данных: Музыкальный стриминговый сервис

-- Страны
CREATE TABLE countries (
    country_id SERIAL PRIMARY KEY,
    country_name VARCHAR(100) NOT NULL UNIQUE,
    country_code CHAR(2) NOT NULL UNIQUE
);

-- Типы подписок 
CREATE TABLE subscription_types (
    subscription_type_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    price_monthly DECIMAL(10, 2) NOT NULL,
    max_devices INT NOT NULL DEFAULT 1,
    has_ads BOOLEAN NOT NULL DEFAULT TRUE,
    offline_listening BOOLEAN NOT NULL DEFAULT FALSE,
    audio_quality VARCHAR(20) NOT NULL DEFAULT 'standard'
        CHECK (audio_quality IN ('low', 'standard', 'high', 'lossless'))
);

-- Жанры музыки
CREATE TABLE genres (
    genre_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

-- Типы альбомов
CREATE TABLE album_types (
    album_type_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Звукозаписывающие лейблы
CREATE TABLE labels (
    label_id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    country_id INT REFERENCES countries(country_id) 
	    ON DELETE SET NULL,
    founded_year INT CHECK (founded_year > 1800 AND founded_year <= EXTRACT(YEAR FROM CURRENT_DATE)),
    website_url VARCHAR(500)
);

-- Пользователи сервиса
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    username VARCHAR(50) NOT NULL,
    birth_date DATE CHECK (birth_date < CURRENT_DATE),
    country_id INT REFERENCES countries(country_id) 
	   ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Подписки пользователей
CREATE TABLE user_subscriptions (
    user_subscription_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) 
	    ON DELETE CASCADE,
    subscription_type_id INT NOT NULL REFERENCES subscription_types(subscription_type_id),
    start_date DATE NOT NULL,
    end_date DATE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT chk_subscription_dates 
	    CHECK (end_date IS NULL OR end_date >= start_date)
);

-- Исполнители
CREATE TABLE artists (
    artist_id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    bio TEXT,
    country_id INT REFERENCES countries(country_id) 
        ON DELETE SET NULL,
    formed_year INT CHECK (formed_year > 1900),
    image_url VARCHAR(500),
    monthly_listeners BIGINT NOT NULL DEFAULT 0 
	    CHECK (monthly_listeners >= 0),
    verified BOOLEAN NOT NULL DEFAULT FALSE
);

-- Альбомы
CREATE TABLE albums (
    album_id SERIAL PRIMARY KEY,
    title VARCHAR(300) NOT NULL,
    release_date DATE,
    cover_image_url VARCHAR(500),
    album_type_id INT REFERENCES album_types(album_type_id) 
        ON DELETE SET NULL,
    label_id INT REFERENCES labels(label_id) 
        ON DELETE SET NULL,
    total_tracks INT NOT NULL DEFAULT 0 CHECK (total_tracks >= 0),
    copyright_text VARCHAR(500)
);

-- Треки
CREATE TABLE tracks (
    track_id SERIAL PRIMARY KEY,
    title VARCHAR(300) NOT NULL,
    duration_seconds INT NOT NULL CHECK (duration_seconds > 0),
    explicit BOOLEAN NOT NULL DEFAULT FALSE,
    audio_file_url VARCHAR(500) NOT NULL,
    lyrics TEXT,
    play_count BIGINT NOT NULL DEFAULT 0 CHECK (play_count >= 0),
    isrc CHAR(12) UNIQUE
);

-- Плейлисты
CREATE TABLE playlists (
    playlist_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) 
        ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Жанры исполнителей
CREATE TABLE artist_genres (
    artist_id INT NOT NULL REFERENCES artists(artist_id) 
        ON DELETE CASCADE,
    genre_id INT NOT NULL REFERENCES genres(genre_id) 
        ON DELETE CASCADE,
    PRIMARY KEY (artist_id, genre_id)
);

-- Исполнители альбомов
CREATE TABLE album_artists (
    album_id INT NOT NULL REFERENCES albums(album_id) 
        ON DELETE CASCADE,
    artist_id INT NOT NULL REFERENCES artists(artist_id) 
        ON DELETE CASCADE,
    is_primary  BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (album_id, artist_id)
);

-- Треки в альбомах
CREATE TABLE album_tracks (
    album_id INT NOT NULL REFERENCES albums(album_id) 
	   ON DELETE CASCADE,
    track_id INT NOT NULL REFERENCES tracks(track_id) 
       ON DELETE CASCADE,
    disc_number INT NOT NULL DEFAULT 1 CHECK (disc_number > 0),
    track_number INT NOT NULL CHECK (track_number > 0),
    PRIMARY KEY (album_id, track_id),
    UNIQUE (album_id, disc_number, track_number)
);

-- Исполнители треков
CREATE TABLE track_artists (
    track_id INT NOT NULL REFERENCES tracks(track_id) 
        ON DELETE CASCADE,
    artist_id INT NOT NULL REFERENCES artists(artist_id) 
        ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL DEFAULT 'main'
	    CHECK (role IN ('main', 'featured', 'producer', 'composer', 'lyricist')),
    PRIMARY KEY (track_id, artist_id, role)
);

-- Жанры треков
CREATE TABLE track_genres (
    track_id INT NOT NULL REFERENCES tracks(track_id) 
       ON DELETE CASCADE,
    genre_id INT NOT NULL REFERENCES genres(genre_id) 
	   ON DELETE CASCADE,
    PRIMARY KEY (track_id, genre_id)
);

-- Треки в плейлистах
CREATE TABLE playlist_tracks (
    playlist_id INT NOT NULL REFERENCES playlists(playlist_id) 
        ON DELETE CASCADE,
    track_id INT NOT NULL REFERENCES tracks(track_id) 
	    ON DELETE CASCADE,
    position INT NOT NULL CHECK (position > 0),
    added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (playlist_id, track_id),
    UNIQUE (playlist_id, position)
);

-- История прослушиваний
CREATE TABLE listening_history (
    history_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) 
	    ON DELETE CASCADE,
    track_id INT NOT NULL REFERENCES tracks(track_id) 
        ON DELETE CASCADE,
    listened_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duration_listened INT NOT NULL CHECK (duration_listened > 0),
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    device_type VARCHAR(50)
	    CHECK (device_type IN ('mobile', 'desktop', 'web', 'smart_speaker', 'tv'))
);

-- Лайки треков
CREATE TABLE track_likes (
    user_id INT NOT NULL REFERENCES users(user_id) 
	    ON DELETE CASCADE,
    track_id INT NOT NULL REFERENCES tracks(track_id) 
        ON DELETE CASCADE,
    liked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, track_id)
);

-- Подписки на исполнителей
CREATE TABLE artist_followers (
    user_id INT NOT NULL REFERENCES users(user_id) 
        ON DELETE CASCADE,
    artist_id INT NOT NULL REFERENCES artists(artist_id) 
        ON DELETE CASCADE,
    followed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, artist_id)
);

-- Сохраненные альбомы
CREATE TABLE saved_albums (
    user_id INT NOT NULL REFERENCES users(user_id) 
	    ON DELETE CASCADE,
    album_id INT NOT NULL REFERENCES albums(album_id) 
        ON DELETE CASCADE,
    saved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  
    PRIMARY KEY (user_id, album_id)
);

-- Подписки на плейлисты
CREATE TABLE playlist_followers (
    user_id INT NOT NULL REFERENCES users(user_id) 
        ON DELETE CASCADE,
    playlist_id INT NOT NULL REFERENCES playlists(playlist_id) 
        ON DELETE CASCADE,
    followed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, playlist_id)
);