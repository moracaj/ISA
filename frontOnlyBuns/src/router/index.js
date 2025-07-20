import { createRouter, createWebHistory } from 'vue-router'; 
import LoginPage from '@/components/LoginPage.vue'; 
//import DashboardPage from '@/components/DashboardPage.vue'; // Import your Dashboard component
import RegisterPage from '@/components/RegisterPage.vue'; // Ensure this path is correct
import AdminPage from '@/components/AdminPage.vue'; // Import your Admin component
import RegisteredPage from '@/components/RegisteredPage.vue'; // Import your Admin component
import UserProfile from '@/components/UserProfile.vue';
//import ListOfPostsPage from '@/components/ListOfPostsPage.vue';
//import UserProfilesPage from '@/components/UserProfilesPage.vue';
//import AllUsers from '@/components/AllUsers.vue';
//import ProfilePage from '@/components/ProfilePage.vue';
import NearbyPosts from '@/components/NearbyPosts.vue'




const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginPage,
  },
  // {
  //   path: '/all-users',
  //   name: 'AllUsers',
  //   component: AllUsers,  // Ovde postavljamo rutu koja vodi ka stranici sa svim korisnicima
  // },
  // {
  //   path: '/list-of-posts',
  //   name: 'ListOfPosts',
  //   component: ListOfPostsPage
  // },
  // {
  //   path: '/user-profiles',
  //   name: 'UserProfiles',
  //   component: UserProfilesPage
  // },
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
  // {
  //   path: '/dashboard', // Define the dashboard route
  //   name: 'Dashboard',
  //   component: DashboardPage, // Ensure this component exists
  // },
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
   },
  //  {
  //   path: '/profileN',
  //   name: 'ProfilePage',
  //   component: ProfilePage,
  //   props: true 
  // },
  {
    path: '/nearby-posts',
    name: 'NearbyPosts',
    component: NearbyPosts
  },

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

router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem('token');
  
  if (token) {
    to.query = { ...to.query, token };
  }

  next();
});


export default router;
