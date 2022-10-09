package com.techcourse.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.techcourse.domain.User;

import nextstep.jdbc.DataAccessException;

public class TxUserService implements UserService {
    private final PlatformTransactionManager transactionManager;
    private final UserService userService;

    public TxUserService(PlatformTransactionManager transactionManager, UserService userService) {
        this.transactionManager = transactionManager;
        this.userService = userService;
    }

    @Override
    public User findById(final long id) {
        return userService.findById(id);
    }

    @Override
    public void insert(final User user) {
        userService.insert(user);
    }

    @Override
    public void changePassword(final long id, final String newPassword, final String createBy) {

        final var transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            userService.changePassword(id, newPassword, createBy);
            transactionManager.commit(transactionStatus);
        } catch (DataAccessException e) {
            transactionManager.rollback(transactionStatus);
            throw e;
        }
    }
}
