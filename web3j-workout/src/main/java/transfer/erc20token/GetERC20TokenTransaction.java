package transfer.erc20token;

import java.io.IOException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

public class GetERC20TokenTransaction {
    /**
     * - 4.2 ERC20 트랜잭션 조회
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
        String infuraKey = System.getenv("INFURA_PROJECT_ID");

        String bscTestnetUrl = "https://bsc-testnet.infura.io/v3/" + infuraKey;

        // Web3를 블록체인에 연결하기
        Web3j web3 = Web3j.build(new HttpService(bscTestnetUrl));

        String transactionHash = "0xbbe26bd0ce256785952c8499473bce079ff27c0bf9ff6aec961d0462625dad77";

        TransactionReceipt transactionReceipt =
            web3.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().get();

        Transaction transaction = web3.ethGetTransactionByHash(transactionHash).send().getTransaction().get();

        System.out.println("================ ERC 트랜잭션 조회 시작 ================");

        // hash -> 트랜잭션의 해시 값 (고유 ID)
        System.out.println("transactionReceipt.getTransactionHash() = " + transactionReceipt.getTransactionHash());

        // nonce -> 발송자 지갑에서 몇 번째 트랜잭션인지, Transaction에만 존재하는 필드
        System.out.println("transaction.getNonce() = " + transaction.getNonce());

        // from
        System.out.println("transactionReceipt.getFrom() = " + transactionReceipt.getFrom());

        // to
        System.out.println("transactionReceipt.getTo() = " + transactionReceipt.getTo());

        // value -> 전송하는 ETH 양 (wei 단위), Transaction에만 존재하는 필드
        // ETH를 전송하지 않았으므로 이 경우 value = 0
        System.out.println("transaction.getValue() = " + transaction.getValue());

        // status -> 트랜잭션 실행 결과
        System.out.println("transactionReceipt.getStatus() = " + transactionReceipt.getStatus());

        // blockNumber -> 이 트랜잭션이 포함된 블록의 번호
        System.out.println("transactionReceipt.getBlockNumber() = " + transactionReceipt.getBlockNumber());

        // gasUsed -> 사용된 가스 양
        System.out.println("transactionReceipt.getGasUsed() = " + transactionReceipt.getGasUsed());

        // gasPrice -> 가스 1개당 가격 (Transaction에만 존재하는 필드)
        System.out.println("transaction.getGasPrice() = " + transaction.getGasPrice());
    }

}

/*
[ Transaction과 TransactionReceipt 의 필드 차이점 정리 ]

< Transaction에만 있는 필드들 >
- nonce                 // 거래 순서 번호
- gasPrice              // 가스 가격 (또는 maxFeePerGas, masPriorityFeePerGas)
- gas                   // 가스 한도 (gasLimit)
- value                 // 전송할 ETH 양
- input                 // 컨트렉트 호출 데이터
- v, r, s               // 서명 정보


< TransactionReceipt에만 있는 필드들 >
- status                // 성공/실패 (0x0/0x1)
- gasUsed               // 실제 사용된 가스
- cumulativeGasUsed     // 블록 내 누적 가스 사용량
- logs                  // 이벤트 로그들
- logsBloom             // 로그 필터링용 블룸 필터
- contractAddress       // 컨트랙트 생성시 새 주소
- effectiveGasPrice     // 실제 적용된 가스 가격
- type                  // 거래 타입 (0, 1, 2)

< 공통 필드들>
- transactionHash
- from
- to
- blockHash
- blockNumber
- transactionIndex




 */