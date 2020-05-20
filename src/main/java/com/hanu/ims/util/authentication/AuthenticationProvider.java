package com.hanu.ims.util.authentication;

import com.hanu.ims.model.domain.Account;

public class AuthenticationProvider {
    private static AuthenticationProvider provider;

    private Account currentAccount;

    private AuthenticationProvider() {
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public static AuthenticationProvider getInstance() {
        if (provider == null) {
            provider = new AuthenticationProvider();
        }
        return provider;
    }
}
