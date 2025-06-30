import { sleep, check } from 'k6';
import { UserToken, QueryParams } from '../lib/types';
import { ApiClient } from '../lib/api-client';
import { randomIntBetween, getRandomDateInPast, formatDate, randomFloatBetween } from '../lib/utils';

export class RecordsBrowsingScenarios {
  private apiClient: ApiClient;

  constructor() {
    this.apiClient = new ApiClient();
  }

  // 기본 레코드 조회 (k6.js 스타일)
  basicRecordsQuery(tokens: UserToken[]): void {
    const vuIndex = (__VU - 1) % tokens.length;
    const user = tokens[vuIndex];
    let accessToken = user.accessToken;

    // 랜덤 파라미터 생성
    const params: QueryParams = {
      page: randomIntBetween(0, 5),
      size: randomIntBetween(1, 20),
    };

    let startDate = getRandomDateInPast(30);
    let endDate = getRandomDateInPast(30);

    // 시작일이 종료일보다 나중이면 교체
    if (startDate > endDate) {
      [startDate, endDate] = [endDate, startDate];
    }

    params.startDate = formatDate(startDate);
    params.endDate = formatDate(endDate);

    const result = this.apiClient.get('/records/me', user, params);

    // 토큰이 업데이트된 경우 반영
    if (result.newToken) {
      tokens[vuIndex].accessToken = result.newToken;
    }

    check(result.response, {
      'status is 200': (r) => r.status === 200,
    });

    if (!result.success) {
      console.error(`사용자 ${user.user_id} 요청 실패: ${result.response.status}`);
    }

    // 랜덤 지연 추가 (실제 사용자 행동 시뮬레이션)
    sleep(randomFloatBetween(0.1, 2));
  }

  // 간단한 레코드 조회 (k6-v2.js 스타일)
  simpleRecordsQuery(tokens: UserToken[]): void {
    const vuIndex = (__VU - 1) % tokens.length;
    const user = tokens[vuIndex];

    const params: QueryParams = {
      page: randomIntBetween(0, 5),
      size: randomIntBetween(1, 20),
    };

    let startDate = getRandomDateInPast(30);
    let endDate = getRandomDateInPast(30);

    if (startDate > endDate) {
      [startDate, endDate] = [endDate, startDate];
    }

    params.startDate = formatDate(startDate);
    params.endDate = formatDate(endDate);

    const result = this.apiClient.get('/records/me', user, params);

    if (result.newToken) {
      tokens[vuIndex].accessToken = result.newToken;
    }

    sleep(1);

    check(result.response, {
      'status is 200': (r) => r.status === 200,
    });
  }

  // 페이징 브라우징 (여러 페이지 조회)
  pagingBrowsing(tokens: UserToken[], pageCount: number = 3): void {
    const vuIndex = (__VU - 1) % tokens.length;
    const user = tokens[vuIndex];

    for (let i = 0; i < pageCount; i++) {
      const params: QueryParams = {
        page: i,
        size: randomIntBetween(5, 20),
      };

      let startDate = getRandomDateInPast(60);
      let endDate = getRandomDateInPast(30);

      if (startDate > endDate) {
        [startDate, endDate] = [endDate, startDate];
      }

      params.startDate = formatDate(startDate);
      params.endDate = formatDate(endDate);

      const result = this.apiClient.get('/records/me', user, params);

      if (result.newToken) {
        tokens[vuIndex].accessToken = result.newToken;
      }

      check(result.response, {
        'request success': (r) => r.status === 200,
      });

      sleep(randomFloatBetween(1, 3)); // 사용자가 데이터를 읽는 시간
    }
  }
}
