package conversion;

import java.math.BigDecimal;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

public class EtherUnitConversion {

    /**
     * 2. 이더리움 단위 변환 (wei, gwei, eth)
     * 
     * 변환 방법:
     * - fromWei(): wei에서 다른 단위로 변환
     * - toWei(): 다른 단위에서 wei로 변환
     * 
     * 실습 내용:
     * 2.1 wei → gwei, wei → eth
     * 2.2 eth → wei, gwei → wei
     */
    public static void main(String[] args) {

        // 이더리움 단위 변환 시 'Convert' util 클래스 사용
        // 단위 정리
        // wei : 가장 작은 단위
        // gwei : 가스비 계산에 사용 (10^9 wei)
        // ether : 기본 이더 단위 (10^18 wei)

        // 1 ether = 10의 18승 wei = 10의 9승 gwei

        // 1. wei -> gwei, wei -> eth
        BigDecimal wei1 = new BigDecimal("18000000000000000000"); // 18 ETH에 해당하는 wei
        BigDecimal gwei1 = Convert.fromWei(wei1, Unit.GWEI);       // wei를 gwei로 변환
        BigDecimal eht1 = Convert.fromWei(wei1, Unit.ETHER);       // wei를 ether로 변환


        System.out.println("=== wei -> gwei, wei -> eth ===");
        System.out.println("wei1 = " + wei1);   // 18000000000000000000 wei
        System.out.println("gwei1 = " + gwei1); // 18000000000 gwei
        System.out.println("eht1 = " + eht1);   // 18 ether

        // 2. eth -> wei, eth -> gwei
        BigDecimal eth2 = new BigDecimal("0.39");              // 0.39 ETH
        BigDecimal wei2 = Convert.toWei(eth2, Unit.ETHER);     // ether를 wei로 변환
        BigDecimal gwei2 = Convert.toWei(eth2, Unit.GWEI);     // gwei 단위로 입력된 값을 wei로 변환

        System.out.println("=== eth -> wei, eth -> gwei ===");
        System.out.println("eth2 = " + eth2);   // 0.39 ether
        System.out.println("wei2 = " + wei2);   // 390000000000000000 wei (0.39 ETH를 wei로 변환)
        System.out.println("gwei2 = " + gwei2); // 390000000 wei (0.39 gwei를 wei로 변환)
    }

    /**
     * wei를 사용하는 이유
     * 
     * 정밀도 문제 해결:
     * - 컴퓨터가 소수점 계산 오류 없이 모든 금융 거래를 정수로만 처리하기 위함
     * - 부동소수점 방식의 정밀도 오류를 방지하여 정확한 금융 계산 보장
     * 
     * 내부 처리 방식:
     * - 이더리움은 모든 계산을 wei(정수) 단위로만 처리
     * - 1 ETH = 10^18 wei로 변환하여 블록체인에 기록
     * - 사용자는 1.5 ETH로 입력하지만, 내부적으로는 1,500,000,000,000,000,000 wei로 처리
     * 
     * 단위 체계:
     * - wei: 가장 작은 단위 (1 wei)
     * - gwei: 가스비 계산용 (10^9 wei = 1 gwei)
     * - ether: 기본 단위 (10^18 wei = 1 ether)
     * 
     * 비교: 비트코인의 사토시와 동일한 개념 (1 BTC = 10^8 사토시)
     */

}
