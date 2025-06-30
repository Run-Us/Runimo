import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';
import { Trend } from 'k6/metrics';

const BASE_URL = 'http://localhost:8080/api/v1';
const USERS_ID = new SharedArray('users', function () {
    const arr = [];
    for(let i = 1; i< 51; i++) {
        arr.push(i);
    }
    return arr;
});

let tokens = [];

const requestCount = new Trend('request_count', true);

export const options = {
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<1000'],
    },

    scenarios: {
        normal: {
            executor: 'per-vu-iterations',
            vus: 50, // 50명의 가상 사용자
            iterations: 400, // 각 가상 사용자가 400번의 요청을 수행
            maxDuration: '2m', // 테스트 전체 지속 시간
            exec: 'normal' // 기록 범위 조회 API
            },

        mypage: {
          executor: 'constant-arrival-rate',
          rate: 10,
          timeUnit: '4s', // 4초 당 10회 요청
          duration: '2m', // 2분 지속
          preAllocatedVUs: 4,
          maxVUs: 50, // 최대 50명의 가상 사용자
          exec: 'mypage', // 마이페이지 조회 API
        },
};

export function setup() {
    console.log('테스트 셋업 시작: 사용자 로그인 및 토큰 획득');

    for (const user of USERS_ID) {
        const res = http.post(`${BASE_URL}/auth/test/login`, JSON.stringify({ user_id: user }), {
            headers: { 'Content-Type': 'application/json' },
        });

        const success = check(res, {
            'logged in successfully': (r) => r.status === 200 && r.json('payload.tokens.access_token'),
        });

        if (success) {
            tokens.push({
                user_id: user,
                accessToken: res.json('payload.tokens.access_token'),
                refreshToken: res.json('payload.tokens.refresh_token'),
            });
        } else {
            console.error(`사용자 ${user} 로그인 실패: ${res.status}`);
        }
    }

    console.log(`셋업 완료: ${tokens.length}개의 토큰 획득`);
    return { tokens };
}

export default function (data) {

}



export function mypage (data) {
    const vuIndex = (__VU - 1) % data.tokens.length;
        let { user_id, accessToken } = data.tokens[vuIndex];
    let res = http.get(`${BASE_URL}/users/me`, {
                 headers: { Authorization: `Bearer ${accessToken}` },
             });
             if (res.status === 401) {
                       const newToken = refreshAccessToken(data.tokens[vuIndex].refreshToken);
                       if (newToken) {
                           accessToken = newToken;
                           res = http.get(`${BASE_URL}/records/me${queryString}`, {
                               headers: { Authorization: `Bearer ${newToken}` },
                           });
                           data.tokens[vuIndex].accessToken = newToken;
                           requestCount.add(1);
                       }
                   }
                   check(res, {
                       'status is 200': (r) => r.status === 200,
                   });
}




export function normal(data ) {
  const vuIndex = (__VU - 1) % data.tokens.length;
   let { user_id, accessToken } = data.tokens[vuIndex];
  const page = randomIntBetween(0, 5);
      const size = randomIntBetween(1, 20);
      let startDate = getRandomDateInPast(30);
      let endDate = getRandomDateInPast(30);

      if (startDate > endDate) {
          const tmp = startDate;
          startDate = endDate;
          endDate = tmp;
      }

      const queryString = `?page=${page}&size=${size}&startDate=${formatDate(startDate)}&endDate=${formatDate(endDate)}`;

      let res = http.get(`${BASE_URL}/records/me${queryString}`, {
          headers: { Authorization: `Bearer ${accessToken}` },
      });


      requestCount.add(1);

      if (res.status === 401) {
          const newToken = refreshAccessToken(data.tokens[vuIndex].refreshToken);
          if (newToken) {
              accessToken = newToken;
              res = http.get(`${BASE_URL}/records/me${queryString}`, {
                  headers: { Authorization: `Bearer ${newToken}` },
              });
              data.tokens[vuIndex].accessToken = newToken;
              requestCount.add(1);
          }
      }
      check(res, {
          'status is 200': (r) => r.status === 200,
      });
}

function runStressScenario(user_id, access_token, vuIndex) {

         let res = http.get(`${BASE_URL}/users/me`, {
             headers: { Authorization: `Bearer ${accessToken}` },
         });
         if (res.status === 401) {
                   const newToken = refreshAccessToken(data.tokens[vuIndex].refreshToken);
                   if (newToken) {
                       accessToken = newToken;
                       res = http.get(`${BASE_URL}/records/me${queryString}`, {
                           headers: { Authorization: `Bearer ${newToken}` },
                       });
                       data.tokens[vuIndex].accessToken = newToken;
                       requestCount.add(1);
                   }
               }
               check(res, {
                   'status is 200': (r) => r.status === 200,
               });
}

export function teardown() {
    const totalDurationInSeconds = 10 + 60 + 30;
    const totalRequests = requestCount._sum;

    const throughput = totalRequests / totalDurationInSeconds;
    console.log(`\n---\n Throughput (requests/sec): ${throughput.toFixed(2)}\n---`);
}

// Utils
function randomIntBetween(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function formatDate(date) {
    return date.toISOString().slice(0, 10);
}

function getRandomDateInPast(daysAgo = 30) {
    const date = new Date();
    date.setDate(date.getDate() - randomIntBetween(0, daysAgo));
    return date;
}

function refreshAccessToken(refreshToken) {
    const res = http.post(`${BASE_URL}/auth/refresh`, JSON.stringify({
        refresh_token: refreshToken
    }), {
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${refreshToken}`
        },
    });
    if (res.status === 200 && res.json('payload.access_token')) {
        return res.json('payload.access_token');
    }

    return null;
}
