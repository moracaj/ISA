<template>
  <div class="admin-page">
    <h2>Admin Dashboard</h2>
    <SignOutButton/>
    
    <div v-if="isAdmin">
      <!-- Load Registered Users Button -->
      <button @click="fetchRegisteredUsers" class="my-button">Load Registered Users</button>

      <!-- Search Form -->
      <div class="search-form">
        <h3>Search Registered Users:</h3>
        <input type="text" v-model="searchCriteria.firstName" placeholder="First Name" />
        <input type="text" v-model="searchCriteria.lastName" placeholder="Last Name" />
        <input type="text" v-model="searchCriteria.email" placeholder="Email" />
        <input type="number" v-model="searchCriteria.minPosts" placeholder="Min Posts" />
        <input type="number" v-model="searchCriteria.maxPosts" placeholder="Max Posts" />
        <button @click="searchRegisteredUsers" class="my-button">Search</button>
      </div>

      <!-- Sort Options -->
      <div class="sort-options">
        <label for="sortAttribute">Sort by:</label>
        <select v-model="sortAttribute" id="sortAttribute">
          <option value="email">Email</option>
          <option value="followersCount">Followers Count</option>
        </select>

        <label for="sortOrder">Order:</label>
        <select v-model="sortOrder" id="sortOrder">
          <option value="asc">Ascending</option>
          <option value="desc">Descending</option>
        </select>
        <button @click="sortRegisteredUsers" class="my-button">Sort</button>
      </div>

      <!-- Registered Users List -->
      <div v-if="registeredUsers.length">
        <h3>List of Registered Users:</h3>
        <div v-for="user in registeredUsers" :key="user.id" class="user-card">
          <p><strong>First name:</strong> {{ user.firstName }} </p>
          <p><strong>Last name:</strong> {{ user.lastName }}</p>
          <p><strong>Email:</strong> {{ user.email }}</p>
          <p><strong>Posts Count:</strong> {{ user.postsCount }}</p>
          <p><strong>Followers Count:</strong> {{ user.followersCount }}</p>
        </div>
      </div>

      <!-- Error or No Users Found -->
      <div v-else>
        <p>No registered users found.</p>
      </div>
      <div v-if="errorMessage" class="error-message">
        <p>{{ errorMessage }}</p>
      </div>
    </div>

    <!-- Permission Message -->
    <div v-else>
      <p>You do not have permission to view this page.</p>
    </div>
  </div>
</template>

<script>
import SignOutButton from './SignOutButton.vue';

export default {
  data() {
    return {
      registeredUsers: [],
      isAdmin: false,
      errorMessage: '',
      searchCriteria: {
        firstName: '',
        lastName: '',
        email: '',
        minPosts: 0,  // Default 0 for search, it will only show users with at least 0 posts
        maxPosts: 0   // Default 0 for search, it will allow all users up to 0 posts
      },

      sortAttribute: 'email',
      sortOrder: 'asc'
    };
  },
  created() {
    this.checkAdmin();
    const token = sessionStorage.getItem('token');
    if (this.isAdmin && token) {
      this.fetchRegisteredUsers();
    }
  },
  components: {
    SignOutButton
  },
  methods: {
    async checkAdmin() {
      const userType = sessionStorage.getItem('userType');
      this.isAdmin = userType === 'ADMIN';
    },

    async fetchRegisteredUsers() {
      this.errorMessage = '';
      const token = sessionStorage.getItem('token');
      if (this.isAdmin && token) {
        try {
          const response = await fetch("http://localhost:8080/registered-users", {
            method: "GET",
            headers: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          });
          if (response.ok) {
            const data = await response.json();
            this.registeredUsers = data;
          } else {
            this.errorMessage = 'Failed to load registered users';
          }
        } catch (error) {
          this.errorMessage = "An error occurred while loading registered users: " + error.message;
        }
      } else {
        this.errorMessage = "You do not have permission to fetch registered users.";
      }
    },

    async searchRegisteredUsers() {
      this.errorMessage = '';
      const token = sessionStorage.getItem('token');
      if (this.isAdmin && token) {
        try {
          const queryParams = new URLSearchParams();

          // Add search criteria only if they're not empty
          Object.keys(this.searchCriteria).forEach(key => {
            if (this.searchCriteria[key] !== null && this.searchCriteria[key] !== '') {
              queryParams.append(key, this.searchCriteria[key]);
            }
          });

          const response = await fetch(`http://localhost:8080/searchReg?${queryParams.toString()}`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json',
            },
          });

          const data = await response.json();
          if (data.length === 0) {
            this.registeredUsers = []; 
            this.errorMessage = 'No users found matching your criteria.';
          } else {
            this.registeredUsers = data;
          }
        } catch (error) {
          this.errorMessage = 'An error occurred while searching for registered users: ' + error.message;
        }
      } else {
        this.errorMessage = 'You do not have permission to search registered users.';
      }
    },

    sortRegisteredUsers() {
      if (this.sortAttribute && this.sortOrder) {
        this.registeredUsers.sort((a, b) => {
          let comparison = 0;
          if (a[this.sortAttribute] > b[this.sortAttribute]) {
            comparison = 1;
          } else if (a[this.sortAttribute] < b[this.sortAttribute]) {
            comparison = -1;
          }
          return this.sortOrder === 'asc' ? comparison : -comparison;
        });
      }
    }
  }
};
</script>

<style scoped>
.admin-page {
  margin: 20px;
  padding: 20px;
  background-color: #f3f4f6;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  color: #1c6124;
}

.user-card {
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 15px;
  margin-bottom: 10px;
}

.my-button {
  background-color: #1c6124;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.my-button:hover {
  background-color: #15481b;
}

.error-message {
  color: red;
}

.search-form, .sort-options {
  margin-bottom: 20px;
}
</style>
