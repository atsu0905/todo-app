-- Todo schema

-- !Ups
CREATE TABLE todo (
    id      bigint(20)    NOT NULL AUTO_INCREMENT,
    content varchar(120)  NOT NULL,
    -- created_at timestamp NOT NULL,
    -- updated_at timestamp NOT NULL
    PRIMARY KEY (id)
);

-- !Downs
DROP TABLE todo;