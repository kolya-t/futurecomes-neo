package com.futurecomes.neo;

import com.futurecomes.neo.wallet.Transaction;
import io.neow3j.protocol.exceptions.ErrorResponseException;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException, ErrorResponseException {
        String neoTransferTxHash = new Transaction.Builder("http://seed7.ngd.network:10332")
                .from("L4Kk8QZwZSeQjdpVF2FC74zgNs481GphVVXkHJTazYpy5rBV5mKf")
                .amount(1)
                .neo()
                .to("AWoBWaymVJ4ukn38LqysGh3Y6iMJno4YMV")
                .build()
                .send();
        System.out.println(neoTransferTxHash);

        String nep5TransferTxHash = new Transaction.Builder("http://seed7.ngd.network:10332")
                .from("L4Kk8QZwZSeQjdpVF2FC74zgNs481GphVVXkHJTazYpy5rBV5mKf")
                .amount("1")
                .nep5("0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5")
                .to("AMpnUpKQSyRKGi5DpFG31wpU13jXR9Yi9e")
                .build()
                .send();
        System.out.println(nep5TransferTxHash);
    }
}
