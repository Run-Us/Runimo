import { sleep, check } from 'k6';
import { UserToken } from '../lib/types';
import { ApiClient } from '../lib/api-client';
import { randomIntBetween, randomFloatBetween } from '../lib/utils';

export class UserProfileScenarios {
  private apiClient: ApiClient;

  constructor() {
    this.apiClient = new ApiClient();
  }

  // 마이페이지 조회
  checkMyPage(tokens: UserToken[]): void {
    const vuIndex = (__VU - 1) % tokens.length;
    const user = tokens[vuIndex];

    const result = this.apiClient.get('/users/me', user);

    if (result.newToken) {
      tokens[vuIndex].accessToken = result.newToken;
    }

    check(result.response, {
      'status is 200': (r) => r.status === 200,
    });
  }

  // 알 업데이트 (k6-15min.js에서 사용)
  updateEgg(tokens: UserToken[], probability: number = 0.3): void {
    if (Math.random() > probability) return;

    const vuIndex = (__VU - 1) % tokens.length;
    const user = tokens[vuIndex];
    const eggId = user.user_id;

    const payload = { 
      love_point_amount: randomIntBetween(1, 3) 
    };

    const result = this.apiClient.patch(`/users/eggs/${eggId}`, payload, user);

    if (result.newToken) {
      tokens[vuIndex].accessToken = result.newToken;
    }

    check(result.response, {
      'egg update success': (r) => r.status === 200 || r.status === 409,
    });

    sleep(randomFloatBetween(0.5, 2));
  }

  // 헤비 유저용 알 업데이트 (더 높은 확률, 더 많은 포인트)
  heavyUserEggUpdate(tokens: UserToken[]): void {
    if (Math.random() > 0.6) return;

    const vuIndex = (__VU - 1) % tokens.length;
    const user = tokens[vuIndex];
    const eggId = user.user_id;

    const payload = { 
      love_point_amount: randomIntBetween(1, 5) 
    };

    const result = this.apiClient.patch(`/users/eggs/${eggId}`, payload, user);

    if (result.newToken) {
      tokens[vuIndex].accessToken = result.newToken;
    }

    check(result.response, {
      'egg update success': (r) => r.status === 200 || r.status === 409,
    });

    sleep(randomFloatBetween(0.5, 1));
  }
}
