package com.futurecomes.neo.wallet.behavior;

import io.neow3j.protocol.exceptions.ErrorResponseException;

import java.io.IOException;

public interface SendBehavior {
    String send() throws IOException, ErrorResponseException;
}
