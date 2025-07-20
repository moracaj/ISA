<template>
  <div class="admin-dashboard">
    <h2>Admin Dashboard</h2>
    <SignOutButton />

    <div v-if="adminRights">
      <button @click="loadUsers" class="action-button">Load Registered Users</button>
    </div>
  </div>
</template>

<script>
import SignOutButton from './SignOutButton.vue';

export default {
  components: {
    SignOutButton
  },
  data() {
    return {
      usersList: [],
      adminRights: false,
      errorText: '',
      filters: {
        firstName: '',
        lastName: '',
        email: '',
        minPostCount: 0,
        maxPostCount: 0
      },
      sortBy: 'email',
      order: 'asc'
    };
  },
  created() {
    this.verifyAdminPrivileges();
    const savedToken = sessionStorage.getItem('token');
    if (this.adminRights && savedToken) {
      this.loadUsers();
    }
  },
  methods: {
    async verifyAdminPrivileges() {
      const role = sessionStorage.getItem('userType');
      this.adminRights = role === 'ADMIN';
    },
    async loadUsers() {
      // placeholder: backend fetch logic goes here
      console.log("Fetching users...");
    }
  }
};
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
  margin: 20px;
  background-color: #f5f7fa;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

h2 {
  color: #14532d;
}

.action-button {
  background-color: #14532d;
  color: #fff;
  border: none;
  padding: 10px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: 0.3s ease;
}

.action-button:hover {
  background-color: #0e3f21;
}

.error-text {
  color: crimson;
}

.filters,
.sorting-controls {
  margin-bottom: 18px;
}
</style>
