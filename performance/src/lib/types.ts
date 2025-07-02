export interface UserToken {
  user_id: number;
  accessToken: string;
  refreshToken: string;
}

export interface TestSetupData {
  tokens: UserToken[];
}

export interface QueryParams {
  page?: number;
  size?: number;
  startDate?: string;
  endDate?: string;
}

export interface ApiResponse {
  status: number;
  body: any;
  json: (path?: string) => any;
}

export interface RequestResult {
  response: ApiResponse;
  success: boolean;
  newToken?: string;
}
