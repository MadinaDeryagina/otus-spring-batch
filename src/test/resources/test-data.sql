insert into GENRES(id, name)
values (1, 'Poetry');
insert into GENRES(id, name)
values (2, 'Drama');
insert into GENRES(id, name)
values (3, 'Prose');
insert into GENRES(id, name)
values (4, 'Nonfiction');
insert into GENRES(id, name)
values (5, 'Media');

insert into BOOKS(id, title)
values (1, 'My book');
insert into AUTHORS(id, name)
values (1, 'First author');
insert into AUTHORS(id, name)
VALUES (2, 'Second author');
insert into BOOKS_AUTHORS_CORRELATION
values (1, 1);
insert into BOOKS_AUTHORS_CORRELATION
values (1, 2);
insert into BOOKS_GENRES_CORRELATION
values (1, 2);
insert into BOOKS_GENRES_CORRELATION
values (1, 3);

insert into BOOKS(id, title)
values (2, 'Second book');
insert into BOOKS_AUTHORS_CORRELATION
values (2, 1);
insert into BOOKS_GENRES_CORRELATION
values (2, 1);

insert into BOOKS(id, TITLE)
values (3, 'Same title dif authors');
insert into BOOKS_AUTHORS_CORRELATION
values (3, 1);
insert into BOOKS_GENRES_CORRELATION
values (3, 4);

insert into BOOKS(id, TITLE)
values (4, 'Same title dif authors');
insert into BOOKS_AUTHORS_CORRELATION
values (4, 2);
insert into BOOKS_GENRES_CORRELATION
values (4, 3);
insert into COMMENTS(id, book_id, text)
values ( 1,1,'my test comment' );
insert into COMMENTS(id, book_id, text)
values ( 2,1,'my test comment 2' );