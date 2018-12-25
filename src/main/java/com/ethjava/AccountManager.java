package com.ethjava;

import com.ethjava.utils.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


/**
 * 账号管理相关
 */
public class AccountManager {
    private static Admin admin;

    static Logger logger = LoggerFactory.getLogger(AccountManager.class);

    public static void main(String[] args) {
        admin = Admin.build(new HttpService(Environment.RPC_URL));
        // createFile();


       // loadwFile();
        getBlanceOf();
       // transto();
        //getAccountList();
        //admin.ethGetBalance("0xB2350f8fA7C3E6b017BACD5f07A31f2D59560570",new DefaultBlockParameterNumber(123));
        //createNewAccount();
        //getAccountList();
        // unlockAccount();

//		admin.personalSendTransaction(); 该方法与web3j.sendTransaction相同 不在此写例子。
    }

    /**
     * 创建钱包
     */
    private static void createFile() {

        try {
            String walletFilePath0 = "C://";//钱包文件保持路径，请替换位自己的某文件夹路径

            String walletFileName0 = WalletUtils.generateFullNewWalletFile("123456", new File(walletFilePath0));

            logger.info("walletName: " + walletFileName0);
        } catch (Exception e) {
            logger.error("ddd:", e);
        }


    }

    /**
     * 加载钱包拿去address
     */
    private static void loadwFile() {
        try {
            Credentials credentials = WalletUtils.loadCredentials("123456", "C://wallet");
            String address = credentials.getAddress();

            BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
            BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();

            logger.info("Credentials address:{}", address);//0x2736285f0528bb90d9a8c077892d98926ffbf062
            logger.info("address=" + address);
            logger.info("public key=" + publicKey);
            logger.info("private key=" + privateKey);
        } catch (Exception e) {
            logger.error("sss:", e);
        }

    }

    /**
     * 创建账号
     */
    private static void createNewAccount() {
        String password = "123456789";
        try {
            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
            String address = newAccountIdentifier.getAccountId();
            System.out.println("new account address " + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取账号列表
     */
    private static void getAccountList() {
        try {
            PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
            List<String> addressList;
            addressList = personalListAccounts.getAccountIds();
            System.out.println("account size " + addressList.size());
            for (String address : addressList) {
                System.out.println(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 账号解锁
     */
    private static void unlockAccount() {
        String address = "0x2736285f0528bb90d9a8c077892d98926ffbf062";
        String password = "123456";
        //账号解锁持续时间 单位秒 缺省值300秒
        BigInteger unlockDuration = BigInteger.valueOf(60L);
        try {
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, password, unlockDuration).send();
            Boolean isUnlocked = personalUnlockAccount.accountUnlocked();
            System.out.println("account unlock " + isUnlocked);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***********查询指定地址的余额***********/
    private static void getBlanceOf() {
        try {

            ///0xB2350f8fA7C3E6b017BACD5f07A31f2D59560570 有币
            String address = "0x2736285f0528bb90d9a8c077892d98926ffbf062";//等待查询余额的地址
            //第二个参数：区块的参数，建议选最新区块
            EthGetBalance balance = admin.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send();
            //格式转化 wei-ether
            String blanceETH = Convert.fromWei(balance.getBalance().toString(), Convert.Unit.ETHER).toPlainString().concat(" ether");

            logger.info("blance:{}",blanceETH);
        } catch (Exception e) {
            logger.error("", e);
        }

    }

    /****************交易*****************/
  /*  public static void transto(){
        try {
        Credentials credentials = WalletUtils.loadCredentials("123456", "C://wallet");

        //开始发送0.01 =eth到指定地址
        String address_to = "0xB2350f8fA7C3E6b017BACD5f07A31f2D59560570";

        TransactionReceipt send = Transfer.sendFunds(admin, credentials, address_to, BigDecimal.ONE, Convert.Unit.FINNEY).send();

        logger.info("Transaction complete:");
        logger.info("trans hash=" + send.getTransactionHash());
        logger.info("from :" + send.getFrom());
        logger.info("to:" + send.getTo());
        logger.info("gas used=" + send.getGasUsed());
        logger.info("status: " + send.getStatus());
        } catch (Exception e) {
            logger.error("transto", e);
        }
    }*/






}
