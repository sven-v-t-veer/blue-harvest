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

create sequence transaction_id_seq start with 1 increment by 1;

create table transaction (
    id bigint,
    name varchar(40),
    amount numeric(6.2),
    primary key (id)
)
