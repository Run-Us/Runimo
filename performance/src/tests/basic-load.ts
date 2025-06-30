import { Options } from 'k6/options';
import { group } from 'k6';
import { config, thresholds } from '../config/environment';
import { setupUsers } from '../lib/auth';
import { generateUserIds } from '../lib/utils';
import { RecordsBrowsingScenarios } from '../scenarios/records-browsing';
import { TestSetupData } from '../lib/types';

export const options: Options = {
  scenarios: {
    shared_iter_scenario: {
      executor: 'shared-iterations',
      vus: 50,
      iterations: 1000,
      startTime: '0s',
    },
    per_vu_scenario: {
      executor: 'per-vu-iterations',
      vus: 10,
      iterations: 10,
      startTime: '10s',
    },
  },
  thresholds,
};

export function setup(): TestSetupData {
  const userIds = generateUserIds(config.maxUsers);
  const tokens = setupUsers(userIds);
  return { tokens };
}

export default function(data: TestSetupData) {
  const recordsScenarios = new RecordsBrowsingScenarios();

  group('get records', function () {
    recordsScenarios.basicRecordsQuery(data.tokens);
  });
}

export function handleSummary(data: any) {
  console.log('테스트 완료, 결과 생성 중...');

  const textSummary = `
========== API 성능 측정 결과 ==========

총 요청 수: ${data.metrics.http_reqs?.values.count || 0}
총 실패 수: ${data.metrics.http_req_failed?.values.count || 0}
평균 응답 시간: ${data.metrics.http_req_duration?.values.avg?.toFixed(2) || 0} ms
최대 응답 시간: ${data.metrics.http_req_duration?.values.max?.toFixed(2) || 0} ms
p95 응답 시간: ${data.metrics.http_req_duration?.values['p(95)']?.toFixed(2) || 0} ms

==================================
  `;

  return {
    stdout: textSummary,
    'summary.json': JSON.stringify(data, null, 2),
    'summary.txt': textSummary,
  };
}
