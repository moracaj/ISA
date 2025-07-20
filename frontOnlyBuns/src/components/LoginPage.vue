



<template>
  <div class="form">
    <h2>{{ isRegister ? 'Create Account' : 'Sign In' }}</h2>
    <HomeButton />

    <template v-if="isRegister">
      <input type="text" v-model="username" placeholder="Username" />
      <input type="email" v-model="registerEmail" placeholder="Email Address" />
      <input type="password" v-model="registerPassword" placeholder="Password" />
      <input type="password" v-model="confirmPassword" placeholder="Confirm Password" />
      <input type="text" v-model="firstName" placeholder="First Name" />
      <input type="text" v-model="lastName" placeholder="Last Name" />
      <input type="text" v-model="address" placeholder="Address" />
      <button @click="register" class="submit-button">Register</button>
    </template>

    <template v-else>
      <input type="email" v-model="email" placeholder="Email" />
      <input type="password" v-model="password" placeholder="Password" />
      <button @click="login" class="submit-button">Log in</button>
    </template>

    <button @click="toggleForm" class="toggle-link">
      {{ isRegister ? 'Already registered? Log in' : 'Need an account? Register here' }}
    </button>
  </div>
</template>

<script>
import HomeButton from './HomePageButton.vue';

export default {
  components: { HomeButton },
  data() {
    return {
      isRegister: false,
      username: "",
      registerEmail: "",
      registerPassword: "",
      confirmPassword: "",
      firstName: "",
      lastName: "",
      address: "",
      email: "",
      password: "",
    };
  },
  mounted() {
    this.autoUpdatePasswords();
  },
  methods: {
    toggleForm() {
      this.$router.push({ name: 'Register' });
    },

    async autoUpdatePasswords() {
      try {
        const response = await fetch("http://localhost:8080/updatePasswords", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
        });

        const result = await response.json();

        if (!response.ok) {
          console.error("Failed to update:", result.message || "Unknown error");
        } else {
          console.log("Password update success:", result);
        }
      } catch (err) {
        console.error("Update error:", err.message);
      }
    },

    async login() {
      try {
        const response = await fetch("http://localhost:8080/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email: this.email, password: this.password }),
        });

        const result = await response.json();

        if (response.ok) {
          sessionStorage.setItem('token', result.token);
          sessionStorage.setItem('userType', result.userType);

          if (result.userType === "ADMIN") {
            this.$router.push({ name: "Admin" });
          } else if (result.userType === "REGISTERED") {
            this.$router.push({ name: "Registered" });
          } else {
            alert("Unknown user role.");
          }
        } else {
          alert(result.message || "Login failed.");
        }
      } catch (err) {
        alert("Login error: " + err.message);
      }
    },

    async register() {
      if (this.registerPassword !== this.confirmPassword) {
        alert("Passwords must match.");
        return;
      }

      try {
        const response = await fetch("http://localhost:8080/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            username: this.username,
            email: this.registerEmail,
            password: this.registerPassword,
            firstName: this.firstName,
            lastName: this.lastName,
            address: this.address,
          }),
        });

        const result = await response.json();

        if (response.ok) {
          alert("Account created. Check your email to activate.");
        } else {
          alert(result.message || "Registration problem.");
        }
      } catch (err) {
        alert("Registration error: " + err.message);
      }
    }
  }
};
</script>

<style scoped>
.form {
  margin: 30px auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #dbeafe; /* svetloplava */
  border-radius: 16px;
  padding: 35px;
  width: 420px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  gap: 18px;
}

h2 {
  font-size: 30px;
  margin-bottom: 10px;
  color: #1e3a8a;
}

input {
  font-size: 18px;
  height: 48px;
  padding: 10px;
  border: 1px solid #3b82f6;
  border-radius: 12px;
  width: 100%;
  box-sizing: border-box;
}

.submit-button {
  width: 160px;
  height: 42px;
  background-color: #2563eb; /* tamnija plava */
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #1d4ed8;
}

.toggle-link {
  margin-top: 15px;
  background: none;
  border: none;
  font-size: 14px;
  color: #1e40af;
  cursor: pointer;
  text-decoration: underline;
}
</style>
