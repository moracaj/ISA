<template>
  <div class="form">
    <HomeButton />
    <h2>{{ isRegister ? 'Registration' : 'Log in' }}</h2>
    
    <template v-if="isRegister">
      <div class="input-grid">
        <div>
          <input type="text" v-model="username" placeholder="Enter your username" @blur="checkUsernameAvailability" />
          <span class="error" v-if="errors.username">{{ errors.username }}</span>
        </div>

        <div>
          <input type="email" v-model="registerEmail" placeholder="Enter your email" @blur="checkEmailAvailability" />
          <span class="error" v-if="errors.email">{{ errors.email }}</span>
        </div>

        <div class="password-container">
          <input
            :type="showPassword ? 'text' : 'password'"
            v-model="registerPassword"
            placeholder="Enter your password"
          />
          <span class="error" v-if="errors.registerPassword">{{ errors.registerPassword }}</span>
        </div>

        <div class="password-container">
          <input
            :type="showConfirmPassword ? 'text' : 'password'"
            v-model="confirmPassword"
            placeholder="Confirm your password"
          />
          <span class="error" v-if="errors.confirmPassword">{{ errors.confirmPassword }}</span>
        </div>

        <div>
          <input type="text" v-model="firstName" placeholder="Enter your first name" />
          <span class="error" v-if="errors.firstName">{{ errors.firstName }}</span>
        </div>

        <div>
          <input type="text" v-model="lastName" placeholder="Enter your last name" />
          <span class="error" v-if="errors.lastName">{{ errors.lastName }}</span>
        </div>

        <div>
          <input type="text" v-model="address" placeholder="Enter your address" />
          <span class="error" v-if="errors.address">{{ errors.address }}</span>
        </div>
      </div>

      <div class="button-container2">
        <button @click="register" class="my-button2">Register</button>
      </div>
    </template>

    <template v-else>
      <input type="email" v-model="email" placeholder="Enter your email" />
      <span class="error" v-if="errors.email">{{ errors.email }}</span>

      <div class="password-container">
        <input
          :type="showPassword ? 'text' : 'password'"
          v-model="password"
          placeholder="Enter your password"
        />
        <span class="error" v-if="errors.password">{{ errors.password }}</span>
      </div>

      <div class="button-container2">
        <button @click="login" class="my-button2">Log in</button>
      </div>
    </template>

    <button @click="toggleForm" class="toggle-button2">
      {{ isRegister ? 'Already have an account? Log in' : "Don't have an account? Register" }}
    </button>
  </div>
</template>

<script>

import HomeButton from './HomePageButton.vue';
export default {
  components:{
      HomeButton
    },
  data() {
    return {
      isRegister: true,
      username: "",
      registerEmail: "",
      registerPassword: "",
      confirmPassword: "",
      firstName: "",
      lastName: "",
      address: "",
      email: "",
      password: "",
      showPassword: false,
      showConfirmPassword: false,
      errors: {} // Object to hold error messages
    };
  },
  methods: {
    toggleForm() {
      this.$router.push({ name: 'Login' });
    },
    clearErrors() {
      this.errors = {};
    },
    components:{
      HomeButton
    },
    async checkUsernameAvailability() {
      this.errors.username = ''; // Clear previous error
      try {
        const response = await fetch('http://localhost:8080/check-username', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ username: this.username }),
        });

        const data = await response.json();
        if (response.ok) {
          // Username is available, no action needed
        } else {
          // Username is already taken, show error
          this.errors.username = data.username;
        }
      } catch (error) {
        console.error('Error checking username:', error);
      }
    },
    async checkEmailAvailability() {
      this.errors.email = ''; // Clear previous error
      try {
        const response = await fetch('http://localhost:8080/check-email', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ email: this.registerEmail }),
        });

        const data = await response.json();
        if (response.ok) {
          // Email is available, no action needed
        } else {
          // Email is already in use, show error
          this.errors.email = data.email;
        }
      } catch (error) {
        console.error('Error checking email:', error);
      }
    },
    async register() {
      this.clearErrors(); // Reset errors

      // Validate inputs and set error messages
      if (!this.username.trim()) this.errors.username = "Username is required."; // Added username validation
      if (!this.registerEmail.trim()) this.errors.email = "Email is required.";
      if (!this.registerPassword.trim()) this.errors.registerPassword = "Password is required.";
      if (this.registerPassword !== this.confirmPassword) {
        this.errors.confirmPassword = "Passwords do not match.";
      }
      if (!this.firstName.trim()) this.errors.firstName = "First name is required.";
      if (!this.lastName.trim()) this.errors.lastName = "Last name is required.";

      // If there are validation errors, stop form submission
      if (Object.keys(this.errors).length > 0) {
        return;
      }

      // Send registration request to the backend
      try {
        const response = await fetch("http://localhost:8080/register", {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            username: this.username,
            email: this.registerEmail,
            password: this.registerPassword,
            first_name: this.firstName,
            last_name: this.lastName,
            address: this.address
          })
        });

        const data = await response.json();

        if (response.ok) {
          alert("Registration successful!");
          this.clearForm(); // Clear form upon success
        } else {
          // Display the error messages from backend response
          Object.assign(this.errors, data);
        }
      } catch (error) {
        alert("An error occurred: " + error.message);
      }
    },
    clearForm() {
      this.username = "";
      this.registerEmail = "";
      this.registerPassword = "";
      this.confirmPassword = "";
      this.firstName = "";
      this.lastName = "";
      this.address = "";
      this.clearErrors(); // Clear errors on form reset
    }
  }
};
</script>

<style scoped>
.form {
  margin-top: 30px;
  margin-left: auto;
  margin-right: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #7fc59e;
  border-radius: 15px;
  padding: 30px;
  width: 600px;
  height: auto;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  gap: 20px;
}

h2 {
  font-size: 34px;
  margin-bottom: 20px;
  color: #eeffef;
}

.input-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
  width: 100%;
  box-sizing: border-box;
}

input {
  font-size: 19px;
  height: 50px;
  padding: 10px;
  border: 1px solid #0c623b;
  border-radius: 15px;
  width: 100%;
  box-sizing: border-box; /* Ensures padding doesn’t affect width */
}

.password-container {
  display: flex;
  flex-direction: column;
  position: relative;
  width: 100%;
}

.my-button2 {
  width: 150px;
  height: 40px;
  background-color: #0c623b;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.3s;
  align-self: center;
}

.my-button2:hover {
  background-color: #eeffef;
}

.toggle-button2 {
  margin-top: 10px;
  background: none;
  border: none;
  font-size: 15px;
  color: #000;
  cursor: pointer;
  text-decoration: underline;
  align-self: center;
}

.error {
  color: red;
  font-size: 14px;
  margin-top: 5px;
  height: 15px; /* Fixed height to prevent container expansion */
  overflow: hidden; /* Prevents text overflow */
}
</style>
