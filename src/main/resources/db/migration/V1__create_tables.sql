create table users
(
    id         bigserial    not null,
    email      varchar(255) not null,
    password   varchar(255) not null,
    name       varchar(255) not null,
    role       varchar(20)  not null,
    created_at timestamp    not null,
    updated_at timestamp,
    primary key (id),
    constraint user_email_unique unique (email)
);

create table posts
(
    id         bigserial    not null,
    title      varchar(250) not null,
    slug       varchar(300) not null,
    content    text         not null,
    created_by bigint       not null references users (id),
    created_at timestamp    not null,
    updated_at timestamp,
    primary key (id),
    constraint posts_slug_unique unique (slug)
);

create table comments
(
    id         bigserial    not null,
    post_id    bigint       not null references posts (id),
    name       varchar(150) not null,
    email      varchar(150),
    content    text         not null,
    created_at timestamp    not null,
    updated_at timestamp,
    primary key (id)
);
