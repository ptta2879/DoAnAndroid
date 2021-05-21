package com.IUH.FastEvent.Web3j;

import org.web3j.crypto.Credentials;

import java.math.BigInteger;

public class ThongTinWeb3 {
    private static final String PRIVATE_KEY= "82bea5f861c3dc7f16865fddec35f44a2566e20855eb7c82d1ae6acf3d283219";
    public static final String PUBLIC_KEY = "0x5d1224F28d6FeB5E0f7B6D29e90A3D9beabDcF20";
    public static final String ADDRESS = "0x8bd54d53550Bbd5c1a508bA78be31Bd62FB94a39";
    public static final BigInteger GAS_PRICE= new BigInteger("20000000000");
    public static final BigInteger GAS_LIMIT = new BigInteger("6721975") ;
    public static final String URL = "https://kovan.infura.io/v3/be4ca854504647adbc9649897b72a5a6";

    public Credentials getCredentialsWallet(){
        return Credentials.create(PRIVATE_KEY);
    }
}
