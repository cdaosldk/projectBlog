# projectBlog
이번에 시도하는 것들 :

ERD

JWT를 활용하기

JPA 연관관계 설정

1. ERD (Entity Relation Diagram)
   
작성중..

2. API 명세서

2-1 숙련 Lv 1

2) 새로 구현하는 기능
![image](https://user-images.githubusercontent.com/110814973/208371240-946448f9-91ae-4522-a2d8-8c3d7c157b55.png)

2) 수정하는 기능
![image](https://user-images.githubusercontent.com/110814973/208371331-3508a77f-071d-4b1a-8f09-b53853bc6557.png)


2-2 숙련 Lv2
1) 새로 추가하는 기능

2) 수정하는 기능


3. 프로젝트 시작
   추가하는 클래스

Entity User

UserService

UserRepository

SignupRequestDto

LoginRequestDto

JwtUtil

PostResponseDto

CommentController

CommentService

CommentRepository

CommentRequestDto

CommentResponseDto

Entity Comment


22.12.16 트러블 슈팅

- 클래스를 생성한 후 테스트를 위해 실행 : secret key 설정을 깜빡함 -> app 설정에 추가

- 회원가입 기능을 테스트 하기 위해 서버 구동 및 Postman으로 POST 쿼리 발송 :

email 값이 제대로 전송되지 않음 : Http 500, not-null property references a null or transient value 오류

-> 클래스 간 값의 이동이 제대로 되지 않는 경우, 내 경우엔 ajax 형식으로 UserController에서 값을 받기 때문에 @ResponeBody, 파라미터에 @RequestBody를 붙여줘야 했으나 이를 잊어 배달사고 발생 ~ 해결

- 회원가입 시 아이디, 비밀번호의 글자 수 및 속성 알파벳, 숫자 제한 조건 추가하기 : UserService에 추가

+ 정규표현식 사용

참조한 글: https://zzang9ha.tistory.com/322


- JWT 토큰 값이 올바르게 생성되지 않습니다 : 쿠키 헤더가 받은 Bearer 값이 올바르지 않은 쿠키라고 하며 JWT 토큰이 생성되지 않음(로그인을 제대로 하지 않고 글 작성한 경우 발생하는 오류였다) -> PostService의 createPost 메서드에서 토큰을 확인한 후 토큰에 저장된 사용자 정보를 조회 및 요청 DTO의 내용을 객체에 담아 Flush 하는 코드를 잊고 작성하지 않음 : 작성 후 테스트 : 로그인까진 잘 되나, 글 작성 POST 메서드를 날리면 오류는 없지만 DB에 저장이 되지 않고, null 출력 + 디버깅 : token 값이 null이다. -> Postman에서 JWT를 저장해야하는데 이를 놓침 -> JWT 만드는 방법을 공부하다 Postman에서 출력되는 것 확인 후 Postman에 입력 : 해결


22.12.19 (1)

수정/삭제 메서드 작성 중 동일한 에러 발생

"message": "Type definition error: [simple type, class jakarta.servlet.http.HttpServletRequest]"

원인을 도저히 알 수 없음 : 도움 요청

해답 : @ResponseBody 이놈.. DTO 앞에다 붙이는 것이다. 아직 개념이 완전히 자리잡지 못한 결과다.

해결!

타입간 반환에 대한 공부를 더 하고 있다. -> 수정 및 삭제후 결과를 PostResponseDto 타입으로 객체를 만들어 보내기 위한 작업 공부


22.12.19 (2)

숙련 Lv2 과제 수행

1. 특수문자까지 필수로 요구하는 비밀번호 작성기능을 정규표현식을 사용하여 구현

~ (?= ) : 긍정형 전방탐색을 활용

참조 : https://okky.kr/articles/298826

2. 회원 권한 부여, 인증/인가 및 권한별 게시글 접근 권한 설정

3. 댓글 관련 기능 추가하기 :

1) 우선 댓글 관련 프로세스를 게시물 작성과 동일하게 처리하는 게 맞겠다는 판단 하에, Comment, CommentController, CommentService, CommentRepository, CommentRequestDto, CommentResponseDto 작성 및 구현

2) 댓글과 게시물 연관관계 설정 -> 게시물 : 댓글 : 사용자 = 1: N : 1

