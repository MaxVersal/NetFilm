merge into genre(id, NAME) values (1, 'Комедия'),(2, 'Драма'),(3,'Мультфильм'), (4, 'Триллер'), (5, 'Документальный'),
                                  (6, 'Боевик');
merge into STATUS(STATUS_ID, NAME) values ( 1, 'добавлен' ), (2, 'не добавлен');
merge into MPA(RATING_ID, NAME) values ( 1, 'G' ), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');