package com.andrewhun.finance.usermapper;

import com.andrewhun.finance.config.AppContext;
import com.andrewhun.finance.config.StorageMode;

/**
 * Supplies the {@link UserMapper} for the storage mode selected at startup. This is the
 * only place in the application that names a concrete mapper; everything else asks here
 * and works through the interface.
 *
 * <p>The mapper is built once and shared, so an in-memory backend keeps the users written
 * to it for the life of the run.
 */
public class UserMapperFactory {

    private static UserMapper userMapper;

    private UserMapperFactory() {
    }

    public static synchronized UserMapper create() {

        if (userMapper == null) {
            userMapper = build(AppContext.storageMode());
        }
        return userMapper;
    }

    private static UserMapper build(StorageMode storageMode) {

        if (storageMode == StorageMode.IN_MEMORY) {
            return new InMemoryUserMapper();
        }
        return new JdbcUserMapper();
    }

    /**
     * Test-support hook: discards the built mapper, and with it any in-memory data it was
     * holding. Kept separate from {@link AppContext#reset()} so a test can re-select the
     * storage mode for a fresh application launch while the data it seeded stays put.
     */
    public static synchronized void reset() {
        userMapper = null;
    }
}
