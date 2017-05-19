package com.eparty.ccp.redis.lock;

import com.eparty.ccp.contract.exception.ServiceException;
import com.eparty.ccp.contract.exception.SystemException;
import com.joindata.inf.common.support.redis.component.RedisClient;
import com.joindata.inf.common.util.log.Logger;

import java.util.UUID;

public class RedisLock {

    private Logger logger = Logger.get();

    private static final Lock NO_LOCK = new Lock(new UUID(0l, 0l), 0l);

    private static final int ONE_SECOND = 1000;

    public static final int DEFAULT_EXPIRY_TIME_MILLIS = 60 * ONE_SECOND;
    public static final int DEFAULT_ACQUIRE_TIMEOUT_MILLIS = 10 * ONE_SECOND;
    public static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;

    private final RedisClient redisClient;

    private final String lockKeyPath;

    private final int lockExpiryInMillis;
    private final int acquiryTimeoutInMillis;
    private final UUID lockUUID;

    private Lock lock = null;

    protected static class Lock {
        private UUID uuid;
        private long expiryTime;

        protected Lock(UUID uuid, long expiryTimeInMillis) {
            this.uuid = uuid;
            this.expiryTime = expiryTimeInMillis;
        }

        protected static Lock fromString(String text) {
            try {
                String[] parts = text.split(":");
                UUID theUUID = UUID.fromString(parts[0]);
                long theTime = Long.parseLong(parts[1]);
                return new Lock(theUUID, theTime);
            } catch (Exception any) {
                return NO_LOCK;
            }
        }

        public UUID getUUID() {
            return uuid;
        }

        public long getExpiryTime() {
            return expiryTime;
        }

        @Override
        public String toString() {
            return uuid.toString() + ":" + expiryTime;
        }

        boolean isExpired() {
            return getExpiryTime() < System.currentTimeMillis();
        }

        boolean isExpiredOrMine(UUID otherUUID) {
            return this.isExpired() || this.getUUID().equals(otherUUID);
        }
    }


    /**
     * Detailed constructor with default acquire timeout 10000 msecs and lock
     * expiration of 60000 msecs.
     * @param redisClient
     * @param lockKey
     */
    public RedisLock(RedisClient redisClient, String lockKey) {
        this(redisClient, lockKey, DEFAULT_ACQUIRE_TIMEOUT_MILLIS, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    /**
     * Detailed constructor with default lock expiration of 60000 msecs.
     *
     * @param redisClient
     * @param lockKey              lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis acquire timeout in miliseconds (default: 10000 msecs)
     */
    public RedisLock(RedisClient redisClient, String lockKey, int acquireTimeoutMillis) {
        this(redisClient, lockKey, acquireTimeoutMillis, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    /**
     * Detailed constructor.
     *
     * @param redisClient
     * @param lockKey              lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis acquire timeout in miliseconds (default: 10000 msecs)
     * @param expiryTimeMillis     lock expiration in miliseconds (default: 60000 msecs)
     */
    public RedisLock(RedisClient redisClient, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis) {
        this(redisClient, lockKey, acquireTimeoutMillis, expiryTimeMillis, UUID.randomUUID());
    }

    /**
     * Detailed constructor.
     *
     * @param redisClient
     * @param lockKey              lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis acquire timeout in miliseconds (default: 10000 msecs)
     * @param expiryTimeMillis     lock expiration in miliseconds (default: 60000 msecs)
     * @param uuid                 unique identification of this lock
     */
    public RedisLock(RedisClient redisClient, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis, UUID uuid) {
        this.redisClient = redisClient;
        this.lockKeyPath = lockKey;
        this.acquiryTimeoutInMillis = acquireTimeoutMillis;
        this.lockExpiryInMillis = expiryTimeMillis + 1;
        this.lockUUID = uuid;
    }

    /**
     * @return lock uuid
     */
    public UUID getLockUUID() {
        return lockUUID;
    }

    /**
     * @return lock key path
     */
    public String getLockKeyPath() {
        return lockKeyPath;
    }

    /**
     * Acquire lock.
     *
     * @return true if lock is acquired, false acquire timeouted
     * @throws InterruptedException in case of thread interruption
     */
    public synchronized boolean acquire() throws InterruptedException {
        return acquire(redisClient);
    }

    /**
     * Acquire lock.
     *
     * @param redisClient
     * @return true if lock is acquired, false acquire timeouted
     * @throws InterruptedException in case of thread interruption
     */
    protected synchronized boolean acquire(RedisClient redisClient) throws InterruptedException {
        int timeout = acquiryTimeoutInMillis;
        while (timeout >= 0) {

            final Lock newLock = asLock(System.currentTimeMillis() + lockExpiryInMillis);
            if (redisClient.putIfNone(lockKeyPath, newLock.toString()) == 1) {
                this.lock = newLock;
                return true;
            }

            final String currentValueStr = redisClient.getString(lockKeyPath);
            final Lock currentLock = Lock.fromString(currentValueStr);
            if (currentLock.isExpiredOrMine(lockUUID)) {
                String oldValueStr = redisClient.getSet(lockKeyPath, newLock.toString());
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    this.lock = newLock;
                    return true;
                }
            }

            timeout -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;
            Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);
        }

        return false;
    }

    /**
     * Renew lock.
     *
     * @return true if lock is acquired, false otherwise
     * @throws InterruptedException in case of thread interruption
     */
    public boolean renew() throws InterruptedException {
        final Lock lock = Lock.fromString(redisClient.getString(lockKeyPath));
        if (!lock.isExpiredOrMine(lockUUID)) {
            return false;
        }

        return acquire(redisClient);
    }

    /**
     * Acquired lock release.
     */
    public synchronized void release() {
        release(redisClient);
    }

    /**
     * Acquired lock release.
     *
     * @param redisClient
     */
    protected synchronized void release(RedisClient redisClient) {
        if (isLocked()) {
            redisClient.delete(lockKeyPath);
            this.lock = null;
        }
    }

    /**
     * Check if owns the lock
     *
     * @return true if lock owned
     */
    public synchronized boolean isLocked() {
        return this.lock != null;
    }

    /**
     * Returns the expiry time of this lock
     *
     * @return the expiry time in millis (or null if not locked)
     */
    public synchronized long getLockExpiryTimeInMillis() {
        return this.lock.getExpiryTime();
    }


    private Lock asLock(long expires) {
        return new Lock(lockUUID, expires);
    }

    public void executeInLock(RedisLockFun fun) throws ServiceException {
        try {
            if (this.acquire()) {
                fun.execute();
            } else {
                logger.error("Redis锁获取失败[系统异常]");
                throw new SystemException("REDIS_LOCK_FAILED","Redis锁获取失败");
            }
        } catch (InterruptedException e) {
            logger.error("Redis锁中断[系统异常]", e);
            throw new SystemException("REDIS_LOCK_INTERRUPTED_ERROR","Redis锁中断",e);
        } finally {
            this.release();
        }
    }

}
