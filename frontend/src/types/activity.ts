import type { ActivityTypes } from './enum/types'

export interface ActivityRequest{
    type: ActivityTypes;
    description: string;
}

export interface ActivityResponse{
    id: number;
    type: ActivityTypes;
    description: String;
    timestamp: string;
}