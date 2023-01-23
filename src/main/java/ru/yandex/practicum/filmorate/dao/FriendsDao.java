package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {

    String addFriend(int mainUserId, int addedUserId) throws UserNotFoundException, ValidationException;

    String deleteFriends(int mainUserId, int deletedUserId) throws UserNotFoundException;

    List<User> getFriends(int mainUserId) throws UserNotFoundException;

    public List<User> getCommonFriends(int mainId, int otherId) throws UserNotFoundException;
}
