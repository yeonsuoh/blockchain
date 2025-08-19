package wallet;

import java.math.BigInteger;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

public class PublicKeyExtract {

    /**
     * 1.1 Private Key로 Public Key 추출
     */

    public static void main(String[] args) {

        /*

        WalletGenerate - [main 클래스 실행 결과]

        --- 지갑 주소 생성 시작 ---
        생성된 지갑 주소 = 0x658b8a1ae242d0460d4777e17c9af438daab4f77
        --- 지갑 주소 생성 완료 ---
        --- Private Key, Public Key 확인 ---
        privateKey = 0x4acdd58a6ccf6f4eceb0b158726689e0ad21beb7afabe2262a8790b39b55ca85
        publicKey = 0x376db746fb37456556a2c0018d876fbc5e61f9d60dec7f9f21132b2d7a19e71d5a7b81bcd29ded77b7b489f2e0e043998e385809f9779133938065190f351a91

         */

        String expectedPublicKey = "0x376db746fb37456556a2c0018d876fbc5e61f9d60dec7f9f21132b2d7a19e71d5a7b81bcd29ded77b7b489f2e0e043998e385809f9779133938065190f351a91";
        System.out.println("검증할 privateKey = " + expectedPublicKey);
        System.out.println();

        // 방법 1. Credentials에서 추출
        System.out.println("=== 방법 1. Credentials에서 추출 시작 ===");

        String privateKey = "0x4acdd58a6ccf6f4eceb0b158726689e0ad21beb7afabe2262a8790b39b55ca85";

        // Private Key로 Credentials 추출하기
        Credentials credentials = Credentials.create(privateKey);

        // Credentials에서 Public Key 추출하기
        BigInteger publicKeyBigInt = credentials.getEcKeyPair().getPublicKey();

        // 추출한 10진수 Public Key를 16진수 문자열로 변환
        String extractedPublicKey = Numeric.toHexStringWithPrefix(publicKeyBigInt);

        System.out.println("방법 1. privateKey로부터 추출한 publicKey = " + extractedPublicKey);

        if(extractedPublicKey.equals(expectedPublicKey)){
            System.out.println("=== 방법 1. Credentials에서 추출 성공 ===");
        }



        // ---------------------------------------------------------
        System.out.println();

        // 방법 2. Sign.publicKeyFromPrivate 메서드 이용
        System.out.println("=== 방법 2. Sign.publicKeyFromPrivate 메서드 이용 추출 시작 ===");

        BigInteger privateKeyBigInt = Numeric.toBigInt(privateKey);

        BigInteger publicKeyBigInt2 = Sign.publicKeyFromPrivate(privateKeyBigInt);

        String extractedPublicKey2 = Numeric.toHexStringWithPrefix(publicKeyBigInt2);

        System.out.println("방법 2. privateKey로부터 추출한 publicKey = " + extractedPublicKey2);

        if(extractedPublicKey.equals(extractedPublicKey2)){
            System.out.println("=== 방법 2. Sign.publicKeyFromPrivate 메서드 이용 추출 성공 ===");

        }


    }

}
