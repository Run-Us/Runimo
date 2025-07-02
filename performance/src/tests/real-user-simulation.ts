import { Options } from 'k6/options';
import { config, thresholds } from '../config/environment';
import { setupUsers } from '../lib/auth';
import { generateUserIds } from '../lib/utils';
import { RealUserPatterns } from '../scenarios/real-user-patterns';
import { TestSetupData } from '../lib/types';

export const options: Options = {
  thresholds: {
    http_req_failed: ['rate<0.05'],
    http_req_duration: ['p(95)<2000', 'p(99)<5000'],
  },

  scenarios: {
    // 아침 출근시간 (7-9시) 시뮬레이션 - 점진적 증가
    morning_rush: {
      executor: 'ramping-arrival-rate',
      startTime: '0s',
      startRate: 5,
      stages: [
        { target: 15, duration: '2m' },
        { target: 30, duration: '3m' },
        { target: 20, duration: '2m' },
      ],
      preAllocatedVUs: 10,
      maxVUs: 25,
      exec: 'browsing_session',
    },

    // 점심시간 (12-1시) 활발한 사용
    lunch_time: {
      executor: 'constant-arrival-rate',
      startTime: '7m',
      rate: 25,
      timeUnit: '1s',
      duration: '4m',
      preAllocatedVUs: 15,
      maxVUs: 20,
      exec: 'active_user_session',
    },

    // 저녁 피크 시간 (6-8시) - 가장 높은 부하
    evening_peak: {
      executor: 'ramping-arrival-rate',
      startTime: '11m',
      startRate: 20,
      stages: [
        { target: 40, duration: '1m' },
        { target: 50, duration: '3m' },
        { target: 35, duration: '2m' },
        { target: 15, duration: '2m' },
      ],
      preAllocatedVUs: 20,
      maxVUs: 35,
      exec: 'heavy_user_session',
    },

    // 야간 시간 - 낮은 부하 유지
    night_time: {
      executor: 'constant-arrival-rate',
      startTime: '19m',
      rate: 8,
      timeUnit: '1s',
      duration: '3m',
      preAllocatedVUs: 5,
      maxVUs: 10,
      exec: 'light_browsing',
    },
  },
};

const userPatterns = new RealUserPatterns();

export function setup(): TestSetupData {
  const userIds = generateUserIds(config.maxUsers);
  const tokens = setupUsers(userIds);
  return { tokens };
}

export function light_browsing(data: TestSetupData) {
  userPatterns.lightBrowsing(data.tokens);
}

export function browsing_session(data: TestSetupData) {
  userPatterns.browsing_session(data.tokens);
}

export function active_user_session(data: TestSetupData) {
  userPatterns.activeUserSession(data.tokens);
}

export function heavy_user_session(data: TestSetupData) {
  userPatterns.heavyUserSession(data.tokens);
}

export function teardown() {
  const totalDuration = 22 * 60;
  console.log(`\n=== 실제 사용자 패턴 테스트 결과 ===`);
  console.log(`테스트 시간: ${totalDuration}초 (${totalDuration/60}분)`);
  console.log(`==========================================\n`);
}
