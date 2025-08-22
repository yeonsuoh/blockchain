package contractdeployment.web3j;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

public class ERC20ContractFunctionCall {

    /**
     * - 5.5 Web3j를 사용하여 배포한 ERC20 컨트랙트 함수 호출
     *     - balanceOf
     *     - transfer
     *     - approve
     *     - transferFrom
     */
    public static void main(String[] args) throws Exception {
        // 계정 주소 설정
        String aAddress = "0x658b8a1ae242d0460d4777e17C9Af438dAAB4f77"; // 토큰 발생 계정
        String bAddress = "0x192897df0B17c99fA24eCF998c22e2E83C3cD3D8"; // 수신 계정

        // 환경변수에서 키 정보 가져오기
        String infuraKey = System.getenv("INFURA_PROJECT_ID");
        String aPrivateKey = System.getenv("A_PRIVATE_KEY");
        String bPrivateKey = System.getenv("B_PRIVATE_KEY");

        // Sepolia 테스트넷 연결 설정
        String sepoliaUrl = "https://sepolia.infura.io/v3/" + infuraKey;
        Web3j web3 = Web3j.build(new HttpService(sepoliaUrl));
        
        // 계정 자격증명 생성
        Credentials aCredentials = Credentials.create(aPrivateKey);
        Credentials bCredentials = Credentials.create(bPrivateKey);

        // 체인 ID 설정
        long sepoliaTestnetChainId = 11155111L;

        // 트랜잭션 매니저 생성
        RawTransactionManager aTxManager = new RawTransactionManager(web3, aCredentials, sepoliaTestnetChainId);
        RawTransactionManager bTxManager = new RawTransactionManager(web3, bCredentials, sepoliaTestnetChainId);

        // 배포된 ERC20 컨트랙트 주소
        // ERC20ContractDepolyment에서 생성한 ERC20 토큰 SEO의 토큰 컨트렉트 주소
        // https://sepolia.etherscan.io/token/0x1797da0ffff01bf87fef6becfa9e943e31540e03
        String contractAddress = "0x1797da0ffff01bf87fef6becfa9e943e31540e03";

        // 각 계정별 컨트랙트 인스턴스 로드
        ERC20 aContract = ERC20.load(contractAddress, web3, aTxManager, getLowGasProvider());
        ERC20 bContract = ERC20.load(contractAddress, web3, bTxManager, getLowGasProvider());

        // 1. balanceOf 호출 - A 계정의 토큰 잔액 조회
        BigInteger balanceOf = aContract.balanceOf(aAddress).send();
        System.out.println("A 계정 잔액: " + Convert.fromWei(String.valueOf(balanceOf), Unit.ETHER) + " SEO");

        // 2. transfer 호출 - A에서 B로 10 SEO 직접 전송
        BigDecimal transferValue = Convert.toWei("10", Unit.ETHER);
        TransactionReceipt transactionReceipt = aContract.transfer(bAddress, transferValue.toBigInteger()).send();
        System.out.println("Transfer 완료: " + transactionReceipt.getTransactionHash());
        // transactionHash = '0xfc63ce26757b7b6b7eed4e14fbbc6348456ad1e10cd38bea218d83d0adc4c123'
        // https://sepolia.etherscan.io/tx/0xfc63ce26757b7b6b7eed4e14fbbc6348456ad1e10cd38bea218d83d0adc4c123

        // 3. approve 호출 - A가 B에게 10 SEO 사용 권한 부여
        BigDecimal approveValue = Convert.toWei("10", Unit.ETHER);
        TransactionReceipt approveReceipt = aContract.approve(bAddress, approveValue.toBigInteger()).send();
        System.out.println("Approve 완료: " + approveReceipt.getTransactionHash());
        // transactionHash = '0x5b6e0f6432788aa3aa2c731f93a9306a74f42d35992962e48c1c3867266b0bfb'
        // https://sepolia.etherscan.io/tx/0x5b6e0f6432788aa3aa2c731f93a9306a74f42d35992962e48c1c3867266b0bfb

        // 4. transferFrom 호출 - B가 A의 승인된 토큰 중 5 SEO를 A에서 B로 전송
        BigDecimal transferFromValue = Convert.toWei("5", Unit.ETHER);
        TransactionReceipt transferFromReceipt = bContract.transferFrom(aAddress, bAddress, transferFromValue.toBigInteger()).send();
        System.out.println("TransferFrom 완료: " + transferFromReceipt.getTransactionHash());
        // transactionHash = '0x8e292137c1e0a5675d853a9e5db06420f31b32691aae44f0021838e75f564652'
        // https://sepolia.etherscan.io/tx/0x8e292137c1e0a5675d853a9e5db06420f31b32691aae44f0021838e75f564652

    }
    /**
     * 낮은 가스비 설정 (Sepolia 테스트넷용)
     */
    private static StaticGasProvider getLowGasProvider() {
        BigInteger gasPrice = BigInteger.valueOf(1_000_000_000L);  // 1 gwei (기본값보다 낮음)
        BigInteger gasLimit = BigInteger.valueOf(100_000L);       // 충분한 가스 한도

        return new StaticGasProvider(gasPrice, gasLimit);
    }

}

/*
[ ERC20 함수 호출 정리 ]

1. balanceOf(address account) → uint256
   - 특정 주소의 토큰 잔액 조회
   - 읽기 전용 함수 (가스비 없음)
   - 반환값 : wei 단위의 토큰 양

2. transfer(address to, uint256 amount)
   - 내 토큰을 다른 주소로 전송
   - 쓰기 함수 (가스비 필요)
   - amount는 wei 단위로 입력

3. approve(address spender, uint256 amount)
   - 다른 주소가 내 토큰을 사용할 수 있도록 승인
   - 쓰기 함수 (가스비 필요)

4. transferFrom(address from, address to, uint256 amount)
   - 승인받은 토큰을 대신 전송
   - 쓰기 함수 (가스비 필요)
   - approve로 먼저 승인받아야 함

[ 주의사항 ]
- 모든 amount는 wei 단위 (10^18)
- transfer와 transferFrom은 잔액이 충분해야 함
- transferFrom은 approve로 승인받은 양만큼만 가능
- 실패 시 예외가 발생하므로 try-catch 처리 필요
*/
