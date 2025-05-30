package Banking;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final int id;
    private int balance;
    private final Lock lock = new ReentrantLock();

    public BankAccount(int id, int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public int getId(){
        return  id;
    }
    public int getBalance() { return balance; }

    public void deposit(int amount) {
        lock.lock();
        try {
            balance += amount ;
        }
        finally {
            lock.unlock();
        }
    }

    public void withdraw(int amount) {
        lock.lock();
        try {
            balance -= amount ;
        }
        finally {
            lock.unlock();
        }
    }

    public void transfer(BankAccount target, int amount) {
        lock.lock();
        try {
            balance -= amount ;
        }
        finally {
            lock.unlock();
        }
        target.deposit(amount);
    }
}
