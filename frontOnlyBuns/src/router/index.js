import { createRouter, createWebHistory } from 'vue-router'; 
import LoginPage from '@/components/LoginPage.vue'; 
import DashboardPage from '@/components/DashboardPage.vue'; // Import your Dashboard component
import RegisterPage from '@/components/RegisterPage.vue'; // Ensure this path is correct
import AdminPage from '@/components/AdminPage.vue'; // Import your Admin component
import RegisteredPage from '@/components/RegisteredPage.vue'; // Import your Admin component
import UserProfile from '@/components/UserProfile.vue';

const routes = [
  // {
  //   path: '/',
  //   name:'Home',
  //   component:LoginPage
  // },
  {

    path: '/login',
    name: 'Login',
    component: LoginPage,
  },
  {
    path: '/registered',
    name: 'Registered',
    component: RegisteredPage,
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage,
  },
  {
    path: '/dashboard', // Define the dashboard route
    name: 'Dashboard',
    component: DashboardPage, // Ensure this component exists
  },
  {
    path: '/admin', // Define the admin route
    name: 'Admin',
    component: AdminPage, // Ensure this component exists
    meta: { requiresAuth: true } // Add meta to require authentication
  },
  {
    path: '/profile/:username',
    name: 'UserProfile',
    component: UserProfile, // This should be the component for the profile page
    props: true
 }

];

const router = createRouter({
  history: createWebHistory(), 
  routes,
});

// Navigation guard to protect admin route
router.beforeEach((to, from, next) => {
  const userType = sessionStorage.getItem('userType'); // Get user type from sessionStorage
  if (to.matched.some(record => record.meta.requiresAuth) && userType !== 'ADMIN') {
    next({ path: '/' }); // Redirect to login if not admin
  } else {
    next(); // Allow access
  }
});

export default router;
