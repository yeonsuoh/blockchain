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
     * 
     * 디지털 서명의 목적:
     * - 메시지 인증: 서명자가 실제로 메시지를 작성했음을 증명
     * - 무결성 보장: 메시지가 변조되지 않았음을 보장
     * - 부인 방지: 서명자가 서명 사실을 부인할 수 없음
     */

    public static void main(String[] args) throws SignatureException {

        String privateKey = System.getenv("PRIVATE_KEY");

        // Private key로 서명을 위한 Credentials 생성
        Credentials credentials = Credentials.create(privateKey);
        System.out.println("서명자 주소: " + credentials.getAddress());

        // 메시지 서명 (ECDSA 알고리즘 사용)
        String message = "This message is for test.";
        System.out.println("원본 메시지: " + message);
        
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

        System.out.println("\n=== 서명 검증 ===");
        System.out.println("복구된 주소: " + address);
        System.out.println("원본 주소: " + credentials.getAddress());
        
        // 서명자의 주소가 복구된 주소와 일치하는지 확인
        if (address.equals(credentials.getAddress())) {
            System.out.println("✓ 서명 검증 성공: 주소가 일치합니다.");
        } else {
            System.out.println("✗ 서명 검증 실패: 주소가 일치하지 않습니다.");
        }

    }

}


        /*
         * 디지털 서명 과정 요약:
         * 1. 메시지 + 개인키 → ECDSA 서명 생성 (r, s, v)
         * 2. 서명 + 메시지 → 공개키 복구
         * 3. 공개키 → 주소 추출
         * 4. 추출된 주소와 원본 주소 비교로 검증
         * 
         * 블록체인에서의 활용:
         * - 트랜잭션 서명: 자산 전송 시 소유권 증명
         * - 메시지 서명: 신원 인증 및 데이터 무결성 보장
         * - 스마트 컨트랙트: 함수 호출 권한 검증
         */
