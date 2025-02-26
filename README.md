### 1. 멀티모듈 & 아키텍쳐 

* movie-adpater : 외부로 부터 들어오는 요청 & 내부에서 외부로 나가는 응답을 처리하는 모듈  
* movie-application : 서비스의 비즈니스 로직을 처리할 수 있는 모듈
* movie-common : 공통으로 사용하는 코드를 관리하는 모듈

### 2. ERD
<img width="1129" alt="Image" src="https://github.com/user-attachments/assets/61a63baa-ac51-4739-afcd-6f4bf281a89a" />

### 3. K6 부하테스트
### 테스트 전재 조건
- DAU: N=1000명
- 1명당 1일 평균 접속 수: 2번
- 피크 시간대의 집중률: 평소 트래픽의 10배
- Throughput 계산:
    - 1일 총 접속 수 = DAU × 1명당 1일 평균 접속 수 = N × 2 = 2N (1일 총 접속 수)
    - 1일 평균 RPS = 1일 총 접속 수 ÷ 86,400 (초/일)= 2N ÷ 86,400 ≈ X RPS
    - 1일 최대 RPS = 1일 평균 RPS × (최대 트래픽 / 평소 트래픽)= X × 10 = 10X RPS
- VU: N명
- optional
    - thresholds
        - e.g p(95) 의 응답 소요 시간 200ms 이하
        - 실패율 1% 이하

### 테스트 데이터 약 20만건
<img width="1363" alt="Image" src="https://github.com/user-attachments/assets/355a8bec-7ee5-41ed-b513-5bbf2ad2fb7f" />

### 3-1. fetch join vs queryDsl
- fetch join 실행 쿼리
```
      select
        distinct s1_0.schedule_id,
        s1_0.created_at,
        s1_0.created_author,
        m1_0.movie_id,
        m1_0.created_at,
        m1_0.created_author,
        m1_0.movie_genre,
        m1_0.movie_grade,
        m1_0.movie_image_url,
        m1_0.movie_name,
        m1_0.movie_release_at,
        m1_0.running_time_minutes,
        m1_0.updated_at,
        m1_0.updated_author,
        s1_0.movie_start_at,
        s1_0.screen_close_at,
        s1_0.screen_open_at,
        t1_0.theater_id,
        t1_0.created_at,
        t1_0.created_author,
        t1_0.theater_name,
        t1_0.updated_at,
        t1_0.updated_author,
        s1_0.updated_at,
        s1_0.updated_author 
    from
        schedule s1_0 
    join
        movie m1_0 
            on m1_0.movie_id=s1_0.movie_id 
    join
        theater t1_0 
            on t1_0.theater_id=s1_0.theater_id 
    where
        1=1 
        and t1_0.theater_id=1 
        and '2025-02-26' between date(s1_0.screen_open_at) and date(s1_0.screen_close_at) 
    order by
        m1_0.movie_release_at desc,
        s1_0.movie_start_at

```
- fetch join 테스트 결과
```
     ✓ is status 200

     checks.........................: 100.00% 300 out of 300
     data_received..................: 1.7 GB  5.5 MB/s
     data_sent......................: 31 kB   103 B/s
     http_req_blocked...............: avg=510.04µs min=225µs    med=510.49µs max=1.5ms    p(90)=635µs    p(95)=650.05µs
     http_req_connecting............: avg=419.73µs min=156µs    med=420µs    max=614µs    p(90)=532.2µs  p(95)=544.05µs
   ✗ http_req_duration..............: avg=873.97ms min=842.19ms med=863.54ms max=1.34s    p(90)=899.71ms p(95)=929.66ms
       { expected_response:true }...: avg=873.97ms min=842.19ms med=863.54ms max=1.34s    p(90)=899.71ms p(95)=929.66ms
   ✓ http_req_failed................: 0.00%   0 out of 300
     http_req_receiving.............: avg=66.04ms  min=61.66ms  med=64.36ms  max=112.38ms p(90)=69.9ms   p(95)=75.78ms 
     http_req_sending...............: avg=59.29µs  min=30µs     med=54µs     max=309µs    p(90)=78µs     p(95)=88µs    
     http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s       p(90)=0s       p(95)=0s      
     http_req_waiting...............: avg=807.87ms min=776.06ms med=798.37ms max=1.23s    p(90)=832.32ms p(95)=862.95ms
     http_reqs......................: 300     0.997095/s
     iteration_duration.............: avg=1.87s    min=1.84s    med=1.86s    max=2.35s    p(90)=1.9s     p(95)=1.93s   
     iterations.....................: 300     0.997095/s
     vus............................: 1       min=1          max=2   
     vus_max........................: 1000    min=1000       max=1000

```  
<img width="1324" alt="Image" src="https://github.com/user-attachments/assets/53bfc777-9467-4df7-9ee6-fa44369e0034" />

