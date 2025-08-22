# Web3j BSC Testnet 학습 프로젝트

BSC Testnet을 활용한 Web3j 라이브러리 학습 과제 구현

## 📁 프로젝트 구조

```
src/main/java/
├── wallet/                     # 1. 지갑 관리
├── conversion/                 # 2. 단위 변환
├── transfer/                   # 3-4. 코인/토큰 전송
└── contractdeployment/         # 5. 컨트랙트 배포
```

## 1. 지갑 주소 생성 (Private Key, Public Key)
- 지갑 주소 생성 → [`WalletGenerate.java`](wallet/WalletGenerate.java)
- 1.1 Private Key로 Public Key 추출 → [`PublicKeyExtract.java`](wallet/PublicKeyExtract.java)
- 1.2 생성된 주소 메타마스크에 등록해보기 → [`MetaMaskImport.java`](wallet/MetaMaskImport.java)
- 1.3 Private Key로 서명 생성 → [`PrivateKeySignGenerate.java`](wallet/PrivateKeySignGenerate.java)

## 2. 단위 변환 (wei, gwei, eth)
- 2.1 wei → eth → [`EtherUnitConversion.java`](conversion/EtherUnitConversion.java)
- 2.2 eth → wei → [`EtherUnitConversion.java`](conversion/EtherUnitConversion.java)

## 3. 네이티브 코인 전송
- 3.1 네이티브 코인 전송 (from, to, amount) → [`NativeCoinTransfer.java`](transfer/nativecoin/NativeCoinTransfer.java)
- 3.2 네이티브 코인 트랜잭션 조회 → [`GetNativeCoinTransaction.java`](transfer/nativecoin/GetNativeCoinTransaction.java)

## 4. ERC20 토큰 전송
- 4.1 ERC20 토큰 전송 (from, to, amount) → [`ERC20TokenTransfer.java`](transfer/erc20token/ERC20TokenTransfer.java)
- 4.2 ERC20 트랜잭션 조회 → [`GetERC20TokenTransaction.java`](transfer/erc20token/GetERC20TokenTransaction.java)

## 5. ERC20 컨트랙트 배포
- 5.1-5.3 Remix IDE 사용하여 배포 및 함수 호출 → [`5.ERC20 컨트랙트 배포.md`](contractdeployment/remixide)
- 5.4-5.5 Web3j를 사용하여 배포 및 함수 호출 → [`contractdeployment/web3j`](contractdeployment/web3j)