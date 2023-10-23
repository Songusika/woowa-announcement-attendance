create table slack_channel
(
    id          bigint auto_increment primary key,
    provider_id varchar(255) not null,
    name        varchar(80)  not null
);
