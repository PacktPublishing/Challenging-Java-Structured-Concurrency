package challenge.concurrency.dao;

sealed interface DAO permits DAOImpl {

    public void sqlOperation1();
    public void sqlOperation2();
}
