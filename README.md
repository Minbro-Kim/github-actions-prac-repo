# 🚀 Github Actions로 ECR & ECS 무중단 배포(Zero Downtime)


## 🔄 작동 방식
코드 Push -> 도커 이미지 생성 -> ECR 푸시 -> ECS Task 생성 -> ECS Service 업데이트 (롤링 업데이트)


## 1. Dockerfile 정의
- 애플리케이션 이미지 빌드 (단일 빌드 방식)

## 2. .github/workflows/---.yml 정의
- Github Actions 실행 내용 정의 (CI/CD)
- envsubst를 이용한 환경변수 주입 (ID 공백 주의)

## 3. root/---.json
- ECS Task 실행 명세서 (IaC)
- 모든 설정은 환경변수로 작성하고 ${VARIABLE} 처리
- 컨테이너 간 의존성 및 네트워크 설정(DB & springboot)

## 4. Github Setting Secret 설정
- AWS Key, DB 비번, S3 버킷명 등 민감 정보를 Secret에 등록

## 5. AWS 설정 (미리 세팅)
- ECR 생성: 이미지 저장소 확보
- ECS 클러스터 및 서비스 생성
  - 서비스는 미리 만들어져 있어야 업데이트 가능
- IAM 사용자 생성(추천)
  - ECR, ECS, S3FullAccess 권한을 가진 배포 전용 계정 사용
