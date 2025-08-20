package wallet;

import java.math.BigInteger;
import java.security.SignatureException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

public class PrivateKeySignGenerate {

    /**
     * 1.3 Private Key로 서명 생성
     */

    public static void main(String[] args) throws SignatureException {

        String privateKey = System.getenv("PRIVATE_KEY");

        // Private key로 서명을 위한 Credentials 생성
        Credentials credentials = Credentials.create(privateKey);

        // 메시지 서명
        String message = "This message is for test.";
        SignatureData signatureData = Sign.signMessage(message.getBytes(), credentials.getEcKeyPair());

        System.out.println("=== 서명 구성 요소 ===");
        System.out.println("r (32바이트) : " + Numeric.toHexString(signatureData.getR()));
        // r : 서명의 첫 번째 구성 요소 (타원곡선 점의 x좌표)
        System.out.println("s (32바이트) : " + Numeric.toHexString(signatureData.getS()));
        // s : 서명의 두 번째 구성 요소 (스칼라 값)
        System.out.println("v (1바이트) - 10진수: " + signatureData.getV()[0]); // 10진수
        System.out.println("v (1바이트) - 16진수 : " + Numeric.toHexString(signatureData.getV())); // 16진수
        // v : 복구 ID (서명에서 사용된 공개키를 복구하는 데 사용되는 값)

        // 16진수 문자열로 서명 얻기 (65바이트)
        byte[] signatureByteArr = new byte[65];
        System.arraycopy(signatureData.getR(), 0, signatureByteArr, 0, 32);
        System.arraycopy(signatureData.getS(), 0, signatureByteArr, 32, 32);
        System.arraycopy(signatureData.getV(), 0, signatureByteArr, 64, 1);

        String signature = Numeric.toHexString(signatureByteArr);
        System.out.println("signature = " + signature);

        // 서명자의 공개키를 복구하여 주소로 변환하기
        // (서명 -> 공개키 복구 가능, 공개키 -> 주소 추출 가능)
        BigInteger publicKeyBigInt = Sign.signedMessageToKey(message.getBytes(), signatureData);

        // Keys.getAddress() 메서드는 0x 접두사 없이 반환
        String address = Numeric.prependHexPrefix(Keys.getAddress(publicKeyBigInt));

        System.out.println(address); // todo
        System.out.println(credentials.getAddress());
        // 서명자의 주소가 복구된 주소와 일치하는지 확인
        if (address.equals(credentials.getAddress())) {
            System.out.println("일치");
        }

    }

}


        /*

        WalletGenerate - [main 클래스 실행 결과]

        --- 지갑 주소 생성 시작 ---
        생성된 지갑 주소 = 0x658b8a1ae242d0460d4777e17c9af438daab4f77
        --- 지갑 주소 생성 완료 ---
        --- Private Key, Public Key 확인 ---
        privateKey = 0x4acdd58a6ccf6f4eceb0b158726689e0ad21beb7afabe2262a8790b39b55ca85
        publicKey = 0x376db746fb37456556a2c0018d876fbc5e61f9d60dec7f9f21132b2d7a19e71d5a7b81bcd29ded77b7b489f2e0e043998e385809f9779133938065190f351a91

         */
