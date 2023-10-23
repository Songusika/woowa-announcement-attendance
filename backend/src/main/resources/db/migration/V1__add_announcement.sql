create table announcement
(
    id               bigint auto_increment primary key,
    title            varchar(255) not null,
    content          text         not null,
    author           varchar(20)  not null,
    slack_channel_id bigint       not null
);
