# java-filmorate
Template repository for Filmorate project.


https://app.quickdatabasediagrams.com/#/d/2V5iw3

Описаниие:
1. Модели - Film, User с указанными полями
2. Лайки поставленные на фильмы находятся в таблице Film_likes, где userID внешний ключ для пользователя, поставившего лайк и filmID соответственно внешний ключ для фильма, которому поставили лайк
3. Список друзей находится в таблице friends, где поле is_confirmed отвечает за статус дружбы - "подтверждена" или "не подтверждена" (буллево значение)
4. также для нормализации была вынесена таблица с айди жанров фильмов

Примеры запросов: 
Друзья определенного пользователя со статусом подтверждено:

SELECT f.friendID
FROM friends as f
JOIN User AS u ON f.userID = u.userID
WHERE f.is_confirmed = 1
GROUP BY f.friendID
