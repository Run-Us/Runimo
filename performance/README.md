## 🚀 설치 및 실행

### 1. 의존성 설치
```bash
cd performance
npm install
```

### 2. 환경 설정
```bash
cp .env.example .env
# .env 파일을 편집하여 실제 API URL 및 설정값 입력
```

### 3. 빌드
```bash
npm run build
```

### 4. 테스트 실행

#### 개별 테스트 실행
```bash
# 기본 부하 테스트
npm run test:basic-load

# 간단한 반복 테스트
npm run test:simple

# 실제 사용자 시뮬레이션 (22분 소요)
npm run test:real-user

# 다중 시나리오 테스트
npm run test:multi
```

#### 환경변수와 함께 실행
```bash
API_BASE_URL=https://your-api.com/v1 MAX_USERS=100 k6 run dist/basic-load.js
```

#### 모든 테스트 실행
```bash
npm run test:all
```

### 개발 모드 (파일 변경 감지)
```bash
npm run build:watch
```

### 새로운 테스트 추가
1. `src/tests/` 디렉토리에 새 TypeScript 파일 생성
2. `webpack.config.js`의 entry에 새 파일 추가
3. `package.json`의 scripts에 실행 명령 추가

### 새로운 시나리오 추가
1. `src/scenarios/` 디렉토리에 새 시나리오 클래스 생성
2. 기존 테스트에서 import하여 사용
