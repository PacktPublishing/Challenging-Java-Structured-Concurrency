package challenge.concurrency;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public final class CollaborativeEditor {
    
    private static final Logger logger = Logger.getLogger(CollaborativeEditor.class.getName());

    private String insertedCharacter;
    private String deletedCharacter;
    private int alignmentRight;
    private List<String> tableColumns;
    // and the list of variables continues

    private Lock insertedCharacterLock = new ReentrantLock();
    private Lock deletedCharacterLock = new ReentrantLock();
    private Lock alignmentRightLock = new ReentrantLock();
    private Lock tableColumnsLock = new ReentrantLock();  
    
    public void insertCharacter(String character) {

        insertedCharacterLock.lock();

        try {
            logger.info(() -> "Character inserted by " + Thread.currentThread());
            this.insertedCharacter = character;
        } finally {
            insertedCharacterLock.unlock();
        }
    }
    
    public void deleteCharacter(String character) {

        deletedCharacterLock.lock();

        try {
            logger.info(() -> "Character deleted by " + Thread.currentThread());
            this.deletedCharacter = character;
        } finally {
            deletedCharacterLock.unlock();
        }
    }
    
    public void alignmentRight(int spaces) {

        alignmentRightLock.lock();

        try {
            logger.info(() -> "Alignment right set by " + Thread.currentThread());
            this.alignmentRight = spaces;
        } finally {
            alignmentRightLock.unlock();
        }
    }
    
    public void addColumnInTable(String columnName) {
        
        tableColumnsLock.lock();
        
        try {
            logger.info(() -> "Table column added by " + Thread.currentThread());
            tableColumns.add(columnName);
        } finally {
            tableColumnsLock.unlock();
        }
    }
    
    public void removeColumnInTable(String columnName) {
        
        tableColumnsLock.lock();
        
        try {
            logger.info(() -> "Table column removed by " + Thread.currentThread());
            tableColumns.remove(columnName);
        } finally {
            tableColumnsLock.unlock();
        }
    }
}