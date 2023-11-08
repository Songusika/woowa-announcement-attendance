create table announcement
(
    id               bigint auto_increment primary key,
    title            varchar(255)   not null,
    content          varchar(65535) not null,
    author           varchar(65535) not null,
    slack_channel_id bigint         not null
);

create table campus
(
    id        bigint auto_increment primary key,
    name      varchar(255)   not null,
    drawing   varchar(65535) not null,
    thumbnail varchar(65535) not null
);
