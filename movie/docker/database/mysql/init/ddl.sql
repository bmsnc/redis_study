CREATE TABLE `member` (
                          `member_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                          `member_name`	VARCHAR(255)	NULL,
                          `created_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                          `updated_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                          `created_author`	BIGINT	NULL,
                          `updated_author`	BIGINT	NULL,
                          PRIMARY KEY (`member_id`)
);

CREATE TABLE `movie` (
                         `movie_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                         `movie_name`	VARCHAR(255)	NULL,
                         `movie_grade`	VARCHAR(255)	NULL	COMMENT 'ENUM',
                         `movie_release_at`	TIMESTAMP	NULL,
                         `movie_image_url`	VARCHAR(255)	NULL,
                         `running_time_minutes`	BIGINT	NULL,
                         `movie_genre`	VARCHAR(255)	NULL	COMMENT 'ENUM',
                         `created_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                         `updated_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                         `created_author`	BIGINT	NULL,
                         `updated_author`	BIGINT	NULL,
                         PRIMARY KEY (`movie_id`)
);

CREATE TABLE `theater` (
                           `theater_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                           `theater_name`	VARCHAR(255)	NULL,
                           `created_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                           `updated_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                           `created_author`	BIGINT	NULL,
                           `updated_author`	BIGINT	NULL,
                           PRIMARY KEY (`theater_id`)
);

CREATE TABLE `schedule` (
                            `schedule_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                            `movie_id`	BIGINT	NOT NULL,
                            `theater_id`	BIGINT	NOT NULL,
                            `movie_start_at`	TIMESTAMP	NULL,
                            `screen_open_at`	TIMESTAMP	NULL,
                            `screen_close_at`	TIMESTAMP	NULL,
                            `created_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                            `updated_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                            `created_author`	BIGINT	NULL,
                            `updated_author`	BIGINT	NULL,
                            PRIMARY KEY (`schedule_id`)
);

CREATE TABLE `seat` (
                        `seat_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                        `theater_id`	BIGINT	NOT NULL,
                        `seat_position`	VARCHAR(255)	NULL,
                        `created_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                        `updated_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                        `created_author`	BIGINT	NULL,
                        `updated_author`	BIGINT	NULL,
                        PRIMARY KEY (`seat_id`)
);

CREATE TABLE `reservation` (
                               `reservation_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                               `member_id`	BIGINT	NOT NULL,
                               `schedule_id`	BIGINT	NOT NULL,
                               `seat_id`	BIGINT	NOT NULL,
                               `created_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                               `updated_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
                               `created_author`	BIGINT	NULL,
                               `updated_author`	BIGINT	NULL,
                               PRIMARY KEY (`reservation_id`)
);


