package transfer;

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
    String infuraKey = System.getenv("INFURA_PROJECT_ID");

    String sepoliaUrl = "https://sepolia.infura.io/v3/" + infuraKey;

    Web3j web3 = Web3j.build(new HttpService(sepoliaUrl));

    String transactionHash = "0x0db5f62336bc07a59ec38ea5a2b2b4a30d5819650fb7e2bbd988766d97b69022";
    String blockHash = "0x06427d3260986f289a61cddff8332a6df2630279ad69b1f9910c0729b7225359";
    String transactionIndexHex = "0x6e";
    BigInteger transactionIndex = Numeric.toBigInt(transactionIndexHex);
    String blockNumberHex = "0x89af7a";
    BigInteger blockNumber = Numeric.toBigInt(blockNumberHex);
    String indexHex = "0x6e";
    BigInteger index = Numeric.toBigInt(indexHex);

    // 트랜잭션 해시로 조회
    EthTransaction ethTransaction1 = web3.ethGetTransactionByHash(transactionHash).send();
    Transaction transaction1 = ethTransaction1.getTransaction().get();

    System.out.println("transaction1.getHash() = " + transaction1.getHash());
    System.out.println("transaction1.getNonce() = " + transaction1.getNonce());
    System.out.println("transaction1.getFrom() = " + transaction1.getFrom());
    System.out.println("transaction1.getTo() = " + transaction1.getTo());
    System.out.println("transaction1.getValue() = " + transaction1.getValue()); // wei 단위
    System.out.println("transaction1.getBlockNumber() = " + transaction1.getBlockNumber());
    System.out.println("transaction1.getGasPrice() = " + transaction1.getGasPrice());

    // 블록해시와 트랜잭션 인덱스로 조회
    EthTransaction ethTransaction2 = web3.ethGetTransactionByBlockHashAndIndex(blockHash, transactionIndex).send();
    Transaction transaction2 = ethTransaction2.getTransaction().get();
    System.out.println("transaction1.getHash() = " + transaction2.getHash());

    // 블록넘버와 인덱스로 조회
    EthTransaction ethTransaction3 = web3.ethGetTransactionByBlockNumberAndIndex(DefaultBlockParameter.valueOf(blockNumber), index).send();
    Transaction transaction3 = ethTransaction3.getTransaction().get();
    System.out.println("transaction1.getHash() = " + transaction3.getHash());


    if (transaction1.getHash().equals(transaction2.getHash()) && transaction2.getHash().equals(transaction3.getHash())) {
        System.out.println("트랜잭션 조회 완료");
    }

    // -------------------------------------------------------------------------
    // status, gasUsed는 TransactionReceipt로 조회 가능

    TransactionReceipt transactionReceipt = web3.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().get();

    System.out.println("transactionReceipt.getStatus() = " + transactionReceipt.getStatus());
    System.out.println("transactionReceipt.getGasUsed() = " + transactionReceipt.getGasUsed());

    // 실제 가스 가격
    System.out.println("transactionReceipt.getEffectiveGasPrice() = " + Numeric.toBigInt(transactionReceipt.getEffectiveGasPrice()));
}

/*
참고 : https://docs.web3j.io/4.14.0/transactions/transfer_eth/
 */

}

/*
각 필드의 의미
 *     - hash : 트랜잭션의 고유 ID
 *     - nonce : 발송자 지갑에서 몇 번째 트랜잭션인지 / 중복 전송 방지 / 순서대로 처리됨
 *     - from : 보내는 지갑 주소
 *     - to : 받는 지갑 주소
 *     - value : 전송하는 ETH 양 (wei 단위)
 *     - status : 트랜잭션 실행 결과, 0x1 = 성공 / 0x0 = 실패 (가스 부족, 에러 등)
 *     - blockNumber : 이 트랜잭션이 포함된 블록의 번호 (블록체인에서 몇 번째 블록인지)
 *     - gasUsed : 사용된 가스 양
 *     - gasPrice : 가스 1개당 가격 (wei 단위)

 * 총 수수료 : gasUsed * gasPrice

 * *** status, gasUsed는 Transaction에는 존재하지 않고, TransactionReceipt에만 존재하는 필드 ***


 */


