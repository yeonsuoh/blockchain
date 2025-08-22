package contractdeployment.web3j;

import java.util.Arrays;
import java.util.List;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

public class ERC20ContractDeployment {

    /**
     * - 5.4 Web3j를 사용하여 배포
     */
    public static void main(String[] args) throws Exception {
        // 환경변수에서 키 정보 가져오기
        String infuraKey = System.getenv("INFURA_PROJECT_ID");
        String privateKey = System.getenv("PRIVATE_KEY");

        // Sepolia 테스트넷 연결 설정
        String sepoliaUrl = "https://sepolia.infura.io/v3/" + infuraKey;
        Web3j web3 = Web3j.build(new HttpService(sepoliaUrl));

        // 계정 자격증명 생성
        Credentials credentials = Credentials.create(privateKey);

        // 트랜잭션 매니저 설정
        long sepoliaTestnetChainId = 11155111L;
        RawTransactionManager txManager = new RawTransactionManager(web3, credentials, sepoliaTestnetChainId);

        // 가스 설정
        DefaultGasProvider gasProvider = new DefaultGasProvider();

        // ERC20 토큰 생성자 파라미터 설정 (토큰명, 심볼)
        List<Type> params = Arrays.asList(
            new Utf8String("SeoulToken"),  // 토큰 이름
            new Utf8String("SEO")          // 토큰 심볼
        );

        // 생성자 파라미터를 바이트코드로 인코딩
        String encodedConstructor = FunctionEncoder.encodeConstructor(params);

        // ERC20 컨트랙트 배포 실행
        ERC20Contract contract = ERC20Contract.deploy(
            web3, txManager, gasProvider, ERC20ByteCode.bytecode, encodedConstructor
        );

        System.out.println("ERC20 Contract 배포 완료, 주소 : " + contract.getContractAddress());

        // 실행 결과
        // ERC20 Contract 배포 완료, 주소 : 0x1797da0ffff01bf87fef6becfa9e943e31540e03

        // 토큰 배포 결과 조회
        // https://sepolia.etherscan.io/token/0x1797da0ffff01bf87fef6becfa9e943e31540e03

    }
}

/*
[ Solidity 컴파일의 핵심 결과물 ]

- ABI (Application Binary Interface)
: 컨트랙트의 사용 설명서, 어떤 함수들이 있고 어떤 파라미터를 받는지 설명하는 JSON 파일
    - ABI의 역할
    : Java 코드에서 컨트랙트 함수를 호출할 때, ABI를 보고 함수의 파라미터를 올바른 형태로 인코딩하여 블록체인에 전송함

- Bytecode
: 이더리움 가상 머신(EVM)이 실제로 실행하는 기계어 코드 -> Solidity 코드가 컴파일된 최종 결과물
    - Bytecode의 역할
    : 배포할 때 - 1. Bytecode를 이더리움 네트워크에 전송
                2. 새로운 컨트랙트 주소 생성
                3. 해당 주소에 Bytecode 저장
                4. 이후 함수 호출 시 이 Bytecode가 실행됨

 */