package com.futurecomes.neo.wallet.behavior;

import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptHash;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.ByteArrayStackItem;
import io.neow3j.protocol.core.methods.response.IntegerStackItem;
import io.neow3j.protocol.core.methods.response.InvocationResult;
import io.neow3j.protocol.core.methods.response.StackItem;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.exceptions.InsufficientFundsException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NEP5SendBehaviorImpl extends AbstractSendBehavior {
    private static final String DECIMALS = "decimals";
    private static final String BALANCE = "balanceOf";
    private static final String TRANSFER = "transfer";

    private static final String HALT = "HALT";

    private ScriptHash scriptHash;

    public NEP5SendBehaviorImpl(Neow3j neow3j, Account from, String to, BigDecimal amount, ScriptHash scriptHash) {
        super(neow3j, from, to, amount);
        this.scriptHash = scriptHash;
    }

    @Override
    public String send() throws IOException, ErrorResponseException {
        int decimals = getDecimals(scriptHash);
        BigInteger satoshiAmount = toSatoshi(amount, decimals);
        BigInteger satoshiBalance = getBalance(scriptHash, fromAccount.getAddress());
        checkBalance(fromSatoshi(satoshiBalance, decimals), amount);

        return new ContractInvocation.Builder(neow3j)
                .contractScriptHash(scriptHash)
                .function(TRANSFER)
                .parameter(ContractParameter.byteArrayFromAddress(fromAccount.getAddress()))
                .parameter(ContractParameter.byteArrayFromAddress(toAddress))
                .parameter(ContractParameter.integer(satoshiAmount))
                .account(fromAccount)
                .build()
                .sign()
                .invoke()
                .getTransaction()
                .getTxId();
    }

    private BigInteger toSatoshi(BigDecimal coins, int decimals) {
        return BigDecimal.TEN.pow(decimals).multiply(coins).toBigInteger();
    }

    private BigDecimal fromSatoshi(BigInteger coins, int decimals) {
        return new BigDecimal(coins, decimals);
    }

    private int getDecimals(ScriptHash scriptHash) throws IOException, ErrorResponseException {
        InvocationResult invocationResult = new ContractInvocation.Builder(neow3j)
                .contractScriptHash(scriptHash)
                .function(DECIMALS)
                .build()
                .testInvoke();

        return getBigIntegerFromResult(invocationResult).intValue();
    }

    private BigInteger getBalance(ScriptHash scriptHash, String address) throws IOException, ErrorResponseException {
        InvocationResult invocationResult = new ContractInvocation.Builder(neow3j)
                .contractScriptHash(scriptHash)
                .function(BALANCE)
                .parameter(ContractParameter.byteArrayFromAddress(address))
                .build()
                .testInvoke();

        return getBigIntegerFromResult(invocationResult);
    }

    private boolean isInvocationOk(InvocationResult invocationResult) {
        boolean isHalted = invocationResult.getState().contains(HALT);
        boolean isStackNotEmpty = !invocationResult.getStack().isEmpty();
        return isHalted && isStackNotEmpty;
    }

    private BigInteger getBigIntegerFromResult(InvocationResult invocationResult) {
        if (isInvocationOk(invocationResult)) {
            StackItem stackItem = invocationResult.getStack().get(0);
            if (stackItem instanceof IntegerStackItem) {
                return ((IntegerStackItem) stackItem).getValue();
            } else if (stackItem instanceof ByteArrayStackItem) {
                return stackItem.asByteArray().getAsNumber();
            }
        }
        throw new IllegalArgumentException("The specified ScriptHash is not a NEP5 contract");
    }

    private void checkBalance(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Needed " + amount + " but only found " +
                    balance + " for NEP5 token " + scriptHash);
        }
    }
}
