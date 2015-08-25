package ru.fireway.ws;

/**
 * Created by Александр on 05.07.2015.
 */
public class AccountEntity {
    private final int id;
    private final long amount;

    public AccountEntity(int id, long amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public long getAmount() {
        return amount;
    }
}
