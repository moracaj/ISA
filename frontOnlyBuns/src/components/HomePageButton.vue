<template>
    <div class="home-button-container">
      <button @click="goToHome" class="home-button">
       
        Back
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
 background-color: #007BFF;   /* Bootstrap plava */
  color: white;                /* Tekst bele boje */
  padding: 14px 28px;          /* Veće dimenzije */
  border: none;
  border-radius: 8px;          /* Malo zaobljenije */
  cursor: pointer;
  font-size: 18px;             /* Veći font */
  font-weight: bold;           /* Bold tekst */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);  /* Lagani senka efekat */
  transition: background-color 0.3s ease;           /* Text size */
}

.home-button:hover {
  background-color: #0056b3;  /* Darken the button on hover */
}
</style>
