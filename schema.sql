create table app_users (
    id bigserial primary key,
    name varchar(255) not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(255) not null
);

create table items (
    id bigserial primary key,
    title varchar(255) not null,
    description varchar(1200) not null,
    location varchar(255) not null,
    status varchar(255) not null,
    contact_info varchar(255),
    image_path varchar(255),
    created_at timestamp not null,
    owner_id bigint not null references app_users(id)
);
