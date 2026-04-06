# 🚀 Github Actions로 ECR & ECS(Fargate/EC2) 무중단 배포(Zero Downtime)

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
- **Fargate vs EC2 설정 차이**:
    - **Fargate**: `awsvpc` 네트워크 모드 사용 (각 컨테이너가 고유 IP 할당)
    - **EC2 (Bridge)**: `bridge` 모드 사용. `links` 설정을 통해 컨테이너 이름으로 통신하도록 DB URL 수정
      (ex: `jdbc:postgresql://postgres-db:5432/...`)

## 4. Github Setting Secret 설정

- AWS Key, DB 비번, S3 버킷명 등 민감 정보를 Secret에 등록

## 5. AWS 설정 (미리 세팅)

- **공통 설정**
    - ECR 생성: 빌드된 이미지 저장할 리포지토리 확보
    - IAM 사용자 생성(추천): ECR, ECS, S3FullAccess 권한 가진 배포 전용 계정 사용
    - 서비스 생성
        - 서비스는 배포 전 미리 만들어져 있어야 업데이트 가능
        - EC2 사용 시 Launch Type을 **EC2**로 설정

- **Fargate 기반 (Serverless)**
    - 인프라 관리 없이 클러스터와 서비스만 생성하면 즉시 배포 가능

- **EC2 기반 (Infrastructure)**
    - **EC2 생성**
        - AMI: `amzn2-ami-ecs-hvm-2.0.20260122-x86_64-ebs` (ECS 최적화 AMI 권장)
        - EC2 전용 IAM Role 적용: `AmazonEC2ContainerServiceforEC2Role` 권한 필수
    - **EC2 인스턴스 - 클러스터 연결 (SSH 접속)**
        ```bash
        # 내 EC2가 어느 클러스터 소속인지 에이전트에 등록하는 작업
        sudo sh -c 'echo "ECS_CLUSTER=my-ec2-test-cluster" >> /etc/ecs/ecs.config' 
        
        # 설정 적용을 위해 에이전트 재시작
        sudo systemctl restart ecs 
        sudo systemctl status ecs ## active(running) 뜨면 성공
        ```
    - **보안 그룹(Security Group) 설정**
        - **80 (HTTP)**: 서비스 접속용 (0.0.0.0/0 인바운드 열기)
        - **22 (SSH)**: 서버 관리용 접속 허용