연관관계 설정에 관한 경험 부족 : 포스트 엔티티에서 코멘트 리스트에 대한 필드를 생성하고 관계설정을 해두고 어플리케이션을 실행시켜보려고 했지만, 빈 생성 에러 발생 -> 엔티티에 Timestamped 상속 표시를 안했기 때문..! (+ 왜 상속해야만 하지?)

3) 코멘트리스트에 댓글 추가하기 : 댓글을 추가하였지만 연관관계 설정이 미비하여 리스트에 추가되지 않음

+@Builder 어노테이션을 활용, 엔티티에서 객체를 생성해서 Service에서 메서드를 호출 후 댓글과 게시물을 연결하려는 시도 중..

4) 새로운 시도 : 교재 코드를 활용한 관계설정 후 List 생성, 서비스에서 객체를 생성하고 반복문을 사용하여 List에 객체를 담아 리스트로 반환 후 조회하기 시도

-> 생각하다 보니 Post - User - Comment - Post : n : 1 : n :1? 의 복잡한 관계를 생각하다.. 이게 뭐지..

다시 돌아와서 Post - Commnet의 관계만 1:n으로 설정하고 코드 시도

댓글을 작성할 때 그 게시물 안에 Comment 타입의 어레이리스트를 생성하고 CommentRequestDto의 값을 참조하는 Comment 타입의 객체를 생성, 그 객체를 리스트에 담아 댓글을 작성하는 방식이라는 매커니즘은 이해했지만, 현재 Dto에 적절한 값을 넣지 못한 부분에 도달했다.

5) 순환참조 에러:
   ﻿https://k3068.tistory.com/32

Jackson의 동작원리를 보고 관련 어노테이션인 @JsonBackReference, @JsonManagedReference를 사용하여 문제를 해결했다! + 다만, 아직..

(1) 댓글 추가 후 응답란에 댓글의 id가 null로 표시되는 것(DB에는 정상 저장 및 조회할 때도 정상 출력)

(2) ResponseDto타입의 객체를 만들어 거기에 완성된 값을 담고 반환한 후 그 객체를 참조하여 순환참조 에러를 원천 차단하는 방식에 대한 이해가 아직 부족함 ~ 이렇게 처리되는 게 맞는지 아닌지도 아직 확실하지 않음

22.12.28

1) 댓글 생성 메서드의 퀄리티 높이기

@OneToMany(cascade = CascadeType.All) List<Comment> commentList = new ArrayList<>();에서 만들어지는 Post-CommentList 테이블로 인해 마치 다대다 관계에서 처리하는 것같이 값을 저장하는 부분 개선 노력

https://wordbe.tistory.com/entry/Spring-Data-Jpa-JPA

cascade : 연관관계가 있는 엔티티의 변화를 반영한다

fetch : 객체를 가져온다

https://www.youtube.com/watch?v=brE0tYOV9jQ

https://www.youtube.com/watch?v=hsSc5epPXDs

2) 더티체킹에 대한 경험치 +1 :

내가 코멘트 객체를 만들고 ResponseDto타입 객체로 반환하기 전에 DB에 저장을 하지 않았지만 DB에 저장이 된 이유:

Post타입의 객체를 만들 때 JPA가 내가 지시하지 않았어도 1차 캐시 공간에 미리 만들어진 Post타입의 객체가 있는지 확인한다. 이후 없을 때 새로 만들어서 1차 캐시 공간에 저장한다.

그 후 @Transactional 주기가 끝날 때, 나는 그 전에 Comment 타입의 객체를 만들고 그 곳에 RequestDto타입에서 받은 값을 할당한 후 그 객체를 Post 클래스에서 참조한 commentList에 추가하고 주기를 끝낸다.

그렇게 되면 JPA는 더티체킹을 하며, post.commentList에 변화가 있다는 것을 알게 되고, 내가 별도로 명령하지 않아도 DB에 변경된 값인, comment가 추가된 commentList를 저장하는 것이다.


연관관계 설정 :

회원가입 하며 만들어 둔 JWT 토큰을 참조하여 게시물 작성 시 해당 토큰을 불러오고 검증을 통해 그 회원이 맞는 지 인증 후 게시물 작성 기능이 수행될 수 있도록 인가

