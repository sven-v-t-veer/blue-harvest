CREATE SCHEMA IF NOT EXISTS BLUEHARVEST;
create table user (
    user_id uuid,
    name varchar(40) not null,
    primary key (user_id)
);


create table account (
    account_id uuid,
    short_name varchar(20) not null,
    description varchar(50) not null,
    balance numeric (6,2) not null,
    primary key (account_id)
);
