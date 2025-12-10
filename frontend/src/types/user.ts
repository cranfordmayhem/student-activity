export interface UserAccountRequest {
    name: string;
    email: string;
    password: string;
}

export interface UserAccountResponse {
    id: number;
    name: string;
    email: string;
}