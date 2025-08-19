package wallet;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

public class WalletGenerate {

    /**
     * 1. 지갑 주소 생성 (Private Key, Public Key)
     */

    public static void main(String[] args)
        throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

        // [[1. 지갑 주소 셍성 (Private Key, Public Key)]]

        System.out.println("--- 지갑 주소 생성 시작 ---");

        // ECKeyPair : Elliptic Curve SECP-256k1(타원곡선 암호화) 키 쌍을 생성
        // Keys.createEcKeyPair() : Private Key(개인키)와 Public Key(공개키)를 동시에 생성
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();

        // Credentials는 이더리움 블록체인의 트랜잭션에 서명하기 위해 필요한 정보를 나타냄
        // ECKeyPair로부터 지갑 주소(0x로 시작하는 40자리 16진수)를 생성
        String walletAddress = Credentials.create(ecKeyPair).getAddress();

        // 생성된 지갑 주소 출력 (예: 0x1234...abcd)
        System.out.println("생성된 지갑 주소 = " + walletAddress);

        System.out.println("--- 지갑 주소 생성 완료 ---");


        // --- 주석 작성 확인 시작

//        // Public Key를 BigInteger 형태로 출력 (매우 큰 숫자)
//        // toString()은 10진수로 변환하여 출력
//        System.out.println("ecKeyPair.getPublicKey().toString() = " + ecKeyPair.getPublicKey().toString());
//
//        // Private Key를 BigInteger 형태로 출력 (매우 큰 숫자)
//        System.out.println("ecKeyPair.getPrivateKey().toString() = " + ecKeyPair.getPrivateKey().toString());
//
//        System.out.println("----");

        System.out.println("--- Private Key, Public Key 확인 ---");

        // Numeric.toHexStringWithPrefix(): BigInteger를 16진수 문자열로 변환
        // 0x 접두사를 붙여서 16진수임을 명시 (예: 0x1a2b3c...)
        // Private Key를 일반적으로 사용하는 16진수 형태로 변환
        String privateKey = Numeric.toHexStringWithPrefix(ecKeyPair.getPrivateKey());

        // Public Key를 일반적으로 사용하는 16진수 형태로 변환
        String publicKey = Numeric.toHexStringWithPrefix(ecKeyPair.getPublicKey());

        // 16진수 형태로 변환된 Private Key 출력
        // 메타마스크 등 지갑 앱에서 지갑을 복원할 때 이 값을 사용
        System.out.println("privateKey = " + privateKey);
        
        // 16진수 형태로 변환된 Public Key 출력
        // 블록체인에서 거래 검증 시 사용되는 공개 정보
        System.out.println("publicKey = " + publicKey);

         // 주석 작성 확인 끝


        /*

        [main 클래스 실행 결과]

        --- 지갑 주소 생성 시작 ---
        생성된 지갑 주소 = 0x658b8a1ae242d0460d4777e17c9af438daab4f77
        --- 지갑 주소 생성 완료 ---
        --- Private Key, Public Key 확인 ---
        privateKey = 0x4acdd58a6ccf6f4eceb0b158726689e0ad21beb7afabe2262a8790b39b55ca85
        publicKey = 0x376db746fb37456556a2c0018d876fbc5e61f9d60dec7f9f21132b2d7a19e71d5a7b81bcd29ded77b7b489f2e0e043998e385809f9779133938065190f351a91

         */
    }
}