<script setup lang="ts">
    import { ref } from 'vue'
    import { useRouter } from 'vue-router'
    import { ActivityService } from '@/api/services'
    import type { ActivityRequest } from '@/api/types/dto'

    const router = useRouter()
    
    const activityTypes: ActivityTypes[] = ["DISCUSSIONS", "QUIZZES", "PROJECTS", "DEBATES", "LABS"]

    const type = ref<ActivityTypes | null>(null)
    const description = ref('')

    const submitActivity = async () => {
        if (!type.value || !description.value){
            alert('Please fill in all fields')
            return
        }

        const payload: ActivityRequest = {
            type: type.value,
            description: description.value
        }

        try {
            await ActivityService.createActivity(payload)
            alert('Activity added successfully')
            router.push('/')
        } catch (err) {
            console.error('Failed to add activity: ', err)
        }
    }

</script>
<template>
    <div class="max-w-xl mx-auto p-6 bg-white rounded shadow mt-10">
        <h2 class="text-2xl font-bold mb-6">Add New Activity</h2>

        <div class="space-y-4">
            <div>
                <label class="block mb-1 font-semibold">Activity Type: </label>
                <select
                    v-model="type"
                        class="border px-2 py-1 rounded"
                >
                    <option 
                        v-for="type in activityTypes"
                        :key="type"
                        :value="type"
                    >
                        {{ type }}
                    </option>
                </select>
            </div>

            <div>
                <label class="block mb-1 font-semibold">Description</label>
                <input
                    v-model="description" type="text"
                    class="w-full border px-3 py-2 rounded"
                />
            </div>

            <div class="flex justify-end gap-2">
                <button @click="router.push('/')" class="px-4 py-2 bg-gray-300 rounded hover:bg-gray">
                    Cancel
                </button>
                <button @click="submitActivity" class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">
                    Add Activity
                </button>
            </div>
        </div>
    </div>
</template>