create table announcement
(
    id               bigint auto_increment primary key,
    title            varchar(255)   not null,
    content          varchar(65535) not null,
    author           varchar(20) not null,
    slack_channel_id bigint         not null,
    created_at       datetime       not null
);

create table campus
(
    id        bigint auto_increment primary key,
    name      varchar(255)   not null,
    drawing   varchar(65535) not null,
    thumbnail varchar(65535) not null
);

create table announcement_slack_channel
(
    id          bigint auto_increment primary key,
    provider_id varchar(255) not null,
    name        varchar(80)  not null
);

