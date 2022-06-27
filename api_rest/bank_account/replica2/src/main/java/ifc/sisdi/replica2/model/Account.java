package ifc.sisdi.replica2.model;

public class Account {
    private int numberAccount;
    private double balance;

    public Account(int numberAccount, double balance) {
        this.numberAccount = numberAccount;
        this.balance = balance;
    }

    public int getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(int numberAccount) {
        this.numberAccount = numberAccount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
