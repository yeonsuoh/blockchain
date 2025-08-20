package conversion;

import java.math.BigDecimal;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

public class EtherUnitConversion {

    /**
     * 2. 단위 변환 (wei, gwei, eth)
     * 2.1 wei → eth
     * 2.2 eth → wei
     *
     * wei를 쓰는 이유?
     */
    public static void main(String[] args) {

        // 이더리움 단위 변환 시 'Convert' util 클래스 사용
        // 단위 정리
        // wei : 가장 작은 단위
        // gwei : 가스비 계산에 사용 (10^9 wei)
        // ether : 기본 이더 단위 (10^18 wei)

        // 1 ether = 10의 18승 wei = 10의 9승 gwei

        // 1 . wei -> gwei, wei -> eth
        BigDecimal wei1 = new BigDecimal("18000000000000000000");
        BigDecimal gwei1 = Convert.fromWei(wei1, Unit.GWEI);
        BigDecimal eht1 = Convert.fromWei(wei1, Unit.ETHER);


        System.out.println("=== wei -> gwei, wei -> eth ===");
        System.out.println("wei1 = " + wei1); // wei1 = 18000000000000000000
        System.out.println("gwei1 = " + gwei1);
        System.out.println("eht1 = " + eht1); // eht1 = 18

        // 2. eth -> wei, eth -> gwei
        BigDecimal eth2 = new BigDecimal("0.39");
        BigDecimal wei2 = Convert.toWei(eth2, Unit.ETHER);
        BigDecimal gwei2 = Convert.toWei(eth2, Unit.GWEI);

        System.out.println("=== eth -> wei, eth -> gwei ===");
        System.out.println("eth2 = " + eth2); // eth2 = 0.39
        System.out.println("wei2 = " + wei2); // wei2 = 390000000000000000.00
        System.out.println("gwei2 = " + gwei2);
    }

    /*
    < wei를 쓰는 이유 >
    - 컴퓨터가 소수점 계산 오류 없이 모든 금융 거래를 정수로만 처리하기 위함
    - 컴퓨터가 소수를 처리하는 부동소수점 방식은 일반적인 프로그래밍에서는 문제가 없을 수 있지만, 금융 거래 등에서는 문제가 발생할 수 있다.
    - 이더리움은 이 문제를 해결하기 위해 아주 작은 단위인 wei를 만들고 모든 계산을 wei라는 정수 단위로만 처리한다.
    - 1 Ether를 보내는 트랜잭션은 컴퓨터 내부에서 10의 18승 wei를 보내는 것으로 기록된다.
    - 사람이 거래할 때는 1.5 ETH처럼 보기 편한 단위를 사용하지만, 블록체인 내부에서는 이를 wei를 사용한 정수로 기록되고 계산된다.
    - 비트코인의 사토시와 비슷한 개념 (1BTC = 1억 사토시)
     */

}
