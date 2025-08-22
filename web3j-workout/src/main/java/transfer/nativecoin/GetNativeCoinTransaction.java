package transfer.nativecoin;

import java.io.IOException;
import java.math.BigInteger;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;


public class GetNativeCoinTransaction {

    /**
     * - 3.2 네이티브 코인 트랜잭션 조회
     *     - hash
     *     - nonce
     *     - from
     *     - to
     *     - value
     *     - status
     *     - blockNumber
     *     - gasUsed
     *     - gasPrice
     */

    public static void main(String[] args) throws IOException {
        // 환경변수에서 API 키 가져오기
        String infuraKey = System.getenv("INFURA_PROJECT_ID");

        // 이더리움 Sepolia 테스트넷 RPC URL 설정
        String sepoliaUrl = "https://sepolia.infura.io/v3/" + infuraKey;

        // Web3j 인스턴스 생성 (블록체인 데이터 조회용)
        Web3j web3 = Web3j.build(new HttpService(sepoliaUrl));

        // 조회할 네이티브 코인 전송 트랜잭션 정보
        String transactionHash = "0x0db5f62336bc07a59ec38ea5a2b2b4a30d5819650fb7e2bbd988766d97b69022";
        String blockHash = "0x06427d3260986f289a61cddff8332a6df2630279ad69b1f9910c0729b7225359";

        // 16진수 문자열을 BigInteger로 변환
        String transactionIndexHex = "0x6e";  // 블록 내 트랜잭션 순서 (110번째)
        BigInteger transactionIndex = Numeric.toBigInt(transactionIndexHex);

        String blockNumberHex = "0x89af7a";   // 블록 번호 (9,023,354)
        BigInteger blockNumber = Numeric.toBigInt(blockNumberHex);

        String indexHex = "0x6e";            // 동일한 인덱스 값
        BigInteger index = Numeric.toBigInt(indexHex);

        System.out.println("================ 네이티브 코인 트랜잭션 조회 ================");
        System.out.println("트랜잭션 해시: " + transactionHash);
        System.out.println();

        // === 방법 1: 트랜잭션 해시로 직접 조회 ===
        // 가장 일반적이고 빠른 조회 방법
        EthTransaction ethTransaction1 = web3.ethGetTransactionByHash(transactionHash).send();
        Transaction transaction1 = ethTransaction1.getTransaction().get();

        System.out.println("=== 방법 1: 트랜잭션 해시 조회 ===");
        System.out.println("Hash: " + transaction1.getHash());
        System.out.println("Nonce: " + transaction1.getNonce());
        System.out.println("From: " + transaction1.getFrom());
        System.out.println("To: " + transaction1.getTo());
        System.out.println("Value: " + transaction1.getValue() + " wei"); // 네이티브 코인 전송량
        System.out.println("Block Number: " + transaction1.getBlockNumber());
        System.out.println("Gas Price: " + transaction1.getGasPrice() + " wei");
        System.out.println();

        // === 방법 2: 블록 해시 + 트랜잭션 인덱스로 조회 ===
        // 특정 블록의 N번째 트랜잭션을 조회할 때 사용
        EthTransaction ethTransaction2 = web3.ethGetTransactionByBlockHashAndIndex(blockHash, transactionIndex).send();
        Transaction transaction2 = ethTransaction2.getTransaction().get();
        System.out.println("=== 방법 2: 블록 해시 + 인덱스 조회 ===");
        System.out.println("Hash: " + transaction2.getHash());
        System.out.println();

        // === 방법 3: 블록 번호 + 트랜잭션 인덱스로 조회 ===
        // 블록 번호를 알고 있을 때 사용 (블록 해시보다 직관적)
        EthTransaction ethTransaction3 = web3.ethGetTransactionByBlockNumberAndIndex(DefaultBlockParameter.valueOf(blockNumber),
            index).send();
        Transaction transaction3 = ethTransaction3.getTransaction().get();
        System.out.println("=== 방법 3: 블록 번호 + 인덱스 조회 ===");
        System.out.println("Hash: " + transaction3.getHash());
        System.out.println();
        // === 3가지 조회 방법 결과 비교 ===
        if (transaction1.getHash().equals(transaction2.getHash()) &&
            transaction2.getHash().equals(transaction3.getHash())) {
            System.out.println("모든 조회 방법이 동일한 트랜잭션을 반환");
        } else {
            System.out.println("조회 결과가 일치하지 않음");
        }
        System.out.println();

        // === TransactionReceipt로 실행 결과 정보 조회 ===
        // Transaction에는 없는 실행 결과 정보들을 확인
        TransactionReceipt transactionReceipt = web3.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt()
            .get();

        System.out.println("=== 트랜잭션 실행 결과 정보 ===");

        // 실행 상태 (0x1=성공, 0x0=실패)
        String status = transactionReceipt.getStatus();
        System.out.println("Status : " + status + " (" + ("0x1".equals(status) ? "성공" : "실패") + ")");

        // 실제 사용된 가스량
        System.out.println("Gas Used : " + transactionReceipt.getGasUsed());

        // 실제 적용된 가스 가격 (EIP-1559 이후 동적 가격)
        BigInteger effectiveGasPrice = Numeric.toBigInt(transactionReceipt.getEffectiveGasPrice());
        System.out.println("Effective Gas Price : " + effectiveGasPrice + " wei");

        // 실제 수수료 계산
        BigInteger totalFee = transactionReceipt.getGasUsed().multiply(effectiveGasPrice);
        System.out.println("Total Transaction Fee : " + totalFee + " wei");

        System.out.println();
        System.out.println("=== 네이티브 코인 vs ERC20 토큰 비교 ===");
        System.out.println("네이티브 코인 : Transaction.value에 실제 전송량 기록");
        System.out.println("ERC20 토큰 : Transaction.value=0, 로그에서 전송 정보 확인");
    }
}

    /*
     * 참고 자료:
     * - Web3j 공식 문서: https://docs.web3j.io/4.14.0/transactions/transfer_eth/
     * - 이더리움 트랜잭션 구조: https://ethereum.org/en/developers/docs/transactions/
     * - Sepolia 테스트넷 탐색기: https://sepolia.etherscan.io/
     */

    /**
     * 네이티브 코인 트랜잭션 필드 설명
     * 
     * === Transaction 필드 (전송 시점 정보) ===
     * - hash: 트랜잭션 고유 식별자
     * - nonce: 발송자 지갑의 트랜잭션 순서 번호
     * - from: 보내는 지갑 주소 (20바이트)
     * - to: 받는 지갑 주소 (20바이트)
     * - value: 전송할 네이티브 코인 양 (wei 단위)
     * - blockNumber: 포함된 블록 번호
     * - gasPrice: 사용자가 설정한 가스 단가 (wei)
     * 
     * === TransactionReceipt 필드 (실행 결과 정보) ===
     * - status: 실행 결과
     *   → 0x1 = 성공, 0x0 = 실패 (가스 부족, 로직 에러 등)
     * - gasUsed: 실제 소모된 가스량
     *   → 네이티브 코인 전송: 약 21,000 가스
     * - effectiveGasPrice: 실제 적용된 가스 가격
     * 
     * === 수수료 계산 ===
     * 총 수수료 = gasUsed × effectiveGasPrice (wei 단위)
     * 
     * === 네이티브 코인 vs ERC20 토큰 차이점 ===
     * 네이티브 코인 :
     * - Transaction.value에 실제 전송량 기록
     * - 블록체인 프로토콜 레벨에서 직접 처리
     * - 상대적으로 낮은 가스비
     * 
     * ERC20 토큰 :
     * - Transaction.value = 0 (네이티브 코인 전송 없음)
     * - 실제 토큰 전송 정보는 로그에서 확인
     * - 스마트 컨트랙트 실행으로 더 많은 가스 소모
     * 
     * === 트랜잭션 조회 방법 비교 ===
     * 1. 해시 조회 : 가장 일반적, 빠름
     * 2. 블록해시+인덱스 : 특정 블록의 트랜잭션 순회
     * 3. 블록번호+인덱스 : 블록 번호를 알 때 사용
     */


