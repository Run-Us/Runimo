import { sleep } from 'k6';
import { UserToken } from '../lib/types';
import { RecordsBrowsingScenarios } from './records-browsing';
import { UserProfileScenarios } from './user-profile';
import { randomIntBetween, randomFloatBetween } from '../lib/utils';

export class RealUserPatterns {
  private recordsScenarios: RecordsBrowsingScenarios;
  private userScenarios: UserProfileScenarios;

  constructor() {
    this.recordsScenarios = new RecordsBrowsingScenarios();
    this.userScenarios = new UserProfileScenarios();
  }

  // 가벼운 브라우징 세션 (짧은 세션, 적은 요청)
  lightBrowsing(tokens: UserToken[]): void {
    const sessionStart = Date.now();

    this.userScenarios.checkMyPage(tokens);
    sleep(randomFloatBetween(2, 5));

    this.recordsScenarios.simpleRecordsQuery(tokens);
    sleep(randomFloatBetween(1, 3));

  }

  // 일반적인 브라우징 세션 (중간 길이, 여러 페이지 조회)
  browsing_session(tokens: UserToken[]): void {
    this.userScenarios.checkMyPage(tokens);
    sleep(randomFloatBetween(1, 3));

    const pageCount = randomIntBetween(2, 4);
    this.recordsScenarios.pagingBrowsing(tokens, pageCount);
  }

  // 활발한 사용자 세션 (중간-높은 활동)
  activeUserSession(tokens: UserToken[]): void {
    this.userScenarios.checkMyPage(tokens);
    sleep(randomFloatBetween(1, 2));

    const pageCount = randomIntBetween(3, 6);
    this.recordsScenarios.pagingBrowsing(tokens, pageCount);

    this.userScenarios.updateEgg(tokens, 0.3);
  }

  // 헤비 유저 세션 (긴 세션, 많은 요청)
  heavyUserSession(tokens: UserToken[]): void {
    this.userScenarios.checkMyPage(tokens);
    sleep(randomFloatBetween(0.5, 1.5));

    const pageCount = randomIntBetween(5, 10);
    this.recordsScenarios.pagingBrowsing(tokens, pageCount);

    this.userScenarios.heavyUserEggUpdate(tokens);
    this.userScenarios.checkMyPage(tokens);
  }
}
