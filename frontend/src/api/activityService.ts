import api from './api'
import type {
    ActivityRequest,
    ActivityResponse
} from '@/types/dto'
import type { ActivityTypes } from '@/types/enum/types'
import type { PageResponse } from '@/types/page/page'

export const ActivityService = {
    createActivity(data: ActivityRequest) {
        return api.post<ActivityResponse>('/activity', data)
    },
    getActivities(page: number=0, size: number=10, type?: ActivityTypes, exactDate?: localDate) {
        const params = new URLSearchParams()
        params.append('page', page.toString())
        params.append('size', size.toString())
        if (type !== undefined) {
            params.append('type', type.toString())
        }
        if(exactDate !== undefined) {
            params.append('exactDate', exactDate.toString())
        }
        return api.get<PageResponse<ActivityResponse>>(`/activity?${params.toString()}`)
    },
    getActivity(id: number) {
        return api.get<ActivityResponse>(`/activity/${id}`)
    },
    updateActivity(id:number, data: ActivityRequest) {
        return api.put<ActivityResponse>(`/activity/${id}`, data)
    },
    deleteActivity(id: number) {
        return api.delete<void>(`/activity/${id}`)
    }
}