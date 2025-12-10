import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth'

import Dashboard from '@/views/Dashboard.vue';
import Login from '@/components/Login.vue';
import Register from '@/components/Register.vue';
import AddActivity from '@/components/AddActivity.vue';
import EditActivity from '@/components/EditActivity.vue';

const routes = [

    // DASHBOARD (fixed typo)
    {
        path: '/',
        name: 'dashboard',
        component: Dashboard,
        meta: { requiresAuth: true }
    },

    // AUTH
    {
        path: '/login',
        name: 'login',
        component: Login
    },
    {
        path: '/register',
        name: 'register',
        component: Register
    },
    {
        path: '/activity/add',
        name: 'add-activity',
        component: AddActivity
    },
    {
        path: '/activity/:id',
        name: 'edit-activity',
        component: EditActivity
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach(async (to) => {
    const auth = useAuthStore();

    // restore session once
    if (!auth.initialized) {
        await auth.restoreSession();
    }

    // requires login
    if (to.meta.requiresAuth && !auth.isAuthenticated) {
        return '/login';
    }

    // logged-in users cannot visit login page
    if (to.name === 'login' && auth.isAuthenticated) {
        return auth.account?.role === 'ADMIN' ? '/admin' : '/dashboard';
    }
});

export { router };