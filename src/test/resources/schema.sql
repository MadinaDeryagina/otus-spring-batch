drop table if exists comments;
drop table if exists books_authors_correlation;
drop table if exists books_genres_correlation;
drop table if exists genres;
create table genres
(
    id   long primary key auto_increment,
    name varchar(255)
);
drop table if exists books;
create table books
(
    id    long primary key auto_increment,
    title varchar(255)
);
drop table if exists authors;
create table authors
(
    id   long primary key auto_increment,
    name varchar(255)
);
create table books_authors_correlation
(
    book_id   long not null,
    author_id long not null,
    foreign key (book_id) references books (id) ON DELETE CASCADE,
    foreign key (author_id) references authors (id) ON DELETE CASCADE
);

create table books_genres_correlation
(
    book_id   long not null,
    genre_id long not null,
    foreign key (book_id) references books (id) ON DELETE CASCADE,
    foreign key (genre_id) references genres (id) ON DELETE CASCADE
);
create table comments
(
    id long primary key auto_increment,
    book_id long not null,
    text varchar(2000),
    foreign key (book_id) references books (id) ON DELETE CASCADE
);