package com.dvlasenko.app.service;

import com.dvlasenko.app.entity.User;
import com.dvlasenko.app.entity.UserMapper;
import com.dvlasenko.app.exceptions.UserException;
import com.dvlasenko.app.repository.impl.UserRepository;
import com.dvlasenko.app.utils.UserValidator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UserService {

    final UserRepository repository = new UserRepository();

    public void create(Map<String, String> data) {
        Map<String, String> errors =
                new UserValidator().validateUserData(data);
        if (!errors.isEmpty()) {
            try {
                throw new UserException("Check inputs", errors);
            } catch (UserException e) {
                e.getErrors(errors);
                return;
            }
        }
        repository.create(new UserMapper().mapData(data));
    }

    public List<User> read() {
        Optional<List<User>> optional = repository.read();
        if (optional.isPresent()) {
            List<User> list = optional.get();
            if (!list.isEmpty()) {
                AtomicInteger count = new AtomicInteger(0);
                StringBuilder sb = new StringBuilder();
                list.forEach((user) ->
                        sb.append(count.incrementAndGet())
                                .append(") ")
                                .append(user.toString())
                );
            }
            return list;
        }
        return Collections.emptyList();
    }

    public void update(Map<String, String> data) {
        Map<String, String> errors =
                new UserValidator().validateUserData(data);
        if (!errors.isEmpty()) {
            try {
                throw new UserException("Check inputs", errors);
            } catch (UserException e) {
                e.getErrors(errors);
                return;
            }
        }
        repository.update(new UserMapper().mapData(data));
    }

    public void delete(Map<String, String> data) {
        Map<String, String> errors =
                new UserValidator().validateUserData(data);
        if (!errors.isEmpty()) {
            try {
                throw new UserException("Check inputs", errors);
            } catch (UserException e) {
                e.getErrors(errors);
                return;
            }
        }
        repository.delete(new UserMapper().mapData(data).getId());
    }

    public Object readById(Map<String, String> data) {
        Optional<User> optional =
                repository.readById(Long.parseLong(data.get("id")));
        if (optional.isPresent()) {
            User user = optional.get();
            return user.toString();
        }
        return null;
    }
}
