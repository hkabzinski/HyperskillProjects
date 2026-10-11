package server;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * A lock that counts how often it was taken for reading and for writing, so a test can show
 * that the database locks the records before it touches them.
 */
class CountingReadWriteLock implements ReadWriteLock {
    private final CountingLock readLock = new CountingLock();
    private final CountingLock writeLock = new CountingLock();

    @Override
    public Lock readLock() {
        return readLock;
    }

    @Override
    public Lock writeLock() {
        return writeLock;
    }

    boolean lockedAndUnlockedForReading() {
        return readLock.isLockedAndUnlockedOnce();
    }

    boolean lockedAndUnlockedForWriting() {
        return writeLock.isLockedAndUnlockedOnce();
    }

    private static final class CountingLock implements Lock {
        private int locks;
        private int unlocks;

        @Override
        public void lock() {
            locks++;
        }

        @Override
        public void unlock() {
            unlocks++;
        }

        boolean isLockedAndUnlockedOnce() {
            return locks == 1 && unlocks == 1;
        }

        @Override
        public void lockInterruptibly() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean tryLock() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }
    }
}
