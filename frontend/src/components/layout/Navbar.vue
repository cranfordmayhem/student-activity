<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = useAuthStore()

// Profile Dropdown
const dropdownOpen = ref(false)
function toggleDropdown() {
  dropdownOpen.value = !dropdownOpen.value
}
function closeDropdown() {
  dropdownOpen.value = false
}


// Computed first letter of name
const userName = computed(() => auth.account?.name || 'User')

function handleLogout() {
  auth.logout()
  router.push('/login')
  closeDropdown()
}

</script>

<template>
  <nav class="bg-white shadow-md px-6 py-4 flex justify-between items-center w-full relative">
    <div class="text-xl font-bold text-gray-800">
      Welcome, {{ userName }}
    </div>

    <!-- Right Icons -->
    <div class="flex items-center gap-4">

        <!-- PROFILE / LOGOUT -->
        <div class="relative">
            <button
            @click="toggleDropdown"
            class="w-10 h-10 rounded-full bg-gray-200 flex items-center justify-center hover:bg-gray-300 transition"
            >
            <span class="text-gray-700 font-bold uppercase">{{ userName.charAt(0) }}</span>
            </button>

            <div
            v-show="dropdownOpen"
            @click.outside="closeDropdown"
            class="absolute right-0 mt-2 w-48 bg-white border rounded-lg shadow-lg py-2 z-50"
            >

            <!-- LOGOUT (all users) -->
            <button
                @click="handleLogout"
                class="block w-full text-left px-4 py-2 text-red-500 hover:bg-gray-100"
            >
                Logout
            </button>
            </div>
        </div>
        </div>
  </nav>
</template>

<style scoped>
/* Smooth dropdown transition */
[v-cloak] > * { display: none; }
</style>
