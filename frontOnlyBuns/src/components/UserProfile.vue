<template>
    <div>
      <h1>User Profile</h1>
      <!-- Display the username -->
      <p><strong>Username:</strong> {{ username }}</p>
      <!-- Additional profile information can be added here -->
    </div>
  </template>
  
  <script>
  export default {
    name: 'UserProfile',
    props: ['username'],
    created() {
      // Fetch the user's profile information using the username
      console.log("Fetching profile information for:", this.username);
    },
  };
  </script>
  
  <style>
  .clickable-username {
    color: #d35400;
    cursor: pointer;
    text-decoration: underline;
  }
  </style>
  