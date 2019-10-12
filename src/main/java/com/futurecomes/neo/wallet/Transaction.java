package com.futurecomes.neo.wallet;

import com.futurecomes.neo.wallet.behavior.NEP5SendBehaviorImpl;
import com.futurecomes.neo.wallet.behavior.SendBehavior;
import com.futurecomes.neo.wallet.behavior.AssetSendBehaviorImpl;
import io.neow3j.contract.ScriptHash;
import io.neow3j.crypto.Base58;
import io.neow3j.crypto.ECKeyPair;
import io.neow3j.crypto.WIF;
import io.neow3j.model.types.GASAsset;
import io.neow3j.model.types.NEOAsset;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.wallet.Account;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyPair;

public class Transaction implements SendBehavior {
    private SendBehavior sendBehavior;

    private Transaction(Builder builder) {
        if (builder.assetId != null) {
            this.sendBehavior = new AssetSendBehaviorImpl(
                    builder.neow3j,
                    builder.fromAccount,
                    builder.toAddress,
                    builder.amount,
                    builder.assetId);
        } else {
            this.sendBehavior = new NEP5SendBehaviorImpl(
                    builder.neow3j,
                    builder.fromAccount,
                    builder.toAddress,
                    builder.amount,
                    builder.nep5ScriptHash);
        }
    }

    @Override
    public String send() throws IOException, ErrorResponseException {
        return sendBehavior.send();
    }

    public static class Builder {
        private Neow3j neow3j;
        private String assetId;
        private ScriptHash nep5ScriptHash;
        private Account fromAccount;
        private String toAddress;
        private BigDecimal amount;

        public Builder() {
        }

        public Builder(String nodeUrl) {
            this.neow3j = Neow3j.build(new HttpService(nodeUrl));
        }

        public Builder node(String nodeUrl) {
            this.neow3j = Neow3j.build(new HttpService(nodeUrl));
            return this;
        }

        public Builder asset(String assetId) {
            throwIfNep5IdSet();
            this.assetId = assetId;
            return this;
        }

        public Builder neo() {
            throwIfNep5IdSet();
            this.assetId = NEOAsset.HASH_ID;
            return this;
        }

        public Builder gas() {
            throwIfNep5IdSet();
            this.assetId = GASAsset.HASH_ID;
            return this;
        }

        public Builder nep5(String scriptHash) {
            throwIfAssetIdSet();
            this.nep5ScriptHash = createScriptHash(scriptHash);
            return this;
        }

        public Builder nep5(ScriptHash scriptHash) {
            throwIfAssetIdSet();
            this.nep5ScriptHash = scriptHash;
            return this;
        }

        public Builder from(String wif) {
            this.fromAccount = Account.fromECKeyPair(ECKeyPair.create(privateKeyFromWIF(wif))).build();
            return this;
        }

        public Builder from(byte[] privateKey) {
            this.fromAccount = Account.fromECKeyPair(ECKeyPair.create(privateKey)).build();
            return this;
        }

        public Builder from(BigInteger privateKey) {
            this.fromAccount = Account.fromECKeyPair(ECKeyPair.create(privateKey)).build();
            return this;
        }

        public Builder from(KeyPair keyPair) {
            this.fromAccount = Account.fromECKeyPair(ECKeyPair.create(keyPair)).build();
            return this;
        }

        public Builder to(String address) {
            checkAddress(address);
            this.toAddress = address;
            return this;
        }

        public Builder amount(String amount) {
            this.amount = new BigDecimal(amount);
            return this;
        }

        public Builder amount(double amount) {
            this.amount = new BigDecimal(amount);
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Transaction build() {
            if (neow3j == null) {
                throw new IllegalStateException("Node URL not set");
            }
            if (assetId == null && nep5ScriptHash == null) {
                throw new IllegalStateException("Asset not set");
            }
            if (fromAccount == null) {
                throw new IllegalStateException("Sender private key not set");
            }
            if (toAddress == null) {
                throw new IllegalStateException("Recipient address not set");
            }
            if (amount == null) {
                throw new IllegalStateException("Amount not set");
            }

            return new Transaction(this);
        }

        private byte[] privateKeyFromWIF(String wif) {
            try {
                return WIF.getPrivateKeyFromWIF(wif);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid WIF", e);
            }
        }

        private ScriptHash createScriptHash(String hexScriptHash) {
            try {
                return new ScriptHash(hexScriptHash);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid ScriptHash", e);
            }
        }

        private void checkAddress(String address) {
            try {
                Base58.base58CheckDecode(address);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid address", e);
            }
        }

        private void throwIfNep5IdSet() {
            if (nep5ScriptHash != null) {
                throw new IllegalStateException("NEP5 script hash already set");
            }
        }

        private void throwIfAssetIdSet() {
            if (assetId != null) {
                throw new IllegalStateException("Asset ID already set");
            }
        }
    }
}
