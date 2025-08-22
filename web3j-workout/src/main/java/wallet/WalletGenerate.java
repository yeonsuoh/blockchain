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
     * 
     * 지갑 생성 과정:
     * 1. ECDSA 키페어 생성 (Private Key + Public Key)
     * 2. Public Key에서 Keccak-256 해시로 주소 도출
     * 3. 0x 접두사 + 20바이트 주소 완성
     */

    public static void main(String[] args)
        throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

        // [[1. 지갑 주소 셍성 (Private Key, Public Key)]]

        System.out.println("=== 이더리움 지갑 생성 시작 ===");

        // SECP-256k1 타원곡선 암호화로 키페어 생성
        // 비트코인과 동일한 암호화 알고리즘 사용
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();

        // Public Key에서 Keccak-256 해시로 주소 생성
        // 0x + 20바이트(40자리 16진수) 형태
        String walletAddress = Credentials.create(ecKeyPair).getAddress();

        System.out.println("생성된 지갑 주소: " + walletAddress);
        System.out.println("=== 지갑 주소 생성 완료 ===");

        System.out.println("\n=== 키 정보 출력 ===");

        // BigInteger를 0x 접두사가 있는 16진수 문자열로 변환
        String privateKey = Numeric.toHexStringWithPrefix(ecKeyPair.getPrivateKey());
        String publicKey = Numeric.toHexStringWithPrefix(ecKeyPair.getPublicKey());

        // Private Key: 32바이트 (비밀키, 절대 공개 금지)
        System.out.println("Private Key: " + privateKey);
        
        // Public Key: 64바이트 (공개키, 주소 생성용)
        System.out.println("Public Key: " + publicKey);


        /*
         * 실행 결과 예시:
         * 
         * 지갑 주소: 0x658b8a1ae242d0460d4777e17c9af438daab4f77 (20바이트)
         * Private Key: 0x4acdd58a6ccf6f4eceb0b158726689e0ad21beb7afabe2262a8790b39b55ca85 (32바이트)
         * Public Key: 0x376db746fb37456556a2c0018d876fbc5e61f9d60dec7f9f21132b2d7a19e71d5a7b81bcd29ded77b7b489f2e0e043998e385809f9779133938065190f351a91 (64바이트)
         * 
         * 주요 특징:
         * - 매번 실행시 다른 값 생성 (암호학적 난수 사용)
         * - Private Key로 언제든 동일한 Public Key와 주소 재생성 가능
         */
    }
}