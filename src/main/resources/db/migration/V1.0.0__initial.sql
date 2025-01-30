CREATE SCHEMA IF NOT EXISTS BLUEHARVEST;
create table customer (
    customer_id uuid,
    name varchar(40) not null,
    sur_name varchar(40) not null,
    primary key (customer_id)
);


create table account (
    account_id uuid,
    customer_id uuid,
    balance numeric (6,2) not null,
    primary key (account_id)
);

create sequence transaction_id_seq start with 1 increment by 1;

create table transaction (
    transaction_id bigint,
    account_id uuid,
    name varchar(40),
    amount numeric(6.2),
    primary key (transaction_id)
)
