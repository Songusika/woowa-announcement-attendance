create table space
(
    id                  bigint auto_increment primary key,
    campus_id           bigint      not null,
    name                varchar(80) not null,
    color               varchar(80) not null,
    area                text        not null,
    reservation_enabled boolean not null
);

create table setting
(
    id             bigint auto_increment primary key,
    space_id       bigint       not null,
    start_time     time         not null,
    end_time       time         not null,
    maximum_minute bigint       not null,
    enable_days    varchar(255) not null
);
