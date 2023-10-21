create table announcement
(
    id bigint auto_increment primary key,
    title varchar(255) not null,
    content varchar not null,
    author varchar not null,
    slack_channel_id bigint not null
)
