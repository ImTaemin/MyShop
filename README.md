# 🛒 MyShop
- 무신사, 29CM, 지그재그 같은 온라인 편집샵인 '**MyShop**' 프로젝트  
- 공부했던 JPA와 리액트의 개념을 확실히 잡기 위한 목표

## 🖼️ Architecture
![myshop-architecture](myshop-architecture.png)
- 젠킨스로 CI, CD 구성
- Nginx로 API 서버 로드밸런싱, 멀티 도메인 사용
- 도커로 각 애플리케이션 이미지화
- DB Replication : write-read 구조의 이중화 구성

[추후 유연성, 확장성, 안정성 확보를 위해 고민하고 레디스 도입 고려]