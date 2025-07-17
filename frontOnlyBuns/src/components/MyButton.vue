<template>
  <div class="button-container">
    <button @click="goToLogin" class="my-button login">Log in</button>
    <button @click="goToRegister" class="my-button register">Sign in</button>
  
  </div>
</template>


<script>
export default {
  methods: {
    goToLogin() {
      console.log("Navigating to Login");
      this.$router.push({ name: 'Login' });
    },
    goToRegister() {
      console.log("Navigating to Register");
      this.$router.push({ name: 'Register' });
    }
  }
}
</script>

  
  <style>
.button-container {
  display: flex; 
  position: fixed; /* Fixed position */
  top: 30px; /* 30px from the top */
  right: 30px; /* 30px from the right */
  gap: 10px; /* Space between buttons */
  flex-wrap: wrap; /* Allows buttons to wrap if necessary */
}

.my-button {
  padding: 15px; /* Padding for the button */
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 1em; /* Font size */
  flex: 1; /* Allows buttons to be the same width */
  min-width: 90px; /* Minimum button width */
  max-width: 50%; /* Makes text responsive */
  box-sizing: border-box; /* Include padding in width calculations */
}

/* Styles for the first button (Log in) */
.my-button.login {
  background-color: #0c623b; /* Dark red background */
  color: white; /* White text */
}

.my-button.login:hover {
  background-color: #0c623b; /* Slightly lighter on hover */
}

/* Styles for the second button (Sign in) */
.my-button.register {
  background-color: white; /* White background */
  color: #0c623b; /* Dark red text */
  border: 2px solid #0c623b; /* Dark red border */
}

.my-button.register:hover {
  background-color: #f2f2f2; /* Light gray background on hover */
}

.rabbit-gif {
  position: fixed; /* Sticks logo to the top-left corner */
  top: 20px;
  left: 20px;
  width: 150px; /* Adjusted width for responsive layout */
  height: auto; /* Maintains aspect ratio */
  z-index: 10;
  border-radius: 15px;
  padding: 5px;

}

  </style>
  