import { Options } from 'k6/options';
import { config, thresholds } from '../config/environment';
import { setupUsers } from '../lib/auth';
import { generateUserIds } from '../lib/utils';
import { RecordsBrowsingScenarios } from '../scenarios/records-browsing';
import { TestSetupData } from '../lib/types';

export const options: Options = {
  scenarios: {
    default: {
      executor: 'per-vu-iterations',
      vus: 1,
      iterations: 100,
      maxDuration: config.testDuration,
    }
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
  recordsScenarios.simpleRecordsQuery(data.tokens);
}

export function teardown() {
  console.log(`\n---\n 간단한 반복 테스트 완료 (지속시간: ${config.testDuration})\n---`);
}
