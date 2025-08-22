package wallet;

import java.math.BigInteger;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

public class PublicKeyExtract {

    /**
     * 1.1 Private Key로 Public Key 추출
     * 
     * 암호화 기본 개념:
     * - Private Key: 비밀키 (절대 공개 금지)
     * - Public Key: 공개키 (Private Key에서 수학적으로 도출)
     * - 주소: Public Key에서 Keccak-256 해시로 생성
     */

    public static void main(String[] args) {

        // WalletGeneration.java에서 생성된 공개키 (검증용)

        String expectedPublicKey = "0x376db746fb37456556a2c0018d876fbc5e61f9d60dec7f9f21132b2d7a19e71d5a7b81bcd29ded77b7b489f2e0e043998e385809f9779133938065190f351a91";
        System.out.println("기대하는 Public Key: " + expectedPublicKey);
        System.out.println();

        // 방법 1. Credentials 클래스 사용 (고수준 API)
        System.out.println("=== 방법 1. Credentials에서 추출 ===");

        String privateKey = System.getenv("PRIVATE_KEY");
        System.out.println("Private Key: " + privateKey);

        // Private Key로 Credentials 객체 생성
        Credentials credentials = Credentials.create(privateKey);

        // EC 키페어에서 Public Key 추출
        BigInteger publicKeyBigInt = credentials.getEcKeyPair().getPublicKey();

        // BigInteger를 16진수 문자열로 변환
        String extractedPublicKey = Numeric.toHexStringWithPrefix(publicKeyBigInt);

        System.out.println("추출된 Public Key: " + extractedPublicKey);

        if(extractedPublicKey.equals(expectedPublicKey)){
            System.out.println("방법 1 성공: Public Key 일치");
        } else {
            System.out.println("방법 1 실패: Public Key 불일치");
        }



        // ---------------------------------------------------------
        System.out.println();

        // 방법 2. Sign 클래스 직접 사용 (저수준 API)
        System.out.println("\n=== 방법 2. Sign.publicKeyFromPrivate 메서드 ===");

        // 16진수 문자열을 BigInteger로 변환
        BigInteger privateKeyBigInt = Numeric.toBigInt(privateKey);

        // ECDSA 알고리즘으로 Public Key 직접 계산
        BigInteger publicKeyBigInt2 = Sign.publicKeyFromPrivate(privateKeyBigInt);

        String extractedPublicKey2 = Numeric.toHexStringWithPrefix(publicKeyBigInt2);

        System.out.println("추출된 Public Key: " + extractedPublicKey2);

        if(extractedPublicKey.equals(extractedPublicKey2)){
            System.out.println("방법 2 성공: 두 방법 결과 일치");
        } else {
            System.out.println("방법 2 실패: 결과 불일치");
        }
        
        System.out.println("\n=== 최종 검증 ===");
        if(extractedPublicKey.equals(expectedPublicKey) && extractedPublicKey2.equals(expectedPublicKey)) {
            System.out.println("모든 방법으로 Public Key 추출 성공");
        }


    }

}
