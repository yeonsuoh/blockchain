package transfer.erc20token;

import java.io.IOException;
import java.util.List;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

/**
 * ERC20 토큰 트랜잭션 조회 실습
 * 
 * 트랜잭션 조회 방법:
 * 1. Transaction: 트랜잭션 전송 시점의 정보 (의도, 설정값)
 * 2. TransactionReceipt: 트랜잭션 실행 결과 정보 (결과, 사용량)
 * 
 * ERC20 토큰 전송의 특징:
 * - 네이티브 코인 전송과 달리 value=0 (ETH 전송 없음)
 * - 실제 토큰 전송 정보는 로그에서 확인
 * - 컨트랙트 함수 호출로 인한 추가 가스 소모
 */
public class GetERC20TokenTransaction {
    /**
     * 4.2 ERC20 트랜잭션 조회
     * 
     * 조회 가능한 주요 정보:
     * - hash: 트랜잭션 고유 식별자
     * - nonce: 발송자의 트랜잭션 순서 번호
     * - from/to: 보내는/받는 주소
     * - value: 네이티브 코인 전송량 (ERC20에서는 0)
     * - status: 실행 성공/실패 여부
     * - blockNumber: 포함된 블록 번호
     * - gasUsed/gasPrice: 가스 사용량 및 가격
     */
    public static void main(String[] args) throws IOException {
        // 환경변수에서 API 키 가져오기
        String infuraKey = System.getenv("INFURA_PROJECT_ID");

        // BSC 테스트넷 RPC 엔드포인트 설정
        String bscTestnetUrl = "https://bsc-testnet.infura.io/v3/" + infuraKey;

        // Web3j 인스턴스 생성 (블록체인 데이터 조회용)
        Web3j web3 = Web3j.build(new HttpService(bscTestnetUrl));

        // 조회할 ERC20 토큰 전송 트랜잭션 해시
        // 이전 ERC20TokenTransfer 실행 결과에서 얻은 해시
        String transactionHash = "0xbbe26bd0ce256785952c8499473bce079ff27c0bf9ff6aec961d0462625dad77";

        // 트랜잭션 영수증 조회 (실행 결과 정보)
        TransactionReceipt transactionReceipt =
            web3.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().get();

        // 트랜잭션 상세 정보 조회 (전송 시점 정보)
        Transaction transaction = web3.ethGetTransactionByHash(transactionHash).send().getTransaction().get();

        System.out.println("================ ERC20 토큰 트랜잭션 조회 ================");
        System.out.println("트랜잭션 해시: " + transactionHash);
        System.out.println();

        // 1. 트랜잭션 해시 (고유 식별자)
        System.out.println("Hash: " + transactionReceipt.getTransactionHash());

        // 2. Nonce (발송자 지갑의 트랜잭션 순서 번호)
        System.out.println("Nonce: " + transaction.getNonce());

        // 3. From (토큰을 보낸 지갑 주소)
        System.out.println("From: " + transactionReceipt.getFrom());

        // 4. To (ERC20 컨트랙트 주소 - 실제 토큰 받는 주소가 아님!)
        System.out.println("To (Contract): " + transactionReceipt.getTo());

        // 5. Value (네이티브 코인 전송량 - wei 단위)
        // ERC20 토큰 전송에서는 항상 0 (ETH를 전송하지 않음)
        System.out.println("Value (ETH): " + transaction.getValue() + " wei");

        // 6. Status (트랜잭션 실행 결과)
        // 0x1 = 성공, 0x0 = 실패
        String status = transactionReceipt.getStatus();
        System.out.println("Status: " + status + " (" + ("0x1".equals(status) ? "성공" : "실패") + ")");

        // 7. Block Number (트랜잭션이 포함된 블록 번호)
        System.out.println("Block Number: " + transactionReceipt.getBlockNumber());

        // 8. Gas Used (실제 사용된 가스량)
        System.out.println("Gas Used: " + transactionReceipt.getGasUsed());

        // 9. Gas Price (가스 단가 - wei 단위)
        System.out.println("Gas Price: " + transaction.getGasPrice() + " wei");

        // 10. 추가 정보: 실제 수수료 계산
        System.out.println();
        System.out.println("=== 추가 정보 ===");
        System.out.println("실제 수수료: " + 
            transactionReceipt.getGasUsed().multiply(transaction.getGasPrice()) + " wei");
        
        // === ERC20 토큰 전송 정보 파싱 (로그 분석) ===
        System.out.println("\n=== ERC20 Transfer 이벤트 로그 분석 ===");
        
        // 1. 트랜잭션 로그 목록 가져오기
        // ERC20 transfer() 함수 실행 시 Transfer 이벤트가 발생하여 로그에 기록됨
        List<Log> logs = transactionReceipt.getLogs();
        System.out.println("로그 개수: " + logs.size());
        
        // 2. 첫 번째 로그 (Transfer 이벤트) 추출
        // ERC20 토큰 전송에서는 보통 첫 번째 로그가 Transfer 이벤트
        Log transferLog = logs.get(0);
        
        // 3. 이벤트 토픽 목록 가져오기
        // Topics: 이벤트의 인덱싱된 파라미터들이 저장되는 배열
        // Transfer(address indexed from, address indexed to, uint256 value)
        List<String> topics = transferLog.getTopics();
        System.out.println("토픽 개수: " + topics.size());
        
        // 4. 실제 토큰을 받은 주소 추출
        // topics[0]: Transfer 이벤트 시그니처 (0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef)
        // topics[1]: from 주소 (32바이트로 패딩됨)
        // topics[2]: to 주소 (32바이트로 패딩됨)
        // data: 전송된 토큰 양 (value)
        
        // 주소는 20바이트(40자리)이지만 32바이트(64자리)로 패딩되어 저장됨
        // 앞의 24자리(12바이트)는 0으로 패딩, 뒤의 40자리가 실제 주소
        // substring(26): "0x" + 24자리 패딩을 제거하고 실제 주소 40자리만 추출
        String toAddress = "0x" + topics.get(2).substring(26);
        System.out.println("토큰 받은 주소 (실제 To): " + toAddress);
        
        // 5. 추가 정보: from 주소도 동일한 방식으로 추출 가능
        String fromAddress = "0x" + topics.get(1).substring(26);
        System.out.println("토큰 보낸 주소 (실제 From): " + fromAddress);
        
        // 6. Transfer 이벤트 시그니처 확인
        System.out.println("Transfer 이벤트 시그니처: " + topics.get(0));
        
        // 7. 전송된 토큰 양은 data 필드에 저장됨 (16진수)
        System.out.println("전송된 토큰 양 (hex): " + transferLog.getData());
    }

}

    /**
     * Transaction vs TransactionReceipt 차이점 정리
     * 
     * Transaction (전송 시점 정보)
     * - 사용자가 전송할 때 설정한 의도와 값들
     * - 블록체인에 전송되기 전의 원본 데이터
     * 
     * TransactionReceipt (실행 결과 정보)
     * - 블록체인에서 실제 실행된 결과
     * - 가스 사용량, 성공/실패, 로그 등 실행 정보
     * 
     * === Transaction 전용 필드 ===
     * - nonce : 거래 순서 번호
     * - gasPrice : 사용자가 설정한 가스 가격
     * - gas (gasLimit) : 사용자가 설정한 최대 가스량
     * - value : 전송할 네이티브 코인 양 (wei)
     * - input : 스마트 컨트랙트 호출 데이터
     * - v, r, s : 디지털 서명 정보
     * 
     * === TransactionReceipt 전용 필드 ===
     * - status : 실행 결과 (0x1=성공, 0x0=실패)
     * - gasUsed : 실제 소모된 가스량
     * - cumulativeGasUsed : 블록 내 누적 가스 사용량
     * - logs : 스마트 컨트랙트 이벤트 로그
     * - logsBloom : 로그 검색 최적화용 블룸 필터
     * - contractAddress : 컨트랙트 생성 시 새 주소
     * - effectiveGasPrice : 실제 적용된 가스 가격
     * - type : 트랜잭션 타입
     * 
     * === 공통 필드 ===
     * - transactionHash: 트랜잭션 고유 식별자
     * - from : 보내는 주소
     * - to : 받는 주소
     * - blockHash/blockNumber : 포함된 블록 정보
     * - transactionIndex : 블록 내 트랜잭션 순서
     * 
     * === ERC20 토큰 전송에서의 특징 ===
     * - Transaction.value = 0 (네이티브 코인 전송 없음)
     * - Transaction.to = 컨트랙트 주소 (실제 토큰 받는 주소 아님)
     * - 실제 토큰 전송 정보는 TransactionReceipt.logs에서 확인
     * - Transfer 이벤트 : topics[0]=이벤트시그니처, topics[1]=from, topics[2]=to, data=amount
     */