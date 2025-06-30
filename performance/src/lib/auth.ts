import http from 'k6/http';
import { check } from 'k6';
import { UserToken } from './types';
import { config } from '../config/environment';

export function setupUsers(userIds: number[]): UserToken[] {
  console.log('테스트 셋업 시작: 사용자 로그인 및 토큰 획득');
  
  const tokens: UserToken[] = [];

  for (const userId of userIds) {
    const response = http.post(
      `${config.baseUrl}/auth/test/login`,
      JSON.stringify({ user_id: userId }),
      { headers: { 'Content-Type': 'application/json' } }
    );

    const success = check(response, {
      'logged in successfully': (r) => r.status === 200 && r.json('payload.tokens.access_token'),
    });

    if (success) {
      tokens.push({
        user_id: userId,
        accessToken: response.json('payload.tokens.access_token'),
        refreshToken: response.json('payload.tokens.refresh_token'),
      });
    } else {
      console.error(`사용자 ${userId} 로그인 실패: ${response.status}`);
    }
  }

  console.log(`셋업 완료: ${tokens.length}개의 토큰 획득`);
  return tokens;
}

export function refreshAccessToken(refreshToken: string): string | null {
  const response = http.post(
    `${config.baseUrl}/auth/refresh`,
    JSON.stringify({ refresh_token: refreshToken }),
    {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${refreshToken}`
      },
      tags: { endpoint: '/auth/refresh', method: 'POST' }
    }
  );

  const success = check(response, {
    'token refresh successful': (r) => r.status === 200 && r.json('payload.tokens.access_token')
  });

  if (success) {
    return response.json('payload.tokens.access_token');
  }

  console.error(`토큰 갱신 실패: ${response.status}`);
  return null;
}