- queryDsl 실행 쿼리
```  
    select
        m1_0.movie_id,
        m1_0.movie_name,
        m1_0.movie_grade,
        m1_0.movie_release_at,
        m1_0.movie_image_url,
        m1_0.running_time_minutes,
        m1_0.movie_genre,
        t1_0.theater_name,
        s1_0.movie_start_at 
    from
        schedule s1_0 
    join
        movie m1_0 
            on s1_0.movie_id=m1_0.movie_id 
    join
        theater t1_0 
            on s1_0.theater_id=t1_0.theater_id 
    where
        t1_0.theater_id=1 
        and '2025-02-26' between date_format(s1_0.screen_open_at, '%Y-%m-%d') and date_format(s1_0.screen_close_at, '%Y-%m-%d') 
    order by
        m1_0.movie_release_at,
        s1_0.movie_start_at
```
- queryDsl 테스트 결과
```
     ✓ is status 200

     checks.........................: 100.00% 300 out of 300
     data_received..................: 1.8 GB  6.1 MB/s
     data_sent......................: 43 kB   143 B/s
     http_req_blocked...............: avg=685.53µs min=254µs    med=648µs    max=2.12ms   p(90)=958.2µs  p(95)=1.01ms  
     http_req_connecting............: avg=515.28µs min=201µs    med=493µs    max=1.96ms   p(90)=692.4µs  p(95)=733.1µs 
   ✗ http_req_duration..............: avg=497.32ms min=469.46ms med=489.84ms max=909.61ms p(90)=509.47ms p(95)=526.82ms
       { expected_response:true }...: avg=497.32ms min=469.46ms med=489.84ms max=909.61ms p(90)=509.47ms p(95)=526.82ms
   ✓ http_req_failed................: 0.00%   0 out of 300
     http_req_receiving.............: avg=79.04ms  min=75.31ms  med=78.03ms  max=134.18ms p(90)=80.4ms   p(95)=86.15ms 
     http_req_sending...............: avg=100.83µs min=33µs     med=96µs     max=595µs    p(90)=146.19µs p(95)=167µs   
     http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s       p(90)=0s       p(95)=0s      
     http_req_waiting...............: avg=418.18ms min=390.59ms med=411.87ms max=831.7ms  p(90)=430.02ms p(95)=442.34ms
     http_reqs......................: 300     0.998406/s
     iteration_duration.............: avg=1.49s    min=1.47s    med=1.49s    max=1.91s    p(90)=1.51s    p(95)=1.52s   
     iterations.....................: 300     0.998406/s
     vus............................: 1       min=1          max=2   
     vus_max........................: 1000    min=1000       max=1000

```
<img width="1343" alt="Image" src="https://github.com/user-attachments/assets/4db03881-6aaf-405d-ad0e-9947f75b6b61" />

### 4. 실행
* 도커 실행 명령어
> docker compose -f ./docker/docker-compose.yml up -d
* 도커 중지 명령어
> docker compose -f ./docker/docker-compose.yml down
