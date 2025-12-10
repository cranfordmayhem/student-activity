export interface LoginRequest {
    username: string;
    password: string;
}

export interface LoginResponse {
    id: number; 
    name: string;
    username: string;
}

export interface RefreshTokenRequest {
    refreshToken: string;
}

export interface TokenResponse {
    accessToken: string;
}