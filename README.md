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

### 3-2. queryDsl 로컬 캐시 적용 테스트

### 테스트 데이터 약 100만건 - schedule
<img width="1362" alt="Image" src="https://github.com/user-attachments/assets/c1b36dff-ea75-47e0-b4f5-36ca6ff152a3" />

### 테스트 데이터 약 10만건 - theater
<img width="1365" alt="Image" src="https://github.com/user-attachments/assets/60a37ebe-3155-41ff-a178-7d8deda51a5d" />

### 테스트 데이터 약 10만건 - movie
<img width="1362" alt="Image" src="https://github.com/user-attachments/assets/634f5fa0-9fc3-4c9b-bc9a-5943fbd2004f" />

- 로컬 캐시 테스트 결과 - schedule 20만건
```
     ✓ is status 200

     checks.........................: 100.00% 300 out of 300
     data_received..................: 51 kB   169 B/s
     data_sent......................: 43 kB   143 B/s
     http_req_blocked...............: avg=732.09µs min=345µs   med=670.5µs  max=2.64ms   p(90)=984.3µs  p(95)=1.25ms  
     http_req_connecting............: avg=535.77µs min=243µs   med=497.5µs  max=2.41ms   p(90)=716.09µs p(95)=777.35µs
   ✓ http_req_duration..............: avg=105.56ms min=83.13ms med=100.35ms max=579.07ms p(90)=107.88ms p(95)=119.31ms
       { expected_response:true }...: avg=105.56ms min=83.13ms med=100.35ms max=579.07ms p(90)=107.88ms p(95)=119.31ms
   ✓ http_req_failed................: 0.00%   0 out of 300
     http_req_receiving.............: avg=449.03µs min=89µs    med=369.5µs  max=3.09ms   p(90)=623.1µs  p(95)=1.01ms  
     http_req_sending...............: avg=121.47µs min=39µs    med=104µs    max=754µs    p(90)=181.4µs  p(95)=248.14µs
     http_req_tls_handshaking.......: avg=0s       min=0s      med=0s       max=0s       p(90)=0s       p(95)=0s      
     http_req_waiting...............: avg=104.99ms min=82.69ms med=99.79ms  max=577.94ms p(90)=107.22ms p(95)=118.86ms
     http_reqs......................: 300     0.999665/s
     iteration_duration.............: avg=1.1s     min=1.08s   med=1.1s     max=1.58s    p(90)=1.11s    p(95)=1.12s   
     iterations.....................: 300     0.999665/s
     vus............................: 1       min=1          max=1   
     vus_max........................: 1000    min=1000       max=1000

```
<img width="1030" alt="Image" src="https://github.com/user-attachments/assets/78e92ef8-21a9-4b4a-84b8-57a66d46a2ec" />

- 로컬 캐시 테스트 결과 - schedule 100만건
```
     ✓ is status 200

     checks.........................: 100.00% 301 out of 301
     data_received..................: 51 kB   169 B/s
     data_sent......................: 43 kB   143 B/s
     http_req_blocked...............: avg=830.43µs min=337µs    med=877µs    max=6.27ms   p(90)=993µs    p(95)=1.02ms  
     http_req_connecting............: avg=614.76µs min=242µs    med=667µs    max=5.41ms   p(90)=737µs    p(95)=759µs   
   ✗ http_req_duration..............: avg=332.01ms min=303.95ms med=327.38ms max=811.95ms p(90)=342.37ms p(95)=355.74ms
       { expected_response:true }...: avg=332.01ms min=303.95ms med=327.38ms max=811.95ms p(90)=342.37ms p(95)=355.74ms
   ✓ http_req_failed................: 0.00%   0 out of 301
     http_req_receiving.............: avg=453.45µs min=107µs    med=397µs    max=2.5ms    p(90)=655µs    p(95)=890µs   
     http_req_sending...............: avg=189.06µs min=40µs     med=115µs    max=16.32ms  p(90)=203µs    p(95)=273µs   
     http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s       p(90)=0s       p(95)=0s      
     http_req_waiting...............: avg=331.37ms min=303.43ms med=326.71ms max=810.76ms p(90)=341.87ms p(95)=355.18ms
     http_reqs......................: 301     0.998969/s
     iteration_duration.............: avg=1.33s    min=1.3s     med=1.32s    max=1.81s    p(90)=1.34s    p(95)=1.35s   
     iterations.....................: 301     0.998969/s
     vus............................: 1       min=1          max=1   
     vus_max........................: 1000    min=1000       max=1000
```
<img width="1034" alt="Image" src="https://github.com/user-attachments/assets/fda7b748-5a17-47ae-9fc4-ba5e2c35596f" />

### 4. 실행
* 도커 실행 명령어
> docker compose -f ./docker/docker-compose.yml up -d
* 도커 중지 명령어
> docker compose -f ./docker/docker-compose.yml down
