package com.futurecomes.neo.wallet.behavior;

import io.neow3j.protocol.Neow3j;
import io.neow3j.wallet.Account;

import java.math.BigDecimal;

public abstract class AbstractSendBehavior implements SendBehavior {
    protected Neow3j neow3j;
    protected Account fromAccount;
    protected String toAddress;
    protected BigDecimal amount;

    public AbstractSendBehavior(Neow3j neow3j, Account fromAccount, String toAddress, BigDecimal amount) {
        this.neow3j = neow3j;
        this.fromAccount = fromAccount;
        this.toAddress = toAddress;
        this.amount = amount;
    }
}
