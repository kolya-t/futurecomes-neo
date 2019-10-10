package com.futurecomes.neo.wallet.behavior;

import io.neow3j.model.types.NEOAsset;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.AssetTransfer;
import io.neow3j.wallet.exceptions.InsufficientFundsException;

import java.io.IOException;
import java.math.BigDecimal;

public class AssetSendBehaviorImpl extends AbstractSendBehavior {
    private String assetId;

    public AssetSendBehaviorImpl(Neow3j neow3j, Account from, String to, BigDecimal amount, String assetId) {
        super(neow3j, from, to, amount);
        this.assetId = assetId;
    }

    @Override
    public String send() throws IOException, ErrorResponseException {
        checkBalance(fromAccount);
        return new AssetTransfer.Builder(neow3j)
                .account(fromAccount)
                .asset(assetId)
                .amount(amount.toString())
                .toAddress(toAddress)
                .build()
                .sign()
                .send()
                .getTransaction()
                .getTxId();
    }

    private void checkBalance(Account account) throws IOException, ErrorResponseException {
        fromAccount.updateAssetBalances(neow3j);
        if (!account.getBalances().hasAsset(NEOAsset.HASH_ID)) {
            throw new InsufficientFundsException("Account balance does not contain the asset " +
                    "with ID " + NEOAsset.HASH_ID);
        }
    }
}
