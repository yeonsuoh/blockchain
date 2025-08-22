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

/**
 * ERC20 토큰 전송 실습
 * 
 * ERC20 토큰 전송 과정
 * 1. 블록체인 네트워크 연결 (BSC Testnet)
 * 2. 지갑 인증 정보 설정 (Private Key)
 * 3. 트랜잭션 매니저 및 가스 설정
 * 4. ERC20 컨트랙트 로드
 * 5. transfer() 함수 호출로 토큰 전송
 */
public class ERC20TokenTransfer {

    /**
     * 4. ERC20 토큰 전송
     * 4.1 ERC20 토큰 전송 (from, to, amount)
     * 
     * 네이티브 코인 전송과의 차이점
     * - 네이티브 코인 : 블록체인 자체 화폐 (BNB, ETH 등)
     * - ERC20 토큰 : 스마트 컨트랙트로 구현된 토큰 (USDT, USDC 등)
     */
    public static void main(String[] args) throws Exception {
        String privateKey = System.getenv("PRIVATE_KEY");     // 지갑 개인키
        String infuraKey = System.getenv("INFURA_PROJECT_ID"); // Infura API 키

        // BSC 테스트넷 RPC URL 구성
        String bscTestnetUrl = "https://bsc-testnet.infura.io/v3/" + infuraKey;
        
        // Web3j 인스턴스 생성 - 블록체인과의 통신 담당
        Web3j web3 = Web3j.build(new HttpService(bscTestnetUrl));

        // 토큰을 받을 지갑 주소
        String toAddress = "0x658b8a1ae242d0460d4777e17C9Af438dAAB4f77";

        // 전송할 토큰 수량 설정
        String value = "1"; // 1개 토큰 전송
        // ERC20 토큰도 18자리 소수점을 사용하므로 wei 단위로 변환 필요
        BigInteger weiValue = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();

        // 개인키로 지갑 인증 정보 생성
        Credentials credentials = Credentials.create(privateKey);

        // BSC 테스트넷 체인 ID (네트워크 식별자)
        long bscTestnetChainId = 97L;

        // 트랜잭션 매니저 생성
        // - Chain ID를 포함하여 리플레이 공격 방지
        // - EIP-155 표준을 따라 트랜잭션 서명
        RawTransactionManager txManager = new RawTransactionManager(web3, credentials, bscTestnetChainId);

        // 가스 설정 (트랜잭션 수수료)
        // BSC는 이더리움 대비 저렴한 가스비 제공
        StaticGasProvider gasProvider = getStaticGasProvider();

        // ERC20 토큰 컨트랙트 주소
        String contractAddress = "0xfF9F57987Acb440C9752ab3F0Af4D00EA1d97d89"; // STRC 토큰
        // BSC 테스트넷 탐색기 : https://testnet.bscscan.com/token/0xff9f57987acb440c9752ab3f0af4d00ea1d97d89
        // 메타마스크에서 토큰 확인: '토큰 가져오기'로 컨트랙트 주소 추가 필요

        // ERC20 컨트랙트 인스턴스 로드
        // Web3j가 자동 생성한 ERC20 래퍼 클래스 사용
        ERC20 contract = ERC20.load(contractAddress, web3, txManager, gasProvider);

        // ERC20 토큰 전송 실행
        // transfer(address to, uint256 amount) 함수 호출
        TransactionReceipt transactionReceipt = contract.transfer(toAddress, weiValue).send();
        // (참고) .send(): 동기 방식 (결과를 기다림)
        // (참고) .sendAsync(): 비동기 방식 (즉시 반환)

        // 트랜잭션 결과 출력
        System.out.println("=== ERC20 토큰 전송 완료 ===");
        System.out.println("트랜잭션 해시 : " + transactionReceipt.getTransactionHash());
        System.out.println("블록 번호 : " + transactionReceipt.getBlockNumber());
        System.out.println("가스 사용량 : " + transactionReceipt.getGasUsed());
        System.out.println("상태 : " + (transactionReceipt.isStatusOK() ? "성공" : "실패"));
        System.out.println("\n전체 영수증 :");
        System.out.println(transactionReceipt);

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