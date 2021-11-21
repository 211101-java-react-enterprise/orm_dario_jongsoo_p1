drop table if exists flashcards;
drop table if exists app_users;

create table app_users (
    user_id varchar check (user_id <> ''),
    first_name varchar(25) not null check (first_name <> ''),
    last_name varchar(25) not null check (last_name <> ''),
    email varchar(255) unique not null check (email <> ''),
    username varchar(20) unique not null check (username <> ''),
    password varchar(255) not null check (password <> ''),

    constraint app_users_pk
    primary key (user_id)
);

create table flashcards (
    card_id varchar check (card_id <> ''),
    question_text varchar not null check (question_text <> ''),
    answer_text varchar not null check (answer_text <> ''),
    creator_id varchar not null check (creator_id <> ''),

    constraint flashcards_pk
    primary key (card_id),

    constraint flashcard_creator_fk
    foreign key (creator_id)
    references app_users
);

