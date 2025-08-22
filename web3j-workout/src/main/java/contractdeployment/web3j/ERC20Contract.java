package contractdeployment.web3j;

import java.math.BigInteger;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * ERC20 컨트랙트 배포 및 상호작용을 위한 래퍼 클래스
 * Web3j의 Contract 클래스를 상속받아 블록체인과의 통신을 처리
 */
public class ERC20Contract extends Contract {

    /**
     * ERC20Contract 생성자
     * 이미 배포된 컨트랙트와 연결할 때 사용
     */
    protected ERC20Contract(String contractAddress, Web3j web3j,
                            TransactionManager transactionManager,
                            ContractGasProvider gasProvider) {
        super("",       // ABI 빈 문자열 - 이 클래스는 배포만 담당하므로
                contractAddress,     // 컨트랙트 주소
                web3j,               // Web3j 인스턴스
                transactionManager,  // 트랜잭션 매니저
                gasProvider);        // 가스 제공자
    }

    /**
     * ERC20 컨트랙트를 블록체인에 배포하는 정적 메소드
     */
    public static ERC20Contract deploy(Web3j web3j,
        TransactionManager transactionManager,
        ContractGasProvider gasProvider,
        String bytecode,
        String encodedConstructor) throws Exception {

        // Web3j의 deployRemoteCall을 사용하여 컨트랙트 배포
        return deployRemoteCall(
            ERC20Contract.class,    // 반환할 컨트랙트 클래스 타입
            web3j,                  // 블록체인 연결
            transactionManager,     // 트랜잭션 서명 및 전송 관리
            gasProvider,            // 가스 가격 및 한도 설정
            bytecode,               // 배포할 컨트랙트 바이트코드
            encodedConstructor,     // 생성자에 전달할 파라미터 (인코딩됨)
            BigInteger.ZERO         // 컨트랙트 배포 시 전송할 ETH (0 ETH)
        ).send();                   // 트랜잭션 전송 및 결과 대기
    }
}
