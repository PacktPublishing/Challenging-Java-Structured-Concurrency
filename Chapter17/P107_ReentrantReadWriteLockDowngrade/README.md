# Downgrade `ReentrantReadWriteLock` 
A writer (holder of a write-lock) can be downgraded to a reader (acquire a read lock and release the write-lock).