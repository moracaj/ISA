<template>
    <div class="home-button-container">
      <button @click="goToHome" class="home-button">
        <!-- Font Awesome House Icon -->
        <i class="fas fa-home"></i>
      </button>
    </div>
  </template>
  
  
<script>
export default {
  methods: {
    goToHome() {
      const token = sessionStorage.getItem('token');  // Get token from sessionStorage
      if (token) {
        // Redirect with token if it exists
        window.location.href = `http://localhost:8081?token=${token}`;
      } else {
        // Show alert and redirect without token if it doesn't exist
         window.location.href = "http://localhost:8081";  // Redirect to home without token
      }
    },
  },
};
</script>

  
<style>
.home-button-container {
  position: fixed;    /* Fix the button in place */
  top: 10px;          /* Distance from the top of the screen */
  right: 10px;        /* Distance from the right edge of the screen */
  z-index: 1000;      /* Ensure it's above other content */
}

.home-button {
  background-color: #f5f5f5;  /* Button background color */
  padding: 10px 20px;         /* Button padding */
  border: none;               /* Remove border */
  border-radius: 5px;         /* Rounded corners */
  cursor: pointer;            /* Change cursor on hover */
  font-size: 16px;             /* Text size */
}

.home-button:hover {
  background-color: #e0e0e0;  /* Darken the button on hover */
}
</style>
