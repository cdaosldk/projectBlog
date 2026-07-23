# projectBlog : 기본 게시판 CRUD에 다양한 기능 구현 추가 프로젝트

# 개발환경

- Spring Boot Version : 3.5,9

- Java 21

- Gradle

- Redis 2.7.6

- AWS S3

- kafka

# 현재까지 구현한 기능

- 게시판 CRUD
- 댓글 CRUD
- 실시간 알림 기능 : 카프카 활용
- 엑셀 생성 기능 : 객체지향
- AWS S3로 이미지 파일 관리
- Spring Security
- CI/CD : git hub action & AWS Code Deploy
- Redis를 활용한 JWT RefreshToken
- 도커화

# Used Case

[blogUsecase.drawio](blogUsecase.drawio)

# ERD

![image](https://user-images.githubusercontent.com/110814973/210943656-3e44fd3a-a1c7-4f9e-bc8c-7a5f0b820cc4.png)

# 구현 간 문제상황 및 해결 노력

1) 카프카 도입 시 고려한 기술 trade-off
    - 카프카 도입할 때 유사하게 사용될 수 있는 Spring Simple Broker와 Spring Application Events를 알아봤다
        - ([기술 블로그 상세 분석 링크](https://cdaosldk.tistory.com/385))

2) 엑셀 파일 생성 시 속도 개선 
   - OOM 원인 분석 및 해결:
      - 초기 개발 시 13만 개 데이터 기준 로컬 환경에서 발생하던 힙 메모리 부족(OOM) 현상을 진단, 모든 Cell 객체를 메모리에 보유하는 XSSFWorkbook 대신 임시 디스크 캐싱 플러시 방식의 SXSSFWorkbook을 도입**하여 가비지 컬렉터(GC) 부담을 최소화하고 안정성을 확보
            - ([기술 블로그 상세 분석 링크](https://cdaosldk.tistory.com/384))
   - 유연성 및 객체지향 설계:** 다양한 도메인의 엑셀 다운로드 요구사항에 유연하게 대응하기 위해, 
     - **커스텀 애너테이션과 Java 리플렉션을 활용한 공통 ExcelService 모듈을 구축**하여 OOP(객체지향 프로그래밍) 원칙 및 재사용성 준수.- Java 가비지 컬렉터 부담을 줄이기 위해 셀 객체를 직접 메모리에 올리지 않고 대용량 처리에 적합한 캐싱 전략으로 전환
            - ([기술 블로그 상세 분석 링크](https://cdaosldk.tistory.com/346))


