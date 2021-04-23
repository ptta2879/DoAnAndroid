package com.IUH.quetma;

import org.web3j.crypto.Credentials;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

public class ThongTinWeb3 {
    private static final String PRIVATE_KEY= "82bea5f861c3dc7f16865fddec35f44a2566e20855eb7c82d1ae6acf3d283219";
    public static final String PUBLIC_KEY = "0x5d1224F28d6FeB5E0f7B6D29e90A3D9beabDcF20";
    public static final String ADDRESS = "0x1928A7554b4Ac7227dD0b30Bb07B3c6FD46F80C6";
    public static final BigInteger GAS_PRICE= new BigInteger("20000000000");
    public static final BigInteger GAS_LIMIT = new BigInteger("300000000") ;

    public Credentials getCredentialsWallet(){
        return Credentials.create(PRIVATE_KEY,PUBLIC_KEY);
    }

}
