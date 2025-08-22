package transfer.nativecoin;

import java.math.BigDecimal;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert.Unit;

/**
 * 네이티브 코인 전송 실습
 * 
 * 네이티브 코인이란?
 * - 블록체인 네트워크의 기본 화폐 (ETH, BNB, BTC 등)
 * - 스마트 컨트랙트 없이 직접 전송 가능
 * - 가스비 지불과 네트워크 참여에 사용
 * 
 * 전송 과정:
 * 1. 네트워크 연결 (Sepolia 테스트넷)
 * 2. 지갑 인증 정보 설정
 * 3. Transfer.sendFunds() 메서드로 간단 전송
 * 4. 단위 변환이 자동으로 처리됨
 */
public class NativeCoinTransfer {

    /**
     * 3. 네이티브 코인 전송
     * 3.1 네이티브 코인 전송 (from, to, amount)
     * 
     * 필수 요소:
     * - to: 목적지 지갑 주소
     * - value: 전송할 이더 양
     * - from: 개인키로 자동 결정
     */
    public static void main(String[] args) throws Exception {
        System.out.println("================ 네이티브 코인 전송 시작 ================");
        
        // === 사전 준비 사항 ===
        // 1. 테스트 ETH 발급: https://cloud.google.com/application/web3/faucet/ethereum/sepolia
        // 2. Infura API 키: https://developer.metamask.io/
        // 3. 개인키 준비: 메타마스크에서 내보내기 가능

        String infuraKey = System.getenv("INFURA_PROJECT_ID");  // Infura API 키
        String privateKey = System.getenv("PRIVATE_KEY");       // 지갑 개인키

        // 이더리움 Sepolia 테스트넷 RPC URL
        String sepoliaUrl = "https://sepolia.infura.io/v3/" + infuraKey;

        // 전송 설정
        String toAddress = "0x192897df0B17c99fA24eCF998c22e2E83C3cD3D8"; // 받는 주소
        BigDecimal value = new BigDecimal("0.000001");              // 0.000001 ETH 전송
        
        System.out.println("받는 주소: " + toAddress);
        System.out.println("전송 금액: " + value + " ETH");

        // Web3j 인스턴스 생성 (블록체인 연결)
        Web3j web3 = Web3j.build(new HttpService(sepoliaUrl));
        
        // 개인키로 지갑 인증 정보 생성
        Credentials credentials = Credentials.create(privateKey);
        
        System.out.println("보내는 주소: " + credentials.getAddress());

        // 네이티브 코인 전송 실행
        // Transfer.sendFunds(): Web3j에서 제공하는 간편한 전송 메서드
        // - 가스 설정, 단위 변환 등이 자동으로 처리됨
        System.out.println("\n트랜잭션 전송 중...");
        
        TransactionReceipt transactionReceipt =
            Transfer.sendFunds(web3, credentials, toAddress, value, Unit.ETHER).send();
        // .send(): 동기 방식 (결과를 기다림)
        // .sendAsync(): 비동기 방식 (즉시 반환)

        // 트랜잭션 결과 출력
        System.out.println("\n=== 네이티브 코인 전송 완료 ===");
        System.out.println("트랜잭션 해시 : " + transactionReceipt.getTransactionHash());
        System.out.println("블록 번호 : " + transactionReceipt.getBlockNumber());
        System.out.println("가스 사용량 : " + transactionReceipt.getGasUsed());
        System.out.println("상태: " + (transactionReceipt.isStatusOK() ? "성공" : "실패"));
        System.out.println("보낸 주소 : " + transactionReceipt.getFrom());
        System.out.println("받은 주소 : " + transactionReceipt.getTo());
        
        // Sepolia 테스트넷 탐색기에서 확인 가능
        System.out.println("\n탐색기 확인: https://sepolia.etherscan.io/tx/" + transactionReceipt.getTransactionHash());
        
        System.out.println("\n=== 전체 영수증 정보 ===");
        System.out.println(transactionReceipt);
        /*
         * 성공적인 네이티브 코인 전송 결과 예시:
         * 
         * 주요 필드 분석:
         * - transactionHash: 트랜잭션 고유 식별자
         * - blockNumber: 0x89af7a (9,023,354번째 블록)
         * - gasUsed: 0x5208 (21,000 가스 - 네이티브 코인 전송 기본값)
         * - status: 0x1 (성공)
         * - from/to: 보내는/받는 주소
         * - logs: [] (빈 배열 - 네이티브 코인 전송은 로그 없음)
         * - contractAddress: null (컨트랙트 생성이 아님)
         * - effectiveGasPrice: 0x42f40a (4,387,850 wei = 약 4.39 gwei)
         * 
         * 네이티브 코인 전송의 특징:
         * - 고정된 21,000 가스 소모
         * - 로그 없음 (스마트 컨트랙트 비사용)
         * - 블록체인 프로토콜 레벨에서 직접 처리
         */
    }

}

/*
코인과 토큰의 공통점
 - 주로 화폐 전송이나 가치 저장 수단으로 사용되는 디지털 자선

코인과 토큰의 차이
- 코인은 특정 블록체인의 기본 화폐
- 예를 들어, BTC는 비트코인 네트워크의 기본 코인
- 암호화폐가 자체 블록체인에서 실행된다면 코인
- 이 네이티브 코인은 거래 수수료를 지불하고 네트워크에 참여하는 데 사용된다.

- 토큰은 자체 블록체인을 구동하는 것이 아니라 특정 블록체인에서 지원하는 화폐 (또는 디지털 자산)
- 네트워크의 기본 코인은 아님
- 이더리움 네트워크의 기본 코인은 이더(Ether)이지만, ERC 표준을 따르는 다른 많은 이더리움 기반 화폐도 지원함
- 이더리움 네트워크에는 이더리움의 기본 이더가 아닌 여러 화폐(및 기타 자산)가 있으며, 이러한 각 자산을 토큰이라고 한다.
- ERC 표준에서 제공하는 첫 번째 토큰은 ERC-20 토큰
    - 이 대체 가능한 토큰 표준을 통해 사용자는 이더리움에서 지원하는 화폐를 생성, 발행 및 관리할 수 있음 (2017 ICO 열풍)
    - 그 후 ERC-20 표준은 ERC-721 토큰 (대체 불가능 토큰)과 ERC-1155 토큰 (반대체 가능 토큰)을 추가하는 등 계속해서 확장
- 이더리움 네트워크가 토큰을 지원할 수 있는 이유는 스마트 계약 호환성 때문
- 토큰은 이더리움 네트워크 뿐만 아니라 솔라나, 카르다노, 테조스 등 여러 블록체인이 토큰을 지원함




[참고]
https://www.ledger.com/ko/academy/%EC%BD%94%EC%9D%B8%EA%B3%BC-%ED%86%A0%ED%81%B0%EC%9D%98-%EC%B0%A8%EC%9D%B4%EC%A0%90

https://docs.web3j.io/4.14.0/transactions/transfer_eth/
 */
