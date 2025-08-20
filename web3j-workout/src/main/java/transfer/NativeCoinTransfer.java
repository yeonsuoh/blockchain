package transfer;

import java.math.BigDecimal;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert.Unit;

public class NativeCoinTransfer {

    /**
     * 3. 네이티브 코인 전송
     * 3.1 네이티브 코인 전송 (from, to, amount)
     */
    public static void main(String[] args) throws Exception {
        // 네이티브 코인 (Ether) 전송에는 최소한의 세부 사항이 필요
        // to : 목적지 지갑 주소
        // value : 대상 주소로 보내려는 이더의 양

        // https://cloud.google.com/application/web3/faucet/ethereum/sepolia
        // faucet 이용, ethereum sepolia 발급받음

        // https://developer.metamask.io/
        // api key 얻음 - cbc3bffb4dd24160aef803fb94436c28

        // Sepolia 테스트넷 (테스트 ETH 사용)

        String infuraKey = System.getenv("INFURA_PROJECT_ID");
        String privateKey = System.getenv("PRIVATE_KEY");

        String sepoliaUrl = "https://sepolia.infura.io/v3/" + infuraKey;

        String toAddress = "0x192897df0B17c99fA24eCF998c22e2E83C3cD3D8";
        BigDecimal value = new BigDecimal("0.000001");

        Web3j web3 = Web3j.build(new HttpService(sepoliaUrl));
        Credentials credentials = Credentials.create(privateKey);

        TransactionReceipt transactionReceipt =
            Transfer.sendFunds(web3, credentials, toAddress, value, Unit.ETHER).send(); // 동기 방식

        System.out.println("transactionReceipt = " + transactionReceipt);
        /*
        메서드 실행 결과

        transactionReceipt =
        TransactionReceipt{transactionHash='0x0db5f62336bc07a59ec38ea5a2b2b4a30d5819650fb7e2bbd988766d97b69022',
        transactionIndex='0x6e', blockHash='0x06427d3260986f289a61cddff8332a6df2630279ad69b1f9910c0729b7225359', blockNumber='0x89af7a',
        cumulativeGasUsed='0x10540bc', gasUsed='0x5208', contractAddress='null', root='null', status='0x1',
        from='0x658b8a1ae242d0460d4777e17c9af438daab4f77', to='0x192897df0b17c99fa24ecf998c22e2e83c3cd3d8',
        logs=[], logsBloom='0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000',
        revertReason='null', type='0x0', effectiveGasPrice='0x42f40a', blobGasPrice='null', blobGasused='null'}


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
