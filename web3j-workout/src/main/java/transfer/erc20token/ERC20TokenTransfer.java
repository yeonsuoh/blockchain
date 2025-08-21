package transfer.erc20token;

import java.math.BigInteger;
import org.jetbrains.annotations.NotNull;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

public class ERC20TokenTransfer {


    /**
     * 4. ERC20 토큰 전송
     * 4.1 ERC20 토큰 전송 (from, to, amount)
     */
    public static void main(String[] args) throws Exception {
        String privateKey = System.getenv("PRIVATE_KEY");
        String infuraKey = System.getenv("INFURA_PROJECT_ID");

        String bscTestnetUrl = "https://bsc-testnet.infura.io/v3/" + infuraKey;
        // Web3를 블록체인에 연결하기
        Web3j web3 = Web3j.build(new HttpService(bscTestnetUrl));

        // 토큰 보낼 지갑 주소 준비
        String toAddress = "0x658b8a1ae242d0460d4777e17C9Af438dAAB4f77";

        String value = "1"; // 보낼 토큰 수량 1개
        BigInteger weiValue = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger(); // wei로 단위 변환

        Credentials credentials = Credentials.create(privateKey);

        // Chain Id - 네트워크를 구분하는 고유 번호
        long bscTestnetChainId = 97L;

        // RawTransactionManager - Chain Id를 포함한 트랜잭션을 생성하는 매니저
        // 이를 통해 트랜잭션에 자동으로 Chain Id가 포함되어 보안성 확보
        RawTransactionManager txManager = new RawTransactionManager(web3, credentials, bscTestnetChainId);

        // 테스트용 가스비 설정
        // BSC는 이더리움보다 훨씬 저렴, 낮은 가격으로 가스비 설정 가능
        StaticGasProvider gasProvider = getStaticGasProvider();

        // 컨트랙트를 로드하기
        // STRC의 토큰 컨트랙트 주소
        // https://testnet.bscscan.com/token/0xff9f57987acb440c9752ab3f0af4d00ea1d97d89
        String contractAddress = "0xfF9F57987Acb440C9752ab3F0Af4D00EA1d97d89";
        // 메타마스크에서 STRC 토큰 조회하려면 '토큰 가져오기' 해야 함

        ERC20 contract = ERC20.load(contractAddress, web3, txManager,gasProvider);

        // 토큰 전송 실행
        TransactionReceipt transactionReceipt = contract.transfer(toAddress, weiValue).send(); // 동기 방식

        System.out.println("transactionReceipt = " + transactionReceipt);

        /*
        실행 결과
        transactionReceipt =
        TransactionReceipt{transactionHash='0xbbe26bd0ce256785952c8499473bce079ff27c0bf9ff6aec961d0462625dad77',
        transactionIndex='0x0', blockHash='0xcec4881ea9860ab9404dcb1daba7f5aa0f62a75d0c268cdc46b353665abb7751', blockNumber='0x3ba29b0',
        cumulativeGasUsed='0x8713', gasUsed='0x8713', contractAddress='null', root='null', status='0x1',
        from='0x192897df0b17c99fa24ecf998c22e2e83c3cd3d8', to='0xff9f57987acb440c9752ab3f0af4d00ea1d97d89',
        logs=[Log{removed=false, logIndex='0x0', transactionIndex='0x0',
        transactionHash='0xbbe26bd0ce256785952c8499473bce079ff27c0bf9ff6aec961d0462625dad77',
        blockHash='0xcec4881ea9860ab9404dcb1daba7f5aa0f62a75d0c268cdc46b353665abb7751',
        blockNumber='0x3ba29b0', address='0xff9f57987acb440c9752ab3f0af4d00ea1d97d89',
        data='0x0000000000000000000000000000000000000000000000000de0b6b3a7640000', type='null',
        topics=[0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef,
        0x000000000000000000000000192897df0b17c99fa24ecf998c22e2e83c3cd3d8, 0x000000000000000000000000658b8a1ae242d0460d4777e17c9af438daab4f77]}],
        logsBloom='0x00000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008000000000000010000000000000000000000000000000004000000000000000000000000000000000000400000000010000000000000000000000000000000000000000000000000080080000000000000000000000000800040000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000100000000000000000',
        revertReason='null', type='0x0', effectiveGasPrice='0x1dcd6500', blobGasPrice='null', blobGasused='null'}



         */

    }

    @NotNull
    private static StaticGasProvider getStaticGasProvider() {
        BigInteger gasPrice = BigInteger.valueOf(500_000_000L);   // 0.5 gwei
        BigInteger gasLimit = BigInteger.valueOf(80_000L);        // 토큰 전송에 충분한 가스량
        /*
        [가스 시스템의 기본 원리]
            < Gas Price (가스 가격) >
                - 설정한 0.5 gwei는 각 가스 단위당 지불할 의사가 있는 가격
                - 이는 거래의 우선순위를 결정 -> 높은 가스 가격을 설정하면 빠르게 처리된다.

            < Gas Limit (가스 한도) >
                - 80,000은 이 거래에서 최대로 사용할 수 있는 가스량
                - 실제로는 더 적게 사용될 수 있지만, 이 한도를 넘으면 거래가 실패한다.

         */

        // 고정된 가스 가격과 한도를 사용하는 가스 공급자
        StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
        return gasProvider;
    }

}
/*
네이티브 코인 전송과 ERC20 토큰 전송 시 주의점

- 네이티브 코인은 Transfer.sendFunds()로 전송 가능
    - sendFund() 할 때는 이더 단위로 입력하여도 파라미터에 Unit을 입력하므로, Web3j가 내부적으로 wei로 자동 변환해줌

- 토큰은 ERC20.transfer()로 전송 가능
    - transfer() 시에는 파라미터를 wei 단위로 입력해야 함


 */