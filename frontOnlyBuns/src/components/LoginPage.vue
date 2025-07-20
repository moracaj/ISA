<template>
  <div class="auth-box">
    <h2>{{ showRegisterForm ? 'Create Account' : 'Sign In' }}</h2>
    <HomeButton />

    <template v-if="showRegisterForm">
      <input v-model="newUsername" placeholder="Username" type="text" />
      <input v-model="newUserEmail" placeholder="Email" type="email" />
      <input v-model="newPassword" placeholder="Password" type="password" />
      <input v-model="repeatPassword" placeholder="Repeat Password" type="password" />
      <input v-model="first" placeholder="First Name" type="text" />
      <input v-model="last" placeholder="Last Name" type="text" />
      <input v-model="location" placeholder="Address" type="text" />
      <button class="action-btn" @click="submitRegistration">Sign Up</button>
    </template>

    <template v-else>
      <input v-model="loginEmail" placeholder="Email" type="email" />
      <input v-model="loginPassword" placeholder="Password" type="password" />
      <button class="action-btn" @click="performLogin">Login</button>
    </template>

    <button class="switch-btn" @click="switchForm">
      {{ showRegisterForm ? 'Already registered? Login here' : 'No account? Create one' }}
    </button>
  </div>
</template>

<script>
import HomeButton from './HomePageButton.vue';

export default {
  components: { HomeButton },
  data() {
    return {
      showRegisterForm: false,
      newUsername: "",
      newUserEmail: "",
      newPassword: "",
      repeatPassword: "",
      first: "",
      last: "",
      location: "",
      loginEmail: "",
      loginPassword: ""
    };
  },
  methods: {
    switchForm() {
      this.$router.push({ name: 'Register' });
    },
    async performLogin() {
      try {
        const res = await fetch("http://localhost:8080/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email: this.loginEmail, password: this.loginPassword })
        });

        const result = await res.json();

        if (res.ok) {
          sessionStorage.setItem("token", result.token);
          sessionStorage.setItem("userType", result.userType);

          if (result.userType === "ROLE_ADMIN") {
            this.$router.push({ name: "Admin" });
          } else if (result.userType === "ROLE_REGISTERED") {
            this.$router.push({ name: "Registered" });
          } else {
            alert("Unsupported user type.");
          }
        } else {
          alert(result.message || "Login failed.");
        }
      } catch (err) {
        alert("Login error: " + err.message);
      }
    },
    async submitRegistration() {
      if (this.newPassword !== this.repeatPassword) {
        alert("Passwords do not match.");
        return;
      }

      try {
        const res = await fetch("http://localhost:8080/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            username: this.newUsername,
            email: this.newUserEmail,
            password: this.newPassword,
            firstName: this.first,
            lastName: this.last,
            address: this.location
          })
        });

        const result = await res.json();

        if (res.ok) {
          alert("Account created successfully!");
        } else {
          alert(result.message || "Registration error.");
        }
      } catch (err) {
        alert("Registration error: " + err.message);
      }
    }
  }
};
</script>

<style scoped>
.auth-box {
  margin-top: 40px;
  margin-left: auto;
  margin-right: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fff0e5;
  border-radius: 12px;
  padding: 35px;
  width: 580px;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.15);
  gap: 18px;
}

h2 {
  font-size: 32px;
  color: #bf360c;
  margin-bottom: 15px;
}

input {
  font-size: 18px;
  height: 48px;
  padding: 10px;
  width: 100%;
  border: 1px solid #ffb184;
  border-radius: 12px;
  background-color: #fff7ef;
}

.action-btn {
  width: 160px;
  height: 42px;
  background-color: #ff8c42;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.25s;
}

.action-btn:hover {
  background-color: #c94f00;
}

.switch-btn {
  margin-top: 12px;
  background: transparent;
  border: none;
  font-size: 15px;
  color: #c94f00;
  text-decoration: underline;
  cursor: pointer;
}
</style>
