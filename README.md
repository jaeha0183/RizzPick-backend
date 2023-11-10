# RizzPick

![99수정 1](https://github.com/RizzPick/RizzPick-backend/assets/114673187/e29caf7a-d3df-4d45-9c4e-5295a53d3e4d)

## 📎 https://rizzpick.com

## 프로젝트 소개

> "우리 뭐할까요?"라는 질문에서 출발해 사용자들이 매력적인 데이트 계획을 통해 깊고 의미 있는 관계를 맺을 수 있도록 유도하고자 합니다.
---
## ✅ 서비스 핵심 기능

### **1. 데이트 계획 공유 및 선택** 
>사용자들이 개인적으로 기획한 데이트 아이디어를 공유하고, 상대방의 계획을 보며 선택하는 기능입니다.

### **2. 개인 프로필 기반 추천**

> 지역, 성별을 기반으로 상대방과의 추천이 이루어집니다.

---
## 🗓 프로젝트 기간
2023년 10월 4일 ~ 2023년 11월 15일 (6주)

---
## ⚙️ 서비스 아키텍처
![ServiceArc](https://github.com/RizzPick/RizzPick-backend/assets/114673187/8279253d-1b33-454b-ab92-b62182f049b2)

---
## 💬 기술적 의사 결정

| 기술             | 설명                                                                                                                                                                                                                                       |
|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nginx**        | 기존 Apache의 WAS에서 추가적으로 HTTPS 연결과 대용량 이미지 처리 요청에 대한 설정이 필요하다 판단하여 추가. Apache와 비교하여 더 빠른 성능을 제공하는 웹서버로, 높은 트래픽 처리와 안정성을 위해 사용.                                             |
| **GitHub Actions** | 프론트엔드와 백엔드의 효율적인 협업을 위한 자동배포를 진행. 소스 코드의 변경 사항에 대한 자동화된 CI/CD 파이프라인을 구축할 수 있으며, 지속적인 통합 및 배포를 위해 적용.                                                                              |
| **Redis(EC2)**   | 임시 데이터 사용과 캐싱에 적합하여 사용자의 빈번한 엑세스가 발생하는 데이터를 Redis에 저장하여 데이터 엑세스 속도를 높임. EC2 인스턴스를 사용하여 원하는 OS, 메모리, 스토리지 및 CPU 구성의 선택 제공. 수직 및 수평 스케일링 가능. AWS 보안 그룹을 통한 접근 제한. |
| **Swagger**      | 프론트엔드와 백엔드의 빠르고 직관적인 의사소통을 위해 API 기능을 문서화하고 테스트하기 위해 적용. API 문서화와 테스트 도구로서 개발자들이 API의 동작을 쉽게 이해하고 테스트 할 수 있도록 도와줌.                                                          |
| **Docker Hub**   | 추후 EC2의 서버 인스턴스 내에서 다중 컨테이너와 Nginx를 활용한 다중 처리를 진행하기 위해, 먼저 Docker Hub에서 배포를 진행하기로 결정. 개발에 프로그램을 실행하기 전 컴파일 단계에서 오류를 찾을 수 있는 장점으로 사용.                                       |
| **QueryDSL**     | Soft Delete와 사용자 추천 로직에 있어 복잡해진 쿼리문을 직관적으로 바꾸기 위해 사용. 추가적으로 POST, UPDATE, DELETE를 제외한 GET 요청에 있어서 QueryDSL을 적용하기로 결정.                                                                      |
| **WebSocket**    | 서버와 클라이언트 간의 실시간 연결을 제공하여 실시간 채팅 기능을 구현하기 위해 적용.                                                                                                                                                        |
| **SSE(Redis PUB/SUB)** | (SSE) 서버와 클라이언트간의 실시간 연결을 제공하여 실시간 알림 푸시 기능을 구현하기 위해서 적용 (PUB/SUB) 다수의 클라이언트에게 알림을 효과적으로 전달하기 위해 적용서버에서 발생한 이벤트를 PUB/SUB 으로 관리하여, 토픽을 구독한 구독자에게 SSE를 통해 효과적인 실시간 알림 기능을 구현                                       |


---
## 📑 ERD
![RizzPickERD](https://github.com/RizzPick/RizzPick-backend/assets/114673187/86754c1c-2360-4dd1-a7c1-0daf712f4bab)

---
## 🛠트러블슈팅

<details>
<summary>사용자 추천로직에서의 Redis 사용</summary>
<div markdown="2">

> 문제

사용자별 추천 로직을 만들기 위해 Recommendations라는 Entity를 만들어서 관리하려 했으나 유저별로 Recommendations를 추가할시 MySQL에 너무 많은 양의 쿼리가 만들어지는게 예상이 됐고 너무 많은 요청이 보내질 거라 예상된다.

> 오류 해결 시도

Redis를 활용하여 사용자가 로그인한 시점에 Recommendations라는 Entity를 만들어 저장하고 일정 시간이 지난 뒤 혹은 유저가 로그아웃하면 Redis에서 삭제를 한다.

하지만, Redis 사용자에 대한 정보를 담는다 하더라도 MySQL에서 사용자 프로필을 다시 조회해야 했고 사용자 프로필 자체를 Redis에 저장하게 되면 조회를 하지 못하는 문제가 생겼다. 추가적으로 MVP내에서 사용자 추천 필터를 확실히 정하지 않았기에 서비스 적으로도 효율적으로도 Redis를 사용할 이유가 없어졌다.

> 오류 해결 방법

사용자 추천 API를 사용자별로 설정하게 만들어지기 전까지 MySQL에서 조회하는 방식을 사용한다.
</div>
</details>

<details>
<summary>Websocket 채팅방이 삭제되는 현상</summary>
<div markdown="3">

> 문제

Websocket을 활용하여 실시간 채팅 기능을 구현하던 중, 실시간 채팅 후 예기치 않게 Redis의 채팅방이 랜덤하게 삭제되거나 해당 사용자가 채팅방에서 삭제되는 현상이 있었다.

> 오류 해결 시도

    
Websocket의 경우 백엔드에서만으로는 이러한 문제를 정확하게 파악하기가 어려웠다. 그래서 프론트엔드 팀과 협업을 시작했고, 양쪽 모두의 코드를 상호 검토해 보면서 오류의 원인을 찾으려고 노력했다. 이 과정에서 발생한 오류 로그를 기반으로 구글링을 진행했고, ChatGPT의 도움도 받아 원인을 해결하려고 했다.
    
> 오류 해결 방법

    
문제 상황에서 다음과 같은 에러 로그가 관찰되었다.

```java
com.willyoubackend.global.exception.CustomException: null
        at com.willyoubackend.domain.websocket.service.ChatRoomService.validateChatRoomId(ChatRoomService.java:132) ...
        ...
        at java.base/java.lang.Thread.run(Thread.java:833) ...
```
    
로그 내용을 보면, **`ChatRoomService`**의 **`validateChatRoomId`** 메서드에서 **`CustomException`**이 발생하고 있었다. 이 메서드는 **`ChatRoomId`**의 유효성을 검사하는 코드로 이 로그는 이 유효성 검사에서 문제가 발생했음을 나타내주고 있었다.
    
문제 해결의 핵심은 아래의 **`ChatRoomId`** 검증 코드를 수정하는 것이었다.
    
```java
// chatRoomId 검사 메서드
private void validateChatRoomId(Long chatRoomId) {
    if (chatRoomId == null) {
        throw new CustomException(ErrorCode.INVALID_CHATROOM_ID);
    }
}
   ```
    
이 코드를 일시적으로 삭제한 후 다시 실행해 보니, 문제가 해결된 것을 확인할 수 있었다.
또한 Redis에 채팅방 정보를 저장하는 것은 휘발성이 높은 데이터이므로, Redis에 저장하는 대신 MySQL에 저장하는 방식으로 변경했다.

</div>
</details>

<details>
<summary>QueryDSL 활용 DB 쿼리 최적화</summary>
<div markdown="4">

> 문제

1. 사용자 목록을 가져오기 위해서 JPA를 사용하여 쿼리를 작성했음. 그러나 이 방법을 사용했을때 쿼리문이 길었으며, 유연성이 떨어졌음.
2. 특정 쿼리 조회 시 요청별로 구성하는 값이 다를경우 다른 요청을 만들거나 쿼리문이 복잡해지는 상황이 발생.

> 오류 해결 시도

1. 문제를 해결하기 위해 QueryDSL 을 도입하여 쿼리문을 더 간결하고 유연하게 작성하려고 했음. 코드의 가독성과 유연성은 좋아졌지만, 쿼리문의 길이는 JPA만을 사용했을 때와 비슷했음.

> 오류 해결 방법

1. QueryDSL의 ‘leftJoin’ 과 ‘fetchJoin’ 기능을 활용하여 쿼리문을 최적화함. 이 기능을 통해 필요 필요한 데이터를 한 번의 쿼리로 가져오하게 하여, 실행되는 쿼리문의 수를 줄임.
</div>
</details>

---

## 🧑🏻‍💻 팀원 소개

| 이름      | 역할  | 깃허브 주소                         |
|---------|-----|-------------------------------|
| 정우용 (B) | 팀장  | https://github.com/jwywoo     |
| 소석진 (F) | 부팀장 | https://github.com/seokjin909    |
| 김연수 (F) | 팀원  | https://github.com/Xeonxoo99    |
| 전진웅 (B) | 팀원  | https://github.com/JJW11111   |
| 김우응 (B) | 팀원  | https://github.com/Gimwooeung |
| 이재하 (B) | 팀원  | https://github.com/jaeha0183  |


---
## [프론트 깃허브 링크](https://github.com/RizzPick/RizzPick-frontEnd)

---
## [백엔드 깃허브 링크](https://github.com/RizzPick/RizzPick-backend)

---

