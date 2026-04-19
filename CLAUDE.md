# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# 빌드
./gradlew build

# 테스트 제외 빌드
./gradlew clean build -x test

# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "com.example.projectblog.service.UserServiceTest"

# 애플리케이션 실행 (Redis가 먼저 실행되어 있어야 함)
./gradlew bootRun

# Docker로 Redis + 애플리케이션 실행
docker-compose up
```

## 아키텍처

### 도메인 구조

`src/main/java/com/example/projectblog/domain/` 아래 세 도메인으로 분리:
- `user` — 회원가입/로그인, UserRoleEnum(USER/ADMIN)
- `post` — 게시글 CRUD + 좋아요(PostLike)
- `comment` — 댓글 CRUD + 대댓글(CommentComment) + 좋아요(CommentLike)

각 도메인은 `controller / dto / entity / repository / service` 구조를 따름.

### 인증 흐름

**이중 토큰 방식:**
- **AccessToken** — `Authorization` 헤더, HS256, 1시간 만료
- **RefreshToken** — HttpOnly 쿠키, Redis에 저장(`@RedisHash`), UUID:username 형태, 14일 만료

요청마다 `JwtAuthFilter`(OncePerRequestFilter)가 실행:
1. 헤더에서 AccessToken 추출 → 유효성 검증
2. AccessToken 만료 시 쿠키에서 RefreshToken 파싱(`UUID:username` split)
3. Redis에 RefreshToken 존재하면 새 AccessToken 발급 후 응답 헤더에 추가
4. 유효하면 `SecurityContextHolder`에 인증 객체 등록

### 권한 처리

`WebSecurityConfig`에서 STATELESS 세션 + JWT 필터 체인 구성. `/api/user/**` 경로만 permitAll, 나머지는 인증 필요. ADMIN 역할은 `@Secured` 또는 서비스 레이어에서 `UserRoleEnum.ADMIN` 비교로 처리.

### Self-injection 패턴

`PostService`와 `CommentService`에서 `@Resource`로 자기 자신을 주입(`thisPostService`)하여 `@Transactional` 프록시를 통한 내부 메서드 호출을 보장. 일반 `this.method()` 호출은 트랜잭션이 적용되지 않으므로 이 패턴을 유지할 것.

### 외부 연동

- **AWS S3** — `S3UploaderService`로 이미지 업로드, `AmazonS3Config`에서 AWS 자격증명 설정
- **Redis** — RefreshToken 저장소로만 사용 (`RefreshTokenRepository extends CrudRepository`)
- **H2** — 개발용 인메모리 DB (h2-console 활성화됨)

### 공통 유틸

- `Timestamped` — JPA `@MappedSuperclass`, createdAt/modifiedAt 자동 관리
- `MessageResponseDto` — 단순 메시지 응답 공통 DTO (`message`, `statusCode`)
- `ExceptionAOP` — 현재 비어있음, 예외 처리 AOP 확장 예정 위치

### Kafka 비동기 이벤트

`util/kafka/` 아래에 구성:
- `config/KafkaTopicConfig` — 토픽 4개 정의 (`blog.post.created`, `blog.comment.created`, `blog.post.liked`, `blog.comment.liked`)
- `event/` — 이벤트 POJO (`PostCreatedEvent`, `CommentCreatedEvent`, `LikeEvent`)
- `producer/BlogEventProducer` — ObjectMapper로 JSON 직렬화 후 `KafkaTemplate<String, String>`으로 전송. 파티션 키는 `postId` / `commentId`로 지정해 순서 보장

`domain/notification/` — 알림 도메인:
- `NotificationConsumer` — `@KafkaListener`로 4개 토픽 구독, 본인 행동(자기 글에 좋아요 등)은 알림 제외 처리
- `NotificationService` — 알림 저장/조회/읽음 처리
- API: `GET /api/notifications`, `PUT /api/notifications/{id}/read`, `GET /api/notifications/unread-count`

**이벤트 발행 위치**: `PostService.createPost`, `PostService.postLike`, `CommentService.createComment`, `CommentService.commentLike`에서 DB 저장 직후 발행. Transactional Outbox 패턴 미적용 상태이므로 DB 커밋 전 Kafka 발행 가능성 존재.

### 환경 변수

`application.yml`에 다음 설정 필요:
- `jwt.secret.key` — Base64 인코딩된 JWT 서명 키
- `spring.kafka.bootstrap-servers` — 기본값 `localhost:9092`, Docker Compose 시 `kafka:29092`로 오버라이드
- AWS S3 자격증명 및 버킷 정보
- Redis 연결 정보