수정/ 삭제 기능도 동일하게 적용, 다만 저번 과제 때 boolean으로 접근해 만들어둔 비밀번호 확인 기능이 더 이상 필요가 없어 수정/ 삭제 메서드 및 PostController에 있는 관련 메서드의 타입을 void로 변경 + 12.19일 트러블 슈팅과 연관되는 내용

4. 체크포인트
   처음 설계한 API 명세서에 변경사항이 있었나요? 변경 되었다면 어떤 점 때문 일까요? 첫 설계의 중요성에 대해 작성해 주세요!

- 프로젝트를 수행하면서 새롭게 배우거나 기존에 알았던 내용이 오류가 있던 경우에 변경을 통해 개선했다. 주로 반환하는 값의 타입에 관한 수정이었고, 아직 배움의 뿌리가 자리잡지 못해 생기는 이슈라고 생각한다. 그럼에도 초기 설계를 통해 무에서 유를 창조하는 단계의 접근이 가능하고, 초기 설계를 바탕으로 사고의 확장이 이루어지기 때문에 초기 설계를 우선 완성하는 것이 정말 중요하다 생각했다.

ERD를 먼저 설계한 후 Entity를 개발했을 때 어떤 점이 도움이 되셨나요?

- 이번엔 ERD를 먼저 설계하지 못했다.

JWT를 사용하여 인증/인가를 구현 했을 때의 장점은 무엇일까요?

- 토큰이 매번 암호화 알고리즘을 통해 생성되므로 데이터의 보안을 강화할 수 있고, 토큰이 유지되는 한 Http 프로토콜 안에서 사용자 정보가 보안유지한 상태로 남아있기 때문에 지속성있게 작업할 수 있다고 느꼈다.

반대로 JWT를 사용한 인증/인가의 한계점은 무엇일까요?

- JWT를 활용해 보안을 강화할 수 있지만, 그만큼 구현해야 하는 코드가 증가하는 것이 가장 큰 단점을 다가왔다.

만약 댓글 기능이 있는 블로그에서 댓글이 달려있는 게시글을 삭제하려고 한다면 무슨 문제가 발생할까요? Database 테이블 관점에서 해결방법이 무엇일까요?

-

5번과 같은 문제가 발생했을 때 JPA에서는 어떻게 해결할 수 있을까요?

- 

IoC / DI 에 대해 간략하게 설명해 주세요!

-- Ioc : 제어의 역전, 사용자가 어떤 클래스의 인스턴스를 사용하기 위해 객체를 생성한 후 인스턴스를 사용하는 것이 아니라, 그 반대로 그 클래스에서 객체를 만들어두고 그 객체를 사용자가 호출하여 사용하는 형태

~ 장점 : 기존의 방식에 비해 수정해야 하는 코드의 양이 감소한다.



- DI : 의존성 주입, Ioc가 원활이 진행될 수 있도록 사용자가 서버에서 만들어진 객체에 의존하여 인스턴스를 사용할 수 있도록 의존성을 "주입"하는 것이다.


======================================================================================================================================================
1. Usecase 작성
![img](https://user-images.githubusercontent.com/110814973/206435978-5dbd3c82-e741-4b25-831f-014eea9f7ce4.png)


2. API 설계 CRUD
![image](https://user-images.githubusercontent.com/110814973/206610813-d2184c5f-d331-4ca7-a69d-e9046a30cd3d.png)


3. 프로젝트 시작

** 엔티티는 DB의 테이블( 객체 지향적 언어의 속성을 살려 자바 관점에서 본 객체 )과 속성이 같아야 한다.

22.12.08 트러블 슈팅

Post 생성자를 만들 때 Dto 인자를 주입하며 @RequestBody를 사용하지 않음

(@ResponseBody와 혼동: @RestController) -> 값이 할당되지 않음 : 해결

+@RequestBody로 받을 때는 반드시 받는 객체가 기본 생성자를 가지고 있어야 한다( 인자의 필드가 하나인 경우에, 인자를 가진 생성자 메서드만 있으면 안된다, 둘 다 있는 건 가능 + 하나만 있는 경우는 Setter가 있어도 안된다 )



&& 메모장 프로젝트 실습보다 추가된 점 :

- 프론트엔드에서 받아오는 정보가 추가되었다. -> dto 객체의 속성값이 증가했다.

- 키 값을 가지고 게시물을 선택 조회 및 수정 / 삭제하는 기능



22.12.08 트러블 슈팅

1. 게시물 선택 조회 기능을 만들때 지식과 경험 부족으로 인한 컨트롤러, 서비스 구현이 막힘

시도 1) List<Post> 타입으로 반환하는 @GetMapping findById 메서드 작성 : postRepository에서 타입의 충돌이 발생 -> +Optional<Post>을 쓰라고 함??

2) JPA repository 클래스를 읽다가.. findById()(처음 시도한 조회메서드)가 없는 것을 발견?

