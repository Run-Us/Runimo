import http from 'k6/http';
import { UserToken, RequestResult, QueryParams } from './types';
import { config } from '../config/environment';
import { refreshAccessToken } from './auth';
import { buildQueryString } from './utils';

export class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = config.baseUrl) {
    this.baseUrl = baseUrl;
  }

  get(endpoint: string, token: UserToken, params?: QueryParams): RequestResult {
    const queryString = params ? buildQueryString(params) : '';
    const url = `${this.baseUrl}${endpoint}${queryString}`;
    
    let response = http.get(url, {
      headers: { Authorization: `Bearer ${token.accessToken}` },
      tags: { endpoint, method: 'GET' }
    });

    // 토큰 만료 시 자동 갱신
    if (response.status === 401) {
      const newToken = refreshAccessToken(token.refreshToken);
      if (newToken) {
        response = http.get(url, {
          headers: { Authorization: `Bearer ${newToken}` },
          tags: { endpoint, method: 'GET', retry: 'true' }
        });
        return { 
          response, 
          success: response.status === 200, 
          newToken 
        };
      }
    }

    return { 
      response, 
      success: response.status === 200 
    };
  }

  patch(endpoint: string, data: any, token: UserToken): RequestResult {
    const url = `${this.baseUrl}${endpoint}`;
    
    let response = http.patch(url, JSON.stringify(data), {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token.accessToken}`
      },
      tags: { endpoint, method: 'PATCH' }
    });

    if (response.status === 401) {
      const newToken = refreshAccessToken(token.refreshToken);
      if (newToken) {
        response = http.patch(url, JSON.stringify(data), {
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${newToken}`
          },
          tags: { endpoint, method: 'PATCH', retry: 'true' }
        });
        return { 
          response, 
          success: response.status === 200 || response.status === 409, 
          newToken 
        };
      }
    }

    return { 
      response, 
      success: response.status === 200 || response.status === 409 
    };
  }
}
