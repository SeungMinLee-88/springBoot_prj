# - 프로젝트 개요
예전 PHP 프레임워크인 Laravel을 통해 개발한 회의실 예약 프로젝트의 일부 기능을 프론트 영역은 react와 react 프레임워크인 Next.js, 백엔드 영역은 Spring Boot, Spring Data JPA, Spring Security 등을 통해 구현해 보았으며 해당 문서에서는 백엔드 부분에 대해 중점적으로 다루었다.
<br /><br />
# - 개발기간
- 25.04 ~ 25.05(약 1.5개월)\
  <br /><br />
# - 개발환경
- JAVA v1.8
- Spring Boot, Spring Data Jpa v3.4.3
- Spring Security v3.4.3
- Jsonwebtoken v0.12.3
- lombok 등 라이브러리 및 Mysql DB
  <br /><br />
# - 주요기능
- 사용자인증
  Spring Security 통한 사용자 인증 및 권한 제어, JWT 인증 토큰 및 refresh 토큰 발급, 재발급 기능
- 게시판 :\
  게시판 CRUD 기능, Pageable 인터페이스를 통한 페이징 처리, Specification을 통한 검색 기능, 첨부 파일 처리
- 코멘트 :\
  코멘트 CRUD 기능, 코멘트 트리 리스트 구현
- 예약 :\
  예약 CRUD 기능
  <br /><br />
# - 특이사항
- Spring Security를 통한 인증 처리, 권한 제어 JWT 인증 토큰 발급/재발급 기능을 통한 사용자 접근 제어
- Pageble, Specification 인터페이스를 통한 쿼리 리스트 조회 처리
- 코멘트에 자기 참조 관계 설정 및 리스트 트리 구현
- N:N 관계 처리를 위해 중간 테이블 추가 및 데이터 처리

## 1. DB구조
### 1.2 사용자
사용자는 여러개의 Role을 가질 수 있고 Role 역시 여러 사용자에게 할당 될 수 있으므로 사용자와 Role은 N:N 관계이며 이를 표현 하기 위해 중간 테이블인 role_user 테이블을 두어 사용자가 추가 될 시 role_user 테이블에 사용자 아이디와 Role 아이디를 가진 데이터가 추가 되어야 한다.

![Image](https://github.com/user-attachments/assets/3ff89efb-cb24-4edf-8514-f769328fc6f7)

### 1.2 게시판
게시판은 게시글을 작성하는 사용자와 N:1, 게시글에 첨부되는 첨부파일과 1:N, 게시글의 코멘트와 1:N 관계이다. 코멘트의 경우 Reply 기능으로 자기 참조 데이터가 생성되므로 자기자신을 1:N으로 참조 하게 된다.

![Image](https://github.com/user-attachments/assets/f08b4e4f-d24a-4b90-baa9-914944126c7c)

### 1.3 예약
사용자는 여러 예약을 가질 수 있으므로 사용자와 예약은 1:N 관계이며 예약은 여러개의 예약시간을 가질 수 있고 예약시간 역시 여러 예약에 할당 될 수 있으 예약과 예약시간 N:N 관계이며 이를 표현 하기 위해 중간 테이블인 reserve_time 테이블을 두어 예약이 추가 될 시 reserve_time 테이블에 예약 아이디와 예약시간 아이디를 가진 데이터가 추가 되어야 한다.

![Image](https://github.com/user-attachments/assets/283df42e-6218-4ca5-a86a-8a8dfd5828d3)


```
## 5. 결론 및 향후 계획
JavaScript 라이브러리인 react와 react 기반 프레임워크인 nextjs를 통해 예전에 진행했던 예약 플젝트의 일부를 구현 해보았다. 이번 프로젝트는 react와 nextjs를 처음 접해보고 사용기에 Pages Router를 통해 구현 하였으며