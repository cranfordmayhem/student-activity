<script setup lang="ts">
    import { useAuthStore } from '@/stores/auth'
    import { ref, watch, onMounted } from 'vue'
    import { ActivityService } from '@/api/services'
    import type { ActivityResponse } from '@/types/dto'
    import { useRouter } from 'vue-router'
    import { format, parseISO } from 'date-fns'
    import Navbar from '@/components/layout/Navbar.vue'

    const router = useRouter()
    const activityTypes: ActivityTypes[] = ["DISCUSSIONS", "QUIZZES", "PROJECTS", "DEBATES", "LABS"]

    const type = ref<ActivityTypes | null>(null)

    const filterDate = ref('')
    const activities = ref<ActivityResponse[]>([])
    const page = ref(0)
    const size = ref(10)
    const totalPages = ref(0)

    const loadActivities = async () => {
        try {
            const { data } = await ActivityService.getActivities(page.value, size.value)
            activities.value = data.content
            totalPages.value = data.totalPages
        } catch (err) {
            console.error('Failed to load Activities: ', err)
        }
    }

    const prevPage = () => {
        if (page.value > 0) {
            page.value--
            loadActivities()
        }
    }

    const nextPage = () => {
        if (page.value + 1 < totalPages.value) {
            page.value++
            loadActivities()
        }
    }

    const goToAddActivity = () => {
        router.push('/activity/add')
    }

    const editActivity = (id: number) => {
        router.push(`/activity/${id}`)
    }

    const deleteActivity = async (id: number) => {
        try {
            await ActivityService.deleteActivity(id)
            alert('Activity deleted successfully')
            await loadActivities()
        } catch (err) {
            console.error('Failed to delete activity: ', err)
        }
    }

    const filterActivities = async () => {
        const { data } = await ActivityService.getActivities(
            page.value, size.value, type.value, filterDate.value
        )

        activities.value = data.content
        totalPages.value = data.totalPages
    }

    watch(type, () => filterActivities())
    watch(filterDate, () => filterActivities())

    const filterThis = async(event: Event) => {
        const value = (event.target as HTMLInputElement).value

        console.log(value)
    }

    onMounted(loadActivities)
</script>
<template>
    <Navbar />
    <div class="p-6">
        <div class="flex justify-between gap-4 mt-4">
            <button @click="goToAddActivity" class="mb-4 px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">
            Add New Activity
            </button>

            <div class="flex items-center mb-4 gap-2">
                <label class="font-semibold">Activity Type:</label>
                <select
                    v-model="type"
                    class="border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500"
                >
                    <option value="">All</option>
                    <option 
                        v-for="activityType in activityTypes"
                        :key="activityType"
                        :value="activityType"
                    >
                        {{ activityType }}
                    </option>
                </select>
                <label class="mb-1 font-semibold">Date:</label>
                <input 
                    type="date" 
                    v-model="filterDate"
                    class="border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500"
                />
            </div>


        </div>
        <div class="overflow-x-auto">
            <table class="min-w-full border">
                <thead>
                    <tr class="bg-gray-100">
                        <th class="px-4 py-2">Activity Type</th>
                        <th class="px-4 py-2">Description</th>
                        <th class="px-4 py-2">Timestamp</th>
                        <th class="px-4 py-2">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="activity in activities" :key="activity.id" class="text-center">
                        <td class="px-4 py-2 border">{{ activity.type }}</td>
                        <td class="px-4 py-2 border">{{ activity.description }}</td>
                        <td class="px-4 py-2 border">{{ format(parseISO(activity.timestamp), 'EEE - MMM dd, yyy hh:mm a') }}</td>
                        <td class="px-4 py-2 border flex justify-center gap-2">
                            <button @click="editActivity(activity.id)" class="px-4 py-1 bg-blue-600 text-white rounded hover:bg-blue-700">
                                Edit
                            </button>
                            <button @click="deleteActivity(activity.id)" class="px-4 py-1 bg-red-600 text-white rounded hover:bg-red-700">
                                Delete
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="flex justify-between mt-4 items-center">
            <button @click="prevPage" :disabled="page === 0" class="px-4 py-2 border rounded hover:bg-gray-100 disabled:opacity-50">
                Prev
            </button>
            <span>Page {{ page + 1 }} of {{ totalPages }}</span>
            <button @click="nextPage" :disabled="page + 1 >= totalPages" class="px-4 py-2 border rounded hover:bg-gray-100 disabled:opacity-50">
                Next
            </button>
        </div>
    </div>
</template>