(나뭇잎달려있는건 어디서 오는걸까) -> getById(Long id)로 바꾸자 반환해야 하는 타입이 Post 엔티티로 바뀜 ->

관련 컨트롤러 및 서비스에 반영

+ 테스트 : url에 /api/posts/1 입력 -> HTTP 500 에러

3) Crud Repository에 findById() 메서드를 찾았으나, Optional<T> 타입이다 ~ 쓸 줄 모르는 타입.

** 선택 조회 메서드 구현 해결 + Optional<Post>에 대한 공부를 더 해야한다.


2. 비밀번호 확인 기능 구현 : 고민과 구글링을 통해 비밀번호만 인스턴스로 갖고 있는 비밀번호 확인용 Dto를 만들어 Dto로 들어온 비밀번호

와 레포지토리의 id값으로 조회한 이미 저장된 게시물의 비밀번호를 비교하는 메서드를 서비스에 구현

다음 이슈는 이 거를 어떻게 호출할 지 -> 비교 메서드를 boolean으로 두고 서비스의 업데이트에서 true....

-> 한참 고민하다가 비밀번호용 Dto가 필요없다는 것을 깨닫고(어차피 Dto vs 레포지토리이므로) RequestDto로 변경 후 재시도 : 해결, 삭제 

기능도 동일하게 해결할 수 있을 것으로 기대



4. API 명세서 작성 및 질문에 대한 고민
![1](https://user-images.githubusercontent.com/110814973/206437010-ca0a73ec-71c4-4748-8932-f1128a913d10.png)
![2](https://user-images.githubusercontent.com/110814973/206437020-e8779df6-77ef-4c3b-9f16-3ee2b47a7199.png)

- 수정, 삭제 API의 request를 어떤 방식으로 사용하셨나요? (param, query, body)

+ param : 주소에 포함된 변수를 담는다/ query : 주소 바깥 ? 이후의 변수를 담는다/ body : Json, XML 등 데이터를 받는다

Json 데이터를 받기 위해 body 방식을 사용했다.



- 어떤 상황에 어떤 방식의 request를 써야하나요?

C : POST 

R : GET

U : PUT

D : DELETE



- RESTful한 API를 설계했나요? 어떤 부분이 그런가요? 어떤 부분이 그렇지 않나요?

HTTP 통신 프로토콜에 따라 데이터 작업이 원활하게 이루어졌으므로 그렇다고 생각한다.



- 적절한 관심사 분리를 적용하였나요? (Controller, Repository, Service)

Dto클래스에는 Json 형식을 받은 사용자의 요청을 속성화하여 객체로 다른 클래스에서 사용가능하도록 설계


Controller에는 body 방식으로 받은 Json 데이터와 엔티티에서 자동 부여된 id 값을 매개변수로 받고 각각의 메서드와 url을 구분하여 작성
하였으며, 각 기능별로 해당하는 서비스로 id와 객체에 할당한 Json 값을 전달
서비스와의 연결을 위한 불변 객체 선언이 돼있다.


Service에는 각 기능별 메서드가 작성되어 있으며, 레포지토리와의 연결을 위한 불변 객체 선언이 돼있다.


Repository는 JPARepository를 상속받고 추후 발생가능한 다중 상속을 위해 인터페이스로 구현하였다.
선택 게시물 조회 같은 경우 id 값을 매개변수로 한 findById() 메서드를 사용했고, 타입을 Optional<Post>로 사용했지만,
아직 Optional 타입을 정확하게 이해하지 못했다.


엔티티 Post에는 Dto에서 받은 Json 데이터가 각 메서드에 맞게 초기화를 거쳐 데이터베이스와 일치하는 인스턴스가 되어 해당 기능을 수행한다.
