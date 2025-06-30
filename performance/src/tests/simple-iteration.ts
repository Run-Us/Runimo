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
  const totalDurationInSeconds = 120; // 2분
  console.log(`\n---\n 테스트 완료\n---`);
}
