import { Options } from 'k6/options';
import { config, thresholds } from '../config/environment';
import { setupUsers } from '../lib/auth';
import { generateUserIds } from '../lib/utils';
import { RecordsBrowsingScenarios } from '../scenarios/records-browsing';
import { UserProfileScenarios } from '../scenarios/user-profile';
import { TestSetupData } from '../lib/types';

export const options: Options = {
  thresholds,
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
  },
};

export function setup(): TestSetupData {
  const userIds = generateUserIds(config.maxUsers);
  const tokens = setupUsers(userIds);
  return { tokens };
}

export default function(data: TestSetupData) {
  // 기본 함수는 비워둠 (시나리오별 exec 함수 사용)
}

export function mypage(data: TestSetupData) {
  const userScenarios = new UserProfileScenarios();
  userScenarios.checkMyPage(data.tokens);
}

export function normal(data: TestSetupData) {
  const recordsScenarios = new RecordsBrowsingScenarios();
  recordsScenarios.simpleRecordsQuery(data.tokens);
}

export function teardown() {
  const totalDurationInSeconds = 120; // 2분
  console.log(`\n---\n📊 다중 시나리오 테스트 완료\n---`);
}